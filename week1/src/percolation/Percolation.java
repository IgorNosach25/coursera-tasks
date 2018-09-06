package percolation;/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final int topPoint;
    private final int bottomPoint;
    private final boolean[] statusSite;
    private int openSites;

    private final WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("n-grid length must be more than 0");
        }
        n = length;
        statusSite = new boolean[n * n + 1];
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        topPoint = 0;
        bottomPoint = n * n + 1;
        initStatusSitesByBlocking();
    }

    public boolean isOpen(int row, int column) {
        validBoundsOfGrid(row, column);
        int position = convertRowAndColToIndexPosition(row, column);
        return statusSite[position];
    }

    public void open(int row, int column) {
        validBoundsOfGrid(row, column);
        if (!isOpen(row, column)) {
            int position = convertRowAndColToIndexPosition(row, column);
            statusSite[position] = true;
            openSites++;
            connectToNeighboringSites(row, column);
        }
    }

    public boolean isFull(int row, int col) {
        validBoundsOfGrid(row, col);
        int indexPosition = convertRowAndColToIndexPosition(row, col);
        return isOpen(row, col)
                && weightedQuickUnionUF.connected(indexPosition, topPoint);
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(topPoint, bottomPoint);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    private void initStatusSitesByBlocking() {
        for (int i = 1; i < statusSite.length; i++) {
            if (i <= n) {
                weightedQuickUnionUF
                        .union(topPoint, i);
            }
            if (i >= statusSite.length - n) {
                weightedQuickUnionUF
                        .union(bottomPoint, i);
            }
        }
    }

    private void connectToNeighboringSites(int row, int column) {
        int currentSite = convertRowAndColToIndexPosition(row, column);
        if (row - 1 > 0 && isOpen(row - 1, column)) {
            int aboveSite = convertRowAndColToIndexPosition(row - 1, column);
            weightedQuickUnionUF.union(currentSite, aboveSite);
        }
        if (row + 1 <= n && isOpen(row + 1, column)) {
            int bottomSite = convertRowAndColToIndexPosition(row + 1, column);
            weightedQuickUnionUF.union(currentSite, bottomSite);
        }
        if (column - 1 >= 1 && isOpen(row, column - 1)) {
            int leftSite = convertRowAndColToIndexPosition(row, column - 1);
            weightedQuickUnionUF.union(currentSite, leftSite);
        }
        if (column + 1 <= n && isOpen(row, column + 1)) {
            int rightSite = convertRowAndColToIndexPosition(row, column + 1);
            weightedQuickUnionUF.union(currentSite, rightSite);
        }
    }

    private void validBoundsOfGrid(int row, int column) {
        if (row < 1 || row > n || column < 1 || column > n) {
            throw new IllegalArgumentException("Bounds of grid are not valid.");
        }
    }

    private int convertRowAndColToIndexPosition(int row, int column) {
        return n * row - (n - column);
    }

}
