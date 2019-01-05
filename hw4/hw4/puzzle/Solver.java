package hw4.puzzle;

import  edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private WorldState current;
        private int moves;
        private SearchNode previous;
        private int distanceToGoal;
        public SearchNode(WorldState c, int m, SearchNode p) {
            current = c;
            moves = m;
            previous = p;
            distanceToGoal = c.estimatedDistanceToGoal();
        }
        public int moves() {
            return moves;
        }
        public WorldState curState() {
            return current;
        }
        public SearchNode preNode() {
            return previous;
        }
        public int distanceToGoal() {
            return distanceToGoal;
        }

        @Override
        public int compareTo(SearchNode a) {
            return moves() + distanceToGoal()
                    - (a.moves() + a.distanceToGoal());
        }
    }
    private SearchNode goalNode;
    private int numEnqueued;
    private Queue<WorldState> solution = new Queue<>();
    public Solver(WorldState initial) {
        numEnqueued = 0;
        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode start = new SearchNode(initial, 0, null);
        pq.insert(start);
        numEnqueued += 1;
        while (!pq.isEmpty()) {
            SearchNode X = pq.delMin();
            solution.enqueue(X.curState());
            if (X.curState().isGoal()) {
                goalNode = X;
                return;
            }
            for (WorldState nb : X.curState().neighbors()) {
                if (X.preNode() != null && nb.equals(X.preNode().curState())) {
                    continue;
                }
                SearchNode sn = new SearchNode(nb, X.moves() + 1, X);
                pq.insert(sn);
                numEnqueued += 1;

            }
        }
    }
    private boolean visited(Queue<WorldState> q, WorldState ws) {
        for (WorldState s: q) {
            if (s.equals(ws)) {
                return true;
            }
        }
        return false;
    }
    int getNumEnqueued() {
        return numEnqueued;
    }
    public int moves() {
        return goalNode.moves();
    }
    public Iterable<WorldState> solution() {
        return solution;
    }
}
