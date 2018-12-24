package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private boolean[] open;
    private int numOfOpenSites;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufCmp;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive.");
        }
        this.N = N;
        open = new boolean[N * N];
        for (int r = 0; r < N; r += 1) {
            for (int c = 0; c < N; c += 1) {
                open[r * N + c] = false;
            }
        }
        numOfOpenSites = 0;
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufCmp = new WeightedQuickUnionUF(N * N + 1);
        for (int i = 0; i < N; i += 1) {
            uf.union(i, N * N);
            ufCmp.union(i, N * N);
        }
        for (int j = (N - 1) * N; j < N * N; j += 1) {
            uf.union(j, N * N + 1);
        }
    }
    private void validate(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
    }
    public void open(int row, int col) {
        validate(row, col);
        int index = row * N + col;
        if (!open[index]) {
            open[index] = true;
            numOfOpenSites += 1;
            if (row > 0 && open[index - N]) {
                uf.union(index - N, index);
                ufCmp.union(index - N, index);
            }
            if (row < N - 1 && open[index + N]) {
                uf.union(index, index + N);
                ufCmp.union(index, index + N);
            }
            if (col > 0 && open[index - 1]) {
                uf.union(index - 1, index);
                ufCmp.union(index - 1, index);
            }
            if (col < N - 1 && open[index + 1]) {
                uf.union(index, index + 1);
                ufCmp.union(index, index + 1);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[row * N + col];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        int index = row * N + col;
        return open[index] && ufCmp.connected(index, N * N);

    }

    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    public boolean percolates() {
        if (N == 1) {
            return open[0] && uf.connected(N * N, N * N + 1);
        }
        return uf.connected(N * N, N * N + 1);
    }

    private void print(int row, int col) {
        System.out.println("Is the site (" + row + ", " + col + ")open? " + isOpen(row, col));
        System.out.println("Is the site (" + row + ", " + col + ")full? " + isFull(row, col));
        System.out.println("Does the system percolate? " + percolates());
        System.out.println("The number of open sites is: " + numberOfOpenSites());
        System.out.println();
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        int r = 2;
        int c = 0;
        p.open(r, c);
        p.print(r, c);
        r = 1;
        p.open(r, c);
        p.print(r, c);
        r = 0;
        p.open(r, c);
        p.print(r, c);
        r = 2;
        c = 2;
        p.open(r, c);
        p.print(r, c);
    }
}
