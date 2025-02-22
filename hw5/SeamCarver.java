import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class SeamCarver {

    private Picture image;
    private final int width;
    private final int height;
    private double[][] energyPixel;
    private double[][] M;

    /**
     * pixel (x, y) refers to the pixel in column x and row y,
     * with pixel (0, 0) at the upper left corner
     * and pixel (W − 1, H − 1) at the bottom right corner.
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

        int width = image.width();
        int height = image.height();

        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            throw new IndexOutOfBoundsException();
        }

        int leftX = x - 1;
        int rightX = x + 1;
        int upY = y - 1;
        int belowY = y + 1;

        if (x == 0) {
            leftX = width - 1;
        }
        if (x == width - 1) {
            rightX = 0;
        }
        if (y == 0) {
            upY = height - 1;
        }
        if (y == height - 1) {
            belowY = 0;
        }

        int deltaX = square(image.get(leftX, y).getRed() - image.get(rightX, y).getRed())
                + square(image.get(leftX, y).getGreen() - image.get(rightX, y).getGreen())
                + square(image.get(leftX, y).getBlue() - image.get(rightX, y).getBlue());
        int deltaY = square(image.get(x, upY).getRed() - image.get(x, belowY).getRed())
                + square(image.get(x, upY).getGreen() - image.get(x, belowY).getGreen())
                + square(image.get(x, upY).getBlue() - image.get(x, belowY).getBlue());

        return deltaX + deltaY;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        int width = image.width();
        int height = image.height();


        energyPixel = new double[height][width];
        // energy cost of pixel at location (i, j)
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energyPixel[i][j] = energy(j, i);
            }
        }

        // cost of minimum cost path ending at (i, j)
        M = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                M[i][j] = energyPixel[i][j] + minEnergyCost(i, j);
            }
        }


        int[] verticallSeam = new int[height];

        // find last row of min cost
        double min = M[height - 1][0];
        int minIndex = 0;
        for (int i = 0; i < width; i++) {
            if (M[height - 1][i] < min) {
                min = M[height - 1][i];
                minIndex = i;
            }
        }
        verticallSeam[height - 1] = minIndex;

        int j = minIndex;
        for (int i = height - 1; i >= 1; i--) {
            j = minMCose(i, j);
            verticallSeam[i - 1] = j;
        }
        return verticallSeam;
    }


    /** find min M[][] neighbors given i, j */
    private int minMCose(int i, int j) {
        if (j == 0) {
            return M[i - 1][j] > M[i - 1][j + 1] ? j + 1 : j;
        }
        if (j == image.width() - 1) {
            return M[i - 1][j - 1] > M[i - 1][j] ? j : j - 1;
        }

        int tempmin = M[i - 1][j - 1] > M[i - 1][j] ? j : j - 1;
        return M[i - 1][tempmin] > M[i - 1][j + 1] ? j + 1 : tempmin;

    }

    /** find min energyPixel[][] neighbors given i, j */
    private double minEnergyCost(int i, int j) {
        if (i == 0) {
            return energyPixel[i][j];
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
        transimage();
        int[] horizontalSeam = findVerticalSeam();
        transimage();
        return horizontalSeam;

    }


    /** transpose the image */
    private void transimage() {
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
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }
        if (checkIllegalSeam(seam)) {
            throw new IllegalArgumentException();
        }
        image = SeamRemover.removeHorizontalSeam(image, seam);

    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }
        if (!checkIllegalSeam(seam)) {
            throw new IllegalArgumentException();
        }
        image = SeamRemover.removeVerticalSeam(image, seam);

    }

    private boolean checkIllegalSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (abs(seam[i] - seam[i + 1]) > 1) {
                return true;
            }
        }
        return false;
    }

    private int square(int x) {
        return x * x;
    }




    public static void main(String[] args) {
        Picture p = new Picture("images/6x5.png");
        SeamCarver sc = new SeamCarver(p);

        int[] seam = sc.findHorizontalSeam();
        int[] expected = {2, 2, 1, 2, 1, 2};
        System.out.println(Arrays.toString(seam));
    }
}
