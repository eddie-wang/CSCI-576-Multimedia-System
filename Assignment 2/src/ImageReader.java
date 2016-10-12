
import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

public class ImageReader {

	
	private final static  int width=352;
	private final static int height=288;
	
	public  static void  showIms(BufferedImage original , BufferedImage modificated){
		
		JFrame frame;
		JLabel lbIm1;
		JLabel lbIm2;
		// Use labels to display the images
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		JLabel lbText1 = new JLabel("Original image (Left)");
		lbText1.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lbText2 = new JLabel("Image after modification (Right)");
		lbText2.setHorizontalAlignment(SwingConstants.CENTER);
		lbIm1 = new JLabel(new ImageIcon(original));
		lbIm2 = new JLabel(new ImageIcon(modificated));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(lbText1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		frame.getContentPane().add(lbText2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		frame.getContentPane().add(lbIm2, c);

		frame.pack();
		frame.setVisible(true);
	}

	public static byte[] read(String fileName) {
		
		byte[] bytes=null;
		try {
			File file = new File(fileName);
			InputStream is = new FileInputStream(file);

			long len = file.length();
			 bytes = new byte[(int)len];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
			
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static BufferedImage toBufferedImage(byte[] bytes){
		int ind = 0;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < height; y++){

			for(int x = 0; x < width; x++){

				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 

				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x,y,pix);
				ind++;
			}
		}
		return img;
	}
}
