import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

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
		drawBackground(img);
		// draw lines
		for (int i = 0; i < lines; i++) {
			double degree = 360.00 / lines * i;
			drawLine(img, centerX, centerY, degree);
		}
		return img;
	}

	private static void drawBackground(BufferedImage img) {
		// draw background
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				byte r = (byte) 255;
				byte g = (byte) 255;
				byte b = (byte) 255;
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)
						| (b & 0xff);
				img.setRGB(x, y, pix);
			}
		}

	}

	public static BufferedImage scaleImage(BufferedImage originalImage,
			double scale, boolean antiAlias) {
		// scale=2;
		antiAlias = false;
		if (antiAlias) {
			originalImage = lowPassFilter(originalImage);
		}
		int nWidth = (int) (width / scale), nHeight = (int) (height / scale);
		BufferedImage newImg = new BufferedImage(nWidth, nHeight,
				BufferedImage.TYPE_INT_RGB);
		drawBackground(newImg);
		for (int y = 0; y < nHeight; y++)
			for (int x = 0; x < nWidth; x++) {
				int color = getColor(originalImage, x, y, scale);
				newImg.setRGB(x, y, color);
			}
		return newImg;
	}

	private static int getColor(BufferedImage img, int x, int y, double scale) {
		int color = 0;
		color = img.getRGB((int) (x * scale), (int) (y * scale));
		return color;
	}

	private static BufferedImage lowPassFilter(BufferedImage img) {
		BufferedImage newImg = deepCopy(img);
		for (int y = 1; y < img.getHeight() - 1; y++)
			for (int x = 1; x < img.getWidth() - 1; x++) {
				int r = 0, g = 0, b = 0;
				int[] xDir = { -1, 0, 1 };
				int[] yDir = { -1, 0, 1 };
				for (int dX : xDir)
					for (int dY : yDir) {
						int tC = img.getRGB(dX + x, dY + y);
						r += (tC >> 16) & 0xff;
						g += (tC >> 8) & 0xff;
						b += (tC) & 0xff;
					}
				int color = 0xff000000 | (((r / 9) & 0xff) << 16)
						| (((g / 9) & 0xff) << 8) | ((b / 9) & 0xff);
				newImg.setRGB(x, y, color);
			}
		return newImg;
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static JFrame show(BufferedImage originalImage,
			BufferedImage scaledImage) {
		// Use a panel and label to display the image
		JPanel panel = new JPanel();
		panel.add(new JLabel(new ImageIcon(originalImage)));
		panel.add(new JLabel(new ImageIcon(scaledImage)));

		JFrame frame = new JFrame("Display images");
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;

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

				double delta = -xDir*Math.tan(Math.PI / 180 * degree);
				y = y + delta;
				int temp = (int) Math.floor(y);
				if (!(temp < 512 && temp >= 0))
					break;
			}
		} else {
			int yDir = (degree > 180) ? 1 : -1;
			int xDir = (degree >= 0 && degree < 90 || degree < 270
					&& degree < 180) ? 1 : -1;
			double x = startX;
			for (int y = startY; y >= 0 && y < 512; y += yDir) {
				img.setRGB((int) Math.floor(x), y, color);
				double delta = 1 / Math.tan(Math.PI / 180 * degree) * xDir;
				x = x + delta;
				int temp = (int) Math.floor(x);
				if (!(temp < 512 && temp >= 0))
					break;
			}
		}
	}

	public static void update(BufferedImage originalImage, int lines,int cur) {
		drawBackground(originalImage);
		for (int i = 0; i < lines; i++) {
			double degree = (360.00 / lines * i+cur)%360;
			drawLine(originalImage, 256, 256, degree);
		}
	}
}
