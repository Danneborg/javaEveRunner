package root;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageComparator {

    private Map<String, double[][]> sampleImages;

    public ImageComparator() {
        sampleImages = new HashMap<>();
    }

    public void preloadSampleImage(String sampleName, File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        double[][] pixelValues = convertImageToDoubleArray(image);
        sampleImages.put(sampleName, pixelValues);
    }

    public boolean compareWithSample(String sampleName, Rectangle screenArea, double similarityPercentage) throws Exception {
        if (!sampleImages.containsKey(sampleName)) {
            throw new IllegalArgumentException("Sample '" + sampleName + "' is not preloaded.");
        }

        BufferedImage capturedImage = captureScreen(screenArea);
        File output = new File("testSample.jpg");
        ImageIO.write(capturedImage, "jpg", output);
        double[][] capturedPixels = convertImageToDoubleArray(capturedImage);

        double[][] samplePixels = sampleImages.get(sampleName);
        double actualSimilarityPercentage = compareImagesNd4(samplePixels, capturedPixels);

        return actualSimilarityPercentage >= similarityPercentage;
    }

    private double[][] convertImageToDoubleArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] pixelValues = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                pixelValues[y][x] = (double) red;
            }
        }
        return pixelValues;
    }

    private BufferedImage captureScreen(Rectangle screenArea) throws Exception {
        Robot robot = new Robot();
        return robot.createScreenCapture(screenArea);
    }



    private double compareImages(double[][] sampleImage, double[][] screenArea) {
        // Perform image comparison logic to calculate similarity percentage
        // The implementation depends on the chosen algorithm or neural network

        // Placeholder implementation using a simple pixel-wise comparison
        int width = sampleImage[0].length;
        int height = sampleImage.length;
        double totalPixels = width * height;
        int matchingPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (sampleImage[y][x] == screenArea[y][x]) {
                    matchingPixels++;
                }
            }
        }

        return (matchingPixels / totalPixels) * 100.0;
    }

    public double compareImagesNd4(double[][] sampleImage, double[][] screenArea) {
        // Convert the input arrays to INDArray
        INDArray sampleImageArray = Nd4j.create(sampleImage);
        INDArray screenAreaArray = Nd4j.create(screenArea);

        // Normalize the pixel values to a range of 0 to 1
        sampleImageArray.divi(255.0);
        screenAreaArray.divi(255.0);

        // Calculate the absolute difference between the two images
        INDArray difference = Transforms.abs(sampleImageArray.sub(screenAreaArray));

        // Calculate the percentage of similarity
        double similarityPercentage = 100.0 - (difference.sumNumber().doubleValue() / sampleImageArray.length()) * 100.0;

        return similarityPercentage;
    }


}
