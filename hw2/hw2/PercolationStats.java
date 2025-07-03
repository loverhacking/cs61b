package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        monteCarloSimulation(pf, N, T);
    }

    private void monteCarloSimulation(PercolationFactory pf, int N, int T) {
        double[] percentiles = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);

            while (!p.percolates()) {
                opensite(p, N);
            }
            percentiles[i] = (double) p.numberOfOpenSites() / (N * N);
        }
        mean = StdStats.mean(percentiles);
        stddev = StdStats.stddev(percentiles);
        confidenceLo = mean - 1.96 * stddev / Math.sqrt(T);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(T);
    }

    private void opensite(Percolation p, int N) {
        int row = StdRandom.uniform(N);
        int col = StdRandom.uniform(N);

        p.open(row, col);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return confidenceHi;
    }

}
