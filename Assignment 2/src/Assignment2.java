import java.awt.image.BufferedImage;


public class Assignment2 {
 public static void main(String[] args) {
	 String fileName=args[0];
	 int n=  Integer.valueOf(args[1]);
	 byte []  original=ImageReader.read(fileName);
	 byte[] modificated = new ImageCompressor(original,n).compress();
	 ImageReader.showIms(ImageReader.toBufferedImage(original), ImageReader.toBufferedImage(modificated));
}
}
