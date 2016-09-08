import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Mypart2 implements ActionListener {
	
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
	public Mypart2(BufferedImage originalImg, double d, JFrame jFrame, BufferedImage smapleImg, double e, int lines) {
		this.originalImage=originalImg;
		updateDegreePerMilleSecond=d;
		f=jFrame;
		sampleImage=smapleImg;
		samplePeriod=(int)e;
		this.lines=lines;
	}

	public static void main(String[] args) {
		int lines = Integer.valueOf(args[0]);
		double speed = Double.valueOf(args[1]);
		double fps = Double.valueOf(args[2]);
		BufferedImage originalImg = ImageUtils.createImage(lines);
		BufferedImage sampleImg = ImageUtils.deepCopy(originalImg);
		JFrame jFrame=ImageUtils.show(originalImg, sampleImg);;
		Mypart2 mypart2=new Mypart2(originalImg,360*speed/1000,jFrame,sampleImg,1000/fps,lines);
		Timer timer = new Timer(delay,mypart2);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		long tmp=System.currentTimeMillis();
		curLeft=(curLeft-(int)(updateDegreePerMilleSecond*(tmp-lastTime))+360)%360;
		lastTime=System.currentTimeMillis();;
		
		if(tmp-curSampleTime>samplePeriod){
			curSampleTime=System.currentTimeMillis();;
			ImageUtils.update(sampleImage,lines,curLeft);
		}
		
		ImageUtils.update(originalImage,lines,curLeft);
		f.repaint();
	}
}
