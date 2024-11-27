package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF ufWithVirtualBottomSite;
    private final WeightedQuickUnionUF ufWithoutVirtualBottomSite;

    private int[][] site;
    private int N;
    private int openSite;
    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        ufWithVirtualBottomSite = new WeightedQuickUnionUF(N * N + 2);
        ufWithoutVirtualBottomSite = new WeightedQuickUnionUF(N * N + 1);
        site = new int[N][N];
        openSite = 0;
        this.N = N;
    }

    private static int xyTo1D(int r, int c) {
        return (r + 1) * (c + 1) - 1;
    }

    private void connectSite(int row, int col) {

        if (row == 0) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), N * N + 1);
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), N * N + 1);
            return;
        }

        if (row == N - 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), N * N + 2);
            return;
        }

        if (row - 1 >= 0 && site[row - 1][col] == 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            return;
        }

        if (row + 1 <= N - 1 && site[row + 1][col] == 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            return;
        }

        if (col - 1 >= 0 && site[row][col - 1] == 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            return;
        }

        if (col + 1 <= N - 1 && site[row][col + 1] == 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            return;
        }

    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        if (site[row][col] == 0) {
            site[row][col] = 1;
            connectSite(row, col);
            openSite++;
            if (isFull(row, col)) {
                site[row][col] = 2;
            }
        }


    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        return site[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        return ufWithoutVirtualBottomSite.connected(xyTo1D(row, col), N * N + 1);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufWithVirtualBottomSite.connected(N * N + 1, N * N + 2);
    }

    // use for unit testing (not required)
    //public static void main(String[] args)





}
