package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;
public class Board implements WorldState {
    private int n;
    private int[][] tiles;
    public Board(int[][] t) {
        n = t.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                tiles[i][j] = t[i][j];
            }
        }

    }
    public int tileAt(int i, int j) {
        if (i < 0 || i > n - 1 || j < 0 || j > n - 1) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }
    public int size() {
        return n;
    }

    private int[][] neighbor(int r0, int c0, int r1, int c1) {
        int[][] nbTiles = new int[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                nbTiles[i][j] = tileAt(i, j);
            }
        }
        int temp = nbTiles[r1][c1];
        nbTiles[r1][c1] = 0;
        nbTiles[r0][c0] = temp;
        return nbTiles;
    }
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int blankRow = -1;
        int blankCol = -1;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                if (tileAt(i, j) == 0) {
                    blankRow = i;
                    blankCol = j;
                    break;
                }
            }
        }
        if (blankRow - 1 >= 0) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow - 1, blankCol));
            neighbors.enqueue(bd);
        }
        if (blankRow + 1 < n) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow + 1, blankCol));
            neighbors.enqueue(bd);
        }
        if (blankCol - 1 >= 0) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow, blankCol - 1));
            neighbors.enqueue(bd);
        }
        if (blankCol + 1 < n) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow, blankCol + 1));
            neighbors.enqueue(bd);
        }
        return neighbors;

    }
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                int num = tileAt(i, j);
                if (num != 0 && num != ((i * n + j + 1) % (n * n))) {
                    sum += 1;
                }
            }
        }
        return sum;
    }
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                int num = tileAt(i, j);
                if (num != 0) {
                    int numAtRow = (num - 1) / n;
                    int numAtCol = num - 1 - numAtRow * n;
                    int manh = Math.abs(i - numAtRow) + Math.abs(j - numAtCol);
                    sum += manh;
                }
            }
        }
        return sum;
    }
    public int estimatedDistanceToGoal() {
        return hamming();
    }
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board board = (Board) y;
        if (size() != board.size()) {
            return false;
        }
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                if (tileAt(i, j) != board.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int s = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                s *= 10;
                s += tileAt(i, j);
            }
        }
        return s;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
