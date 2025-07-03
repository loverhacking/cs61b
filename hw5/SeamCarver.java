import edu.princeton.cs.algs4.Picture;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class SeamCarver {

    private Picture image;
    private final int width;
    private final int height;

    /** cost of minimum cost path ending at (i, j) */
    private double[][] M;

    /**
     * pixel (x, y) refers to the pixel in column x and row y,
     * with pixel (0, 0) in the upper left corner
     * and pixel (W − 1, H − 1) in the bottom right corner.
     */

    public SeamCarver(Picture picture) {
        image = new Picture(picture);
        width = image.width();
        height = image.height();

    }

    // current picture
    public Picture picture() {
        return new Picture(image);
    }
    // width of current picture
    public  int width() {
        return width;
    }

    // height of current picture
    public  int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        int thisWidth = image.width();
        int thisHeight = image.height();

        if (x < 0 || x > thisWidth - 1 || y < 0 || y > thisHeight - 1) {
            throw new IndexOutOfBoundsException();
        }

        int leftX = x - 1;
        int rightX = x + 1;
        int upY = y - 1;
        int belowY = y + 1;

        if (x == 0) {
            leftX = thisWidth - 1;
        }
        if (x == thisWidth - 1) {
            rightX = 0;
        }
        if (y == 0) {
            upY = thisHeight - 1;
        }
        if (y == thisHeight - 1) {
            belowY = 0;
        }

        int deltaX = calculateSquareGradient(image.get(leftX, y), image.get(rightX, y));
        int deltaY = calculateSquareGradient(image.get(x, upY), image.get(x, belowY));
        return deltaX + deltaY;
    }

    private int calculateSquareGradient(Color color1, Color color2) {
        int color1Red = color1.getRed();
        int color2Red = color2.getRed();

        int color1Blue = color1.getBlue();
        int color2Blue = color2.getBlue();

        int color1Green = color1.getGreen();
        int color2Green = color2.getGreen();
        return (color1Red - color2Red) * (color1Red - color2Red)
                + (color1Blue - color2Blue) * (color1Blue - color2Blue)
                + (color1Green - color2Green) * (color1Green - color2Green);
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        int thisWidth = image.width();
        int thisHeight = image.height();

        // energy cost of pixel at location (i, j)
        double[][] energyPixel = new double[thisHeight][thisWidth];
        M = new double[thisHeight][thisWidth];
        int[] verticalSeam = new int[thisHeight];

        double min = Double.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < thisHeight; i++) {
            for (int j = 0; j < thisWidth; j++) {
                energyPixel[i][j] = energy(j, i);
                M[i][j] = energyPixel[i][j] + minEnergyCost(i, j);

                // find min value in last row in M[][]
                if (i == thisHeight - 1 && M[i][j] < min) {
                    min = M[i][j];
                    minIndex = j;
                }
            }
        }

        verticalSeam[thisHeight - 1] = minIndex;

        int j = minIndex;
        for (int i = thisHeight - 1; i >= 1; i--) {
            j = minMCost(i, j);
            verticalSeam[i - 1] = j;
        }
        return verticalSeam;
    }


    /** find min M[][] neighbors given i, j */
    private int minMCost(int i, int j) {
        // special case: width == 1: no need to compare
        if (image.width() == 1) {
            return j;
        }
        if (j == 0) {
            return M[i - 1][j] > M[i - 1][j + 1] ? j + 1 : j;
        }
        if (j == image.width() - 1) {
            return M[i - 1][j - 1] > M[i - 1][j] ? j : j - 1;
        }

        int tempMin = M[i - 1][j - 1] > M[i - 1][j] ? j : j - 1;
        return M[i - 1][tempMin] > M[i - 1][j + 1] ? j + 1 : tempMin;
    }

    /** find min energyPixel[][] neighbors given i, j */
    private double minEnergyCost(int i, int j) {
        if (i == 0) {
            return 0;
        }
        // special case: width == 1: no need to compare
        if (image.width() == 1) {
            return M[i - 1][j];
        }
        if (j == 0) {
            return min(M[i - 1][j], M[i - 1][j + 1]);
        }
        if (j == image.width() - 1) {
            return min(M[i - 1][j - 1], M[i - 1][j]);
        }
        return min(M[i - 1][j - 1], min(M[i - 1][j], M[i - 1][j + 1]));

    }


    // sequence of indices for horizontal seam
    public  int[] findHorizontalSeam() {
        transImage();
        int[] horizontalSeam = findVerticalSeam();
        transImage();
        return horizontalSeam;
    }


    /** transpose the image */
    private void transImage() {
        Picture newImage = new Picture(image.height(), image.width());
        for (int i = 0; i < newImage.width(); i++) {
            for (int j = 0; j < newImage.height(); j++) {
                newImage.set(i, j, image.get(j, i));
            }
        }
        image = newImage;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width || checkIllegalSeam(seam)) {
            throw new IllegalArgumentException();
        }

        SeamRemover.removeHorizontalSeam(image, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height || checkIllegalSeam(seam)) {
            throw new IllegalArgumentException();
        }

        SeamRemover.removeVerticalSeam(image, seam);
    }

    private boolean checkIllegalSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (abs(seam[i] - seam[i + 1]) > 1) {
                return true;
            }
        }
        return false;
    }
}
