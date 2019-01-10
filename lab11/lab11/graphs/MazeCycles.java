package lab11.graphs;
import edu.princeton.cs.algs4.Stack;
/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int c;
    private int N;
    private boolean cycleFound = false;
    private int[] preOf;
    private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        N = marked.length;
        s = maze.xyTo1D(3,  3);
        distTo[s] = 0;
        preOf = new int[N];
    }

    @Override
    public void solve() {
        //  Your code here!
        dfs(s);

    }
    private void dfs(int v) {
        if (cycleFound) {
            return;
        }
        marked[v] = true;
        announce();
        for (int w: maze.adj(v)) {
            if (marked[w] && preOf[v] != w) {
                preOf[w] = v;
                c = w;
                cycleFound = true;
                showCycle();
                break;
            } else if (!marked[w]) {
                preOf[w] = v;
                distTo[w] = distTo[v] + 1;
                dfs(w);
            }
        }
    }
    private void showCycle() {
        int cur = preOf[c];
        while (cur != c) {
            edgeTo[cur] = preOf[cur];
            announce();
            cur = preOf[cur];
        }
        edgeTo[cur] = preOf[cur];
        announce();
    }



}

