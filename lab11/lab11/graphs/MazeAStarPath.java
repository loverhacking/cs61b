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

    /**
     * create Object Node to make vertex number comparable
     * in PriorityQueue
     */
    private class Node {
        private int priority;
        private int v;

        private Node(int v) {
            this.v = v;
            /* A* consider both d[v] and h[v] */
            this.priority = distTo[v] + h(v);
        }
    }

    /**
     * write NodeComparator for class Node in PriorityQueue
     */
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
    private void astar(int src) {

        marked[src] = true;
        announce();

        pq.offer(new Node(src));

        /* like BFS but use PQ instead of Queue to maintain vertexes to be visited */
        while (!pq.isEmpty()) {
            Node v = pq.poll();
            int vertex = v.v;
            if (vertex == t) {
                return;
            }
            for (int w: maze.adj(vertex)) {
                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[vertex] + 1;
                    edgeTo[w] = vertex;
                    announce();
                    pq.offer(new Node(w));
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

