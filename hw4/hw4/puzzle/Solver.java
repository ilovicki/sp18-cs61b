package hw4.puzzle;

import  edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;

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
    private ArrayDeque<WorldState> solution = new ArrayDeque<>();
    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode start = new SearchNode(initial, 0, null);
        pq.insert(start);
        while (!pq.isEmpty()) {
            SearchNode X = pq.delMin();
            if (X.curState().isGoal()) {
                goalNode = X;
                break;
            }
            for (WorldState nb : X.curState().neighbors()) {
                if (X.preNode() == null || !nb.equals(X.preNode().curState())) {
                    SearchNode sn = new SearchNode(nb, X.moves() + 1, X);
                    pq.insert(sn);
                }
            }
        }
        SearchNode cur = goalNode;
        while (cur != null) {
            solution.addFirst(cur.curState());
            cur = cur.preNode();
        }
    }
    public int moves() {
        return goalNode.moves();
    }
    public Iterable<WorldState> solution() {
        return solution;
    }
}
