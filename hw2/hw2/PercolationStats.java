package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
public class PercolationStats {

    private int N;
    private int T;
    private double[] percentiles;
    private PercolationFactory pf;
    private Percolation p;
    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        this.T = T;
        this.pf = pf;
        this.percentiles = new double[T];
        monteCarloSimulation();
    }

    private void monteCarloSimulation() {
        for (int i = 0; i < T; i++) {
            this.p = pf.make(N);

            while (p.percolates()) {
                opensite();
            }

            percentiles[i] = (double) (p.numberOfOpenSites() / N);
        }

    }

    private void opensite() {
        int row = StdRandom.uniform(N);
        int col = StdRandom.uniform(N);

        while (!p.isOpen(row, col)) {
            row = StdRandom.uniform(N);
            col = StdRandom.uniform(N);
        }
        p.open(row, col);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percentiles);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percentiles);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

}
