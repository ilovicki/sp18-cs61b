package hw2;

import  edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int T;
    private double[] thresholds;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        thresholds = new double[T];
        for (int i = 0; i < T; i += 1) {
            Percolation p =  pf.make(N);
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                p.open(row, col);
            }
            thresholds[i] = p.numberOfOpenSites() / (double) (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        if (T == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(thresholds);
    }

    public double confidenceLow() {
        if (T == 1) {
            return Double.NaN;
        }
        return mean() - 1.96 * stddev() /Math.sqrt(T);
    }

    public double confidenceHigh() {
        if (T == 1) {
            return Double.NaN;
        }
        return mean() + 1.96 * stddev() /Math.sqrt(T);
    }
}
