package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private WeightedQuickUnionUF ufWithVirtualBottomSite;
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

    private int xyTo1D(int r, int c) {
        return r * N + c;
    }

    private void connectSite(int row, int col) {

        if (row == 0) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), N * N);
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), N * N);


        }

        if (row == N - 1) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), N * N + 1);


        }

        if (row - 1 >= 0 && !isBlocked(row - 1, col)) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));



        }

        if (row + 1 <= N - 1 && !isBlocked(row + 1, col)) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));



        }

        if (col - 1 >= 0 && !isBlocked(row, col - 1)) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));



        }

        if (col + 1 <= N - 1 && !isBlocked(row, col + 1)) {
            ufWithVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            ufWithoutVirtualBottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));



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

    private boolean isBlocked(int row, int col) {
        return site[row][col] == 0;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }

        return site[row][col] == 1 || site[row][col] == 2;
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
        Percolation p = new Percolation(6);
        p.open(0, 5);
        System.out.println(p.isOpen(0, 5));

    }



    
}
