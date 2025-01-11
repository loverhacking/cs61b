package hw4.puzzle;


import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    private int numMoves;
    private SearchNode targetSearchNode;
    private WorldState s;
    private MinPQ<SearchNode> BMS;
    private HashMap<WorldState, Integer> h;

    private LinkedList<WorldState> list;

    public Solver(WorldState initial) {
        s = initial;
        numMoves = 0;
        targetSearchNode = null;
        BMS = new MinPQ<>();
        BMS.insert(new SearchNode(0, s, null));
        list = new LinkedList<>();
        h = new HashMap<>();
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


    public int moves() {
        return numMoves;
    }

    public Iterable<WorldState> solution() {
        return list;
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
                    BMS.insert(new SearchNode(n.moves + 1, ws, n));
                }
            }
        }

    }

    private void getSolution() {
        solve();
        SearchNode n = targetSearchNode;
        while (n != null) {
            list.addFirst(n.w);
            n = n.prev;
        }
    }
}
