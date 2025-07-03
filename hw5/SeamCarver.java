import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class SeamCarver {

    private final Picture image;
    private final int width;
    private final int height;

    /**
     * store energy and color for every pixel
     * notice that it's organized by height * width (i.e. row * col)
     * which is different from Color object
     */
    private Color[][] color;
    private double[][] energyPixel;

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
        initColor(picture);
        initEnergy();
    }

    private void initColor(Picture picture) {
        color = new Color[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                color[y][x] = picture.get(x, y);
            }
        }
    }

    private void initEnergy() {
        energyPixel = new double[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energyPixel[y][x] = energy(x, y);
            }
        }
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

        int deltaX = calculateSquareGradient(color[y][leftX], color[y][rightX]);
        int deltaY = calculateSquareGradient(color[upY][x], color[belowY][x]);
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

        // energy cost of pixel at location (i, j)
        M = new double[height][width];
        int[] verticalSeam = new int[height];

        double min = Double.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                M[i][j] = energyPixel[i][j] + minEnergyCost(i, j);

                // find min value in last row in M[][]
                if (i == height - 1 && M[i][j] < min) {
                    min = M[i][j];
                    minIndex = j;
                }
            }
        }

        verticalSeam[height - 1] = minIndex;

        int j = minIndex;
        for (int i = height - 1; i >= 1; i--) {
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

        /** minimum cost path ending at (row i, col j) */
        double[][] minCost = new double[height][width];

        int[] horizontalSeam = new int[width];

        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                minCost[i][j] = energyPixel[i][j] + minHorizontalEnergyCost(i, j, minCost);
            }
        }

        // find min value in the last column in minCost[][]
        double min = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int i = 0; i < height; i++) {
            if (minCost[i][width - 1] < min) {
                min = minCost[i][width - 1];
                minIndex = i;
            }
        }
        horizontalSeam[width - 1] = minIndex;

        // trace back from the last column to form path
        int i = minIndex;
        for (int j = width - 1; j >= 1; j--) {
            i = minHorizontalCost(i, j, minCost);
            horizontalSeam[j - 1] = i;
        }
        return horizontalSeam;
    }

    /** find pathTo[i][j] given pixel (row i, col j) */
    private int minHorizontalCost(int i, int j, double[][] minCost) {

        // special case: height == 1: no need to compare
        if (height == 1) {
            return i;
        }

        if (i == 0) {
            return minCost[i][j - 1] > minCost[i + 1][j - 1] ? i + 1 : i;
        }
        if (i == height - 1) {
            return minCost[i - 1][j - 1] > minCost[i][j - 1] ? i : i - 1;
        }

        int tempMin = minCost[i - 1][j - 1] > minCost[i][j - 1] ? i : i - 1;
        return minCost[tempMin][j - 1] > minCost[i + 1][j - 1] ? i + 1 : tempMin;
    }

    private double minHorizontalEnergyCost(int i, int j, double[][] minCost) {
        if (j == 0) {
            return 0;
        }

        // special case: height == 1: no need to compare
        if (height == 1) {
            return minCost[i][j - 1];
        }

        if (i == 0) {
            return Math.min(minCost[i][j - 1], minCost[i + 1][j - 1]);
        }
        if (i == height - 1) {
            return Math.min(minCost[i][j - 1], minCost[i - 1][j - 1]);
        }
        return Math.min(minCost[i - 1][j - 1], Math.min(minCost[i][j - 1], minCost[i + 1][j - 1]));
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
