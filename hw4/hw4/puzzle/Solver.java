package hw4.puzzle;


import edu.princeton.cs.algs4.MinPQ;

import java.util.HashMap;
import java.util.LinkedList;


public class Solver {
    private int numMoves;
    private SearchNode targetSearchNode;
    private WorldState s;
    private MinPQ<SearchNode> BMS;
    private HashMap<WorldState, Integer> h;
    private LinkedList<WorldState> solList;

    public Solver(WorldState initial) {

        s = initial;
        numMoves = 0;
        targetSearchNode = null;
        solList = new LinkedList<>();
        h = new HashMap<>();

        BMS = new MinPQ<>();
        BMS.insert(new SearchNode(0, s, null));

        getSolution();
    }

    private class SearchNode implements Comparable<SearchNode> {
        int moves;
        WorldState w;
        SearchNode prev;

        private SearchNode(int moves, WorldState w, SearchNode prev) {
            this.moves = moves;
            this.w = w;
            this.prev = prev;
        }

        /**
         * To avoid recomputing the estimatedDistanceToGoal() result from scratch 
         * each time during various priority queue operations, 
         * compute it at most once per object
         */
        @Override
        public int compareTo(SearchNode o) {
            int hthis, ho;
            if (h.containsKey(this.w)) {
                hthis = h.get(this.w);
            } else {
                hthis = this.w.estimatedDistanceToGoal();
                h.put(this.w, hthis);
            }
            if (h.containsKey(o.w)) {
                ho = h.get(o.w);
            } else {
                ho = o.w.estimatedDistanceToGoal();
                h.put(o.w, ho);
            }
            return this.moves + hthis - (o.moves + ho);
        }
    }


    /**
     * Returns the minimum number of moves
     * to solve the puzzle starting at the initial WorldState.
     */
    public int moves() {
        return numMoves;
    }

    /**
     * Returns a sequence of WorldStates
     * from the initial WorldState to the solution.
     */
    public Iterable<WorldState> solution() {
        return solList;
    }

    private void solve() {

        while (!BMS.isEmpty()) {
            SearchNode n = BMS.delMin();
            WorldState state = n.w;
            if (state.isGoal()) {
                numMoves = n.moves;
                targetSearchNode = n;
                return;
            }
            for (WorldState ws: state.neighbors()) {
                if (n.prev == null || !ws.equals(n.prev.w)) {
                    /* no enqueued WorldState is its own grandparent */
                    BMS.insert(new SearchNode(n.moves + 1, ws, n));
                }
            }
        }

    }

    private void getSolution() {
        solve();
        SearchNode n = targetSearchNode;
        while (n != null) {
            solList.addFirst(n.w);
            n = n.prev;
        }
    }
}
