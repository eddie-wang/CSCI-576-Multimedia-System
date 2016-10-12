import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;

import javax.swing.JFrame;
import javax.swing.Timer;

public class MyExtraCredit implements ActionListener {
	
	public final static int delay = 1;
	private double updateDegreePerMilleSecond; 
	private long lastTime=0;
	private BufferedImage originalImage;
	private BufferedImage sampleImage;
	private JFrame f;
	private long curSampleTime=0;
	int curLeft=0,curRight=0;
	int samplePeriod=0;
	int lines=0;
	double scale=1;
	boolean antiAlias=false;
	public MyExtraCredit(BufferedImage originalImg, double d, JFrame jFrame, BufferedImage smapleImg, double e, int lines, double scale, boolean antiAlias) {
		this.originalImage=originalImg;
		updateDegreePerMilleSecond=d;
		f=jFrame;
		sampleImage=smapleImg;
		samplePeriod=(int)e;
		this.lines=lines;
		this.scale=scale;
		this.antiAlias=antiAlias;
	}

	public static void main(String[] args) {
		int lines = Integer.valueOf(args[0]);
		double speed = Double.valueOf(args[1]);
		double fps = Double.valueOf(args[2]);
		double scale=Double.valueOf(args[3]);
		boolean antiAlias=Integer.valueOf(args[4])==1?true:false;
		BufferedImage originalImg = ImageUtils.createImage(lines);
		BufferedImage sampleImg = ImageUtils.scaleImage(originalImg, scale, antiAlias);
		JFrame jFrame=ImageUtils.show(originalImg, sampleImg);;
		MyExtraCredit mypart2=new MyExtraCredit(originalImg,360*speed/1000,jFrame,sampleImg,1000/fps,lines,scale,antiAlias);
		Timer timer = new Timer(50,mypart2);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean updateSampleImg=false;
		long tmp=System.currentTimeMillis();
		curLeft=(curLeft-(int)(updateDegreePerMilleSecond*(tmp-lastTime))+360)%360;
		if(curLeft<0)curLeft+=360;
		lastTime=System.currentTimeMillis();
		
		if(tmp-curSampleTime>samplePeriod){
			curSampleTime=tmp;
			updateSampleImg=true;
		}
		ImageUtils.update(originalImage,sampleImage,lines,curLeft,scale,antiAlias,updateSampleImg);
		f.repaint();
	}
}
