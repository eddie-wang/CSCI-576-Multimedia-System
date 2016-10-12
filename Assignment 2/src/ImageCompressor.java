public class ImageCompressor {
	private final static int width = 352;
	private final static int height = 288;
	int n = 0;
	byte[] original = null;

	public ImageCompressor(byte[] original, int n) {
		this.n = n;
		this.original = original;
	}

	public byte[] compress() {
		double[] Y = toY();
		double[] Cb = toCb();
		double[] Cr = toCr();
		double[] DCTY=new double[Y.length];
		double[] DCTCb=new double[Cb.length];
		double[] DCTCr=new double[Cr.length];
		
		for(int x=0;x<height;x+=8)
			for(int y=0;y<width;y+=8){
				DCT(Y,DCTY,x,y,n);
				DCT(Cb,DCTCb,x,y,n);
		    	DCT(Cr,DCTCr,x,y,n);
			}
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				System.out.println(Cr[i*width+j]);
			}
		for(int x=0;x<height;x+=8)
			for(int y=0;y<width;y+=8){
				InverseDCT(Y,DCTY,x,y);
				InverseDCT(Cb,DCTCb,x,y);
				InverseDCT(Cr,DCTCr,x,y);
			}
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				System.out.println(Cr[i*width+j]);
			}		
		return toRGB(Y,Cb,Cr);
	}

	
	private double[] toCb() {
		int length=original.length/3;
		double[] Cb=new double[length];
		for (int i=0;i<length;i++){
			Cb[i]=-0.169*(original[i] & 0xff)+-0.331*(original[i+length] & 0xff)+0.5*(original[i+length*2] & 0xff);
		}
		return Cb;
	}

	private double[] toCr() {
		int length=original.length/3;
		double[] Cr=new double[length];
		for (int i=0;i<length;i++){
			Cr[i]=0.5*(original[i] & 0xff)+-0.419*(original[i+length] & 0xff)+-0.081*(original[i+length*2] & 0xff);
		}
		return Cr;
	}
	private double[] toY() {
		int length=original.length/3;
		double[] Y=new double[length];
		for (int i=0;i<length;i++){
			Y[i]=0.299*(original[i] & 0xff)+0.587*(original[i+length]& 0xff)+0.114*(original[i+length*2] & 0xff);
		}
		return Y;
	}
	
	
	private byte[] toRGB(double [] y, double [] cb, double [] cr) {
		byte[] rgb=new byte[y.length*3];
		for(int i=0;i<y.length;i++){
			rgb[i]= converHelper(1.0*(y[i])+1.402*cr[i]) ;
			rgb[i+y.length]=converHelper(1.0*(y[i])+ -0.344*cb[i]+-0.714*cr[i]);
			rgb[i+y.length*2]=converHelper(1.00*(y[i])+ 1.772*cb[i]+0.0*cr[i]);	
		}
		return rgb;
	}
	private byte converHelper(double t){
		if(t>=255.0) {
			t=255;
		}
		return (byte)t;
	}
	private void InverseDCT(double[] channel, double[] DCTChannel, int startX,
			int startY) {
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++) {
				double t = 0;
				for (int u = 0; u < 8; u++)
					for (int v = 0; v < 8; v++) {
						double cv = 1,cu=1;
						if (u ==0)cu=1/(Math.sqrt(2));
						if (v ==0)cv=1/(Math.sqrt(2));
						t += cu*cv*DCTChannel[(startX + u) * width + startY + v]
								* Math.cos((2 * x + 1) * Math.PI * u / 16)
								* Math.cos((2 * y + 1) * Math.PI * v / 16);
					}
				channel[(startX + x) * width + startY + y]=t/4.0;
			}
	}

	private void DCT(double[] channel, double[] DCTChannel, int startX,
			int startY, int n) {
		for (int u = 0; u < 8; u++)
			for (int v = 0; v < 8; v++) {
				if (u + v < n) {
					double cu=1,cv = 1;
					if (u==0) cu=1/(Math.sqrt(2));
					if (v==0) cv=1/(Math.sqrt(2));
					double t = 0;
					for (int x = 0; x < 8; x++)
						for (int y = 0; y < 8; y++)
							t += channel[(startX + x) * width + startY + y]
									* Math.cos((2 * x + 1) * Math.PI * u / 16)
									* Math.cos((2 * y + 1) * Math.PI * v / 16);
					DCTChannel[(startX + u) * width + startY + v] = cu*cv* t/4.0;
				}
				else{
					DCTChannel[(startX + u) * width + startY + v]=0.0;
				}
				
			}
		
	}

}
