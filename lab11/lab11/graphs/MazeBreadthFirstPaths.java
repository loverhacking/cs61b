package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;
    private Queue<Integer> queue;


    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        queue = new LinkedList<Integer>();
        queue.add(s);



    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {

        marked[s] = true;
        announce();

        while (!queue.isEmpty()) {
            int v = queue.poll();
            if (v == t) {
                return;
            }
            announce();
            for (int w: maze.adj(v)) {
                if (!marked[w]) {
                    queue.add(w);
                    marked[w] = true;

                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
            }
        }

    }


    @Override
    public void solve() {
        bfs();
    }
}

