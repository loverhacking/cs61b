package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private boolean isMazeCycles;
    /* a copy of edgeTo */
    private int[] nodeTo;



    public MazeCycles(Maze m) {

        super(m);
        maze = m;
        nodeTo = new int[maze.V()];
        isMazeCycles = false;

    }

    @Override
    public void solve() {
        dfs(0);
    }


    // Helper methods go here
    private void dfs(int v) {
        marked[v] = true;
        announce();

        for (int w: maze.adj(v)) {

            /**
             * For every visited vertex v, if there is an adjacent u
             * such that u is already visited and u is not parent of v
             * , then there is a cycle in graph.
             */

            if (marked[w] && nodeTo[v] != w) {
                isMazeCycles = true;
                edgeTo[w] = v;
                announce(); // draw straight line v -> w

                drawcircle(w, v); // draw another line v -> w
                return;
            }
            if (!marked[w]) {
                nodeTo[w] = v;
                dfs(w);
            }
            if (isMazeCycles) {
                return;
            }
        }

    }

    private void drawcircle(int w, int v) {
        int x;
        for (x = v; x != w; x = nodeTo[x]) {
            edgeTo[x] = nodeTo[x];
            announce();
        }
    }

}

