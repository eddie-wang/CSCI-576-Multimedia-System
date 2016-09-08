import java.awt.image.BufferedImage;

public class Mypart1 {
	public static void main(String[] args) {
		int lines=0;
		double scale=0;
		boolean antiAlias=false;
		try{
		lines=Integer.valueOf(args[0]);
		scale=Double.valueOf(args[1]);
		antiAlias=Integer.valueOf(args[2])==1?true:false;
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Input format is wrong!!!");
			return ;
		}
		BufferedImage originalImage=ImageUtils.createImage(lines);
		BufferedImage scaledImage=ImageUtils.scaleImage(originalImage,scale,antiAlias);
		ImageUtils.show(originalImage,scaledImage);
	}
}