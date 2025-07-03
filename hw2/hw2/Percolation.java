package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    /**
     * create 2 WeightedQuickUnionUF,
     * both have VirtualTopSite,
     * one with VirtualBottomSite, the other without.
     * isFull check for a connection to top site using
     * ufWithoutVirtualBottomSite to avoid backwash.
     * percolates check for connection between top and bottom sites
     * using ufWithVirtualBottomSite.
     */

    private final WeightedQuickUnionUF ufWithVirtualBottomSite;
    private final WeightedQuickUnionUF ufWithoutVirtualBottomSite;

    private final boolean[][] flagOpen;
    private final int N;
    private int openSite;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        ufWithVirtualBottomSite = new WeightedQuickUnionUF(N * N + 2);
        ufWithoutVirtualBottomSite = new WeightedQuickUnionUF(N * N + 1);

        flagOpen = new boolean[N][N];
        openSite = 0;
        this.N = N;
    }

    private int xyTo1D(int r, int c) {
        return r * N + c;
    }

    private void connectSite(int row, int col, int newrow, int newcol) {

        if (newrow < 0 || newrow > N - 1 || newcol < 0 || newcol > N - 1) {
            return;
        }

        if (isOpen(newrow, newcol)) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(newrow, newcol));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(newrow, newcol));
        }

    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        if (!isOpen(row, col)) {
            flagOpen[row][col] = true;
            connectVirtualSite(row, col);
            connectSite(row, col, row + 1, col);
            connectSite(row, col, row - 1, col);
            connectSite(row, col, row, col - 1);
            connectSite(row, col, row, col + 1);
            openSite++;
        }


    }

    private void connectVirtualSite(int row, int col) {
        if (row == 0) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), N * N);
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), N * N);
        }

        if (row == N - 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), N * N + 1);
        }
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        return flagOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        return ufWithoutVirtualBottomSite.connected(xyTo1D(row, col), N * N);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufWithVirtualBottomSite.connected(N * N, N * N + 1);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }



    
}
