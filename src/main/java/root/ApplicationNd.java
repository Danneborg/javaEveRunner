package root;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ApplicationNd {

    public void compare(BufferedImage bufferedImage1 , BufferedImage bufferedImage2){
        // Load first image
        INDArray image1 = imageToINDArray(bufferedImage1);
        // Load second image
        INDArray image2 = imageToINDArray(bufferedImage2);

        // Compare images
        double difference = Transforms.abs(image1.sub(image2)).sumNumber().doubleValue();

        // Define a threshold for considering images equal
        double threshold = 0.001;

        // Check if images are equal
        boolean imagesEqual = difference < threshold;

        if (imagesEqual) {
            System.out.println("The images are equal.");
        } else {
            System.out.println("The images are different.");
        }
    }

    public void run(){
        // Paths to the two images
        String image1Path = "sample.jpg";
        String image2Path = "testSample.jpg";

        // Load first image
        BufferedImage bufferedImage1 = loadImage(image1Path);
        INDArray image1 = imageToINDArray(bufferedImage1);

        // Load second image
        BufferedImage bufferedImage2 = loadImage(image2Path);
        INDArray image2 = imageToINDArray(bufferedImage2);

        // Compare images
        double difference = Transforms.abs(image1.sub(image2)).sumNumber().doubleValue();

        // Define a threshold for considering images equal
        double threshold = 0.001;

        // Check if images are equal
        boolean imagesEqual = difference < threshold;

//        if (imagesEqual) {
//            System.out.println("The images are equal.");
//        } else {
//            System.out.println("The images are different.");
//        }
    }

//    public static void main(String[] args) {
//        // Paths to the two images
//        String image1Path = "sample.jpg";
//        String image2Path = "testSample.jpg";
//
//        // Load first image
//        BufferedImage bufferedImage1 = loadImage(image1Path);
//        INDArray image1 = imageToINDArray(bufferedImage1);
//
//        // Load second image
//        BufferedImage bufferedImage2 = loadImage(image2Path);
//        INDArray image2 = imageToINDArray(bufferedImage2);
//
//        // Compare images
//        double difference = Transforms.abs(image1.sub(image2)).sumNumber().doubleValue();
//
//        // Define a threshold for considering images equal
//        double threshold = 0.001;
//
//        // Check if images are equal
//        boolean imagesEqual = difference < threshold;
//
//        if (imagesEqual) {
//            System.out.println("The images are equal.");
//        } else {
//            System.out.println("The images are different.");
//        }
//    }

    public static BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static INDArray imageToINDArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        INDArray array = Nd4j.create(height, width, 3);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                double red = (pixel >> 16) & 0xff;
                double green = (pixel >> 8) & 0xff;
                double blue = pixel & 0xff;

                array.putScalar(y, x, 0, red);
                array.putScalar(y, x, 1, green);
                array.putScalar(y, x, 2, blue);
            }
        }

        return array;
    }
}
