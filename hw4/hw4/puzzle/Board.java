package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;
public class Board implements WorldState{
    private int N;
    private int[][] tiles;
    public Board(int[][] t) {
        N = t.length;
        tiles = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                tiles[i][j] = t[i][j];
            }
        }

    }
    public int tileAt(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }
    public int size() {
        return N;
    }
    public int[][] getTiles() {
        return tiles;
    }

    private int[][] neighbor(int r0, int c0, int r1, int c1) {
        int[][] nbTiles = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
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
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if(tileAt(i, j) == 0) {
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
        if (blankRow + 1 < N) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow + 1, blankCol));
            neighbors.enqueue(bd);
        }
        if (blankCol - 1 >= 0) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow, blankCol - 1));
            neighbors.enqueue(bd);
        }
        if (blankCol + 1 < N) {
            Board bd = new Board(neighbor(blankRow, blankCol, blankRow, blankCol + 1));
            neighbors.enqueue(bd);
        }
        return neighbors;

    }
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (tileAt(i, j) != ((i * N + j + 1) % (N * N))) {
                    sum += 1;
                }
            }
        }
        return sum;
    }
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                int num = tileAt(i, j);
                int numAtRow;
                int numAtCol;
                if (num == 0) {
                    numAtRow = N - 1;
                    numAtCol = N - 1;
                } else {
                    numAtRow = (num - 1) / N;
                    numAtCol = num - 1 - numAtRow * N;
                }
                int manh = Math.abs(i - numAtRow) + Math.abs(j - numAtCol);
                sum += manh;
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
        return getTiles().equals(board.getTiles());


    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
