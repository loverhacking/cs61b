package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private Maze maze;
    private PriorityQueue<Node> pq;

    private class Node {
        private int priority;
        private int node;

        private Node(int v) {
            node = v;
            priority = distTo[v] + h(v);
        }
    }

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.priority - o2.priority;
        }
    }


    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        pq = new PriorityQueue<Node>(new NodeComparator());


    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);

        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {

        marked[s] = true;
        announce();

        pq.offer(new Node(s));
        if (s == t) {
            return;
        }

        while (!pq.isEmpty()) {
            Node v = pq.poll();
            for (int w: maze.adj(v.node)) {
                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[v.node] + 1;
                    edgeTo[w] = v.node;
                    announce();
                    if (w == t) {
                        return;
                    } else {
                        pq.offer(new Node(w));
                    }
                }


            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

