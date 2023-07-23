package root;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Application {

    public static void captureArea(int x, int y, int width, int height) {
        try {
            Robot robot = new Robot();
            BufferedImage screenshot = robot.createScreenCapture(new Rectangle(x, y, width, height));

            File output = new File("sample1.jpg");
            ImageIO.write(screenshot, "jpg", output);

            System.out.println("Image saved successfully.");
        } catch (AWTException | IOException e) {
            System.out.println("Failed to capture and save image: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
//        int x = 1597;        // X-coordinate of the top-left corner
//        int y = 1057;        // Y-coordinate of the top-left corner
//        int width = 11;    // Width of the captured area
//        int height = 11;   // Height of the captured area
//
//        captureArea(x, y, width, height);
//
//        ImageComparator ic = new ImageComparator();
//        ic.preloadSampleImage("sample", new File("sample.jpg"));
////        System.out.println(ic.compareWithSample("sample", new Rectangle(1741, 49, 11, 11), 80));
//        System.out.println(ic.compareWithSample("sample", new Rectangle(1596, 1056, 11, 11), 80));
        ApplicationNd applicationNd = new ApplicationNd();
        applicationNd.compare(ApplicationNd.loadImage("sample.jpg"), ApplicationNd.loadImage("sample1.jpg"));
        applicationNd.compare(ApplicationNd.loadImage("sample.jpg"), ApplicationNd.loadImage("sample.jpg"));

    }
}
