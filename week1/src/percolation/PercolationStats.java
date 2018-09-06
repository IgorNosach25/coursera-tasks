package percolation;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double s = 1.96;
    private final int n;
    private final int trials;
    private final double mean;
    private final double[] percThreshold;
    private final double stddev;
    private final double confidenceHi;
    private final double confidenceLo;


    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.trials = trials;
        percThreshold = new double[trials];
        performTrials();
        this.mean = StdStats.mean(percThreshold);
        this.stddev = StdStats.stddev(percThreshold);
        this.confidenceLo = mean - ((s * stddev) / n);
        this.confidenceHi = mean + ((s * stddev) / n);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    private void performTrials() {
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomColumn = StdRandom.uniform(1, n + 1);
                percolation.open(randomRow, randomColumn);
            }
            percThreshold[i] = 1.0 * percolation.numberOfOpenSites() / (1.0 * (n * n));
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo
                + ", " + percolationStats.confidenceHi + "]");
    }
}
