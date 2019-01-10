package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

import java.util.HashSet;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private Maze maze;
    private HashSet<Integer> open = new HashSet<>();

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        open.add(s);
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int x = maze.toX(v);
        int y = maze.toY(v);
        int tx = maze.toX(t);
        int ty = maze.toY(t);
        return Math.abs(x - tx) + Math.abs(y - ty);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {

        /* You do not have to use this method. */
        int minV = -1;
        for (int w: open) {
            minV = w;
            break;
        }
        for (int v: open) {
            if (distTo[v] + h(v) < distTo[minV] + h(minV)) {
                minV = v;
            }
        }
        return minV;
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        while (!open.isEmpty()) {
            int current = findMinimumUnmarked();
            open.remove(current);
            marked[current] = true;
            announce();
            if (current == t) {
                return;
            }
            for (int w: maze.adj(current)) {
                if(!marked[w] && !open.contains(w)) {
                    distTo[w] = distTo[current] + 1;
                    edgeTo[w] = current;
                    open.add(w);
                }
            }
        }

    }

    @Override
    public void solve() {
        astar(s);
    }

}

