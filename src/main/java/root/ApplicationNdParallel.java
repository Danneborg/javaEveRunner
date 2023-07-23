package root;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public class ApplicationNdParallel {

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
        double difference = IntStream.range(0, (int) image1.length())
                .parallel()
                .mapToDouble(i -> image1.getDouble(i) - image2.getDouble(i))
                .map(Math::abs)
                .sum();

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
//        String image1Path = "path/to/image1.jpg";
//        String image2Path = "path/to/image2.jpg";
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
//        double difference = IntStream.range(0, (int) image1.length())
//                .parallel()
//                .mapToDouble(i -> image1.getDouble(i) - image2.getDouble(i))
//                .map(Math::abs)
//                .sum();
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

    private static BufferedImage loadImage(String imagePath) {
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

        IntStream.range(0, height).parallel().forEach(y -> {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                double red = (pixel >> 16) & 0xff;
                double green = (pixel >> 8) & 0xff;
                double blue = pixel & 0xff;

                array.putScalar(y, x, 0, red);
                array.putScalar(y, x, 1, green);
                array.putScalar(y, x, 2, blue);
            }
        });

        return array;
    }
}
