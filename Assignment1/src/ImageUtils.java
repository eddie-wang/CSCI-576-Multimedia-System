import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageUtils {

	public static final int width = 512, height = 512;

	public static BufferedImage createImage(int lines) {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		int centerX = width / 2, centerY = height / 2;
		// draw background
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				byte r = (byte) 255;
				byte g = (byte) 255;
				byte b = (byte) 255;
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)
						| (b & 0xff);
				img.setRGB(x, y, pix);
			}
		}

		// draw lines
		for (int i = 0; i < lines; i++) {
			double degree = 360.00 / lines * i;
			drawLine(img, centerX, centerY, degree);
		}
		return img;
	}

	public static BufferedImage scaleImage(BufferedImage originalImage,
			int scale, boolean antiAlias) {
		return null;
	}

	public static void show(BufferedImage originalImage,
			BufferedImage scaledImage) {
		// Use a panel and label to display the image
		JPanel panel = new JPanel();
		panel.add(new JLabel(new ImageIcon(originalImage)));
		panel.add(new JLabel(new ImageIcon(originalImage)));

		JFrame frame = new JFrame("Display images");

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private static void drawLine(BufferedImage img, int startX, int startY,
			double degree) {
		int color = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8)
				| (0 & 0xff);

		if (degree < 45 || degree > 315 || degree > 135 && degree < 225) {
			int xDir = (degree >= 0 && degree < 90 || degree > 270
					&& degree < 360) ? 1 : -1;
			double y = startY;
			for (int x = startX; x >= 0 && x < 512; x += xDir) {
				img.setRGB(x, (int) Math.floor(y), color);

				double delta = Math.tan(Math.PI / 180 * degree);
				y = y + delta;
				int temp = (int) Math.floor(y);
				if (!(temp < 512 && temp >= 0))
					break;
			}
		} else {
			int yDir = (degree > 180) ? 1 : -1;
			double x = startX;
			for (int y = startY; y >= 0 && y < 512; y += yDir) {
				img.setRGB((int) Math.floor(x), y, color);
				double delta = -1 / Math.tan(Math.PI / 180 * degree);
				x = x + delta;
				int temp = (int) Math.floor(x);
				if (!(temp < 512 && temp >= 0))
					break;
			}
		}
	}
}
