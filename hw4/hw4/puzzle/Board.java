package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    /** Returns the string representation of the board. 
      * Uncomment this method. */

    private int N;
    private int[][] board;
    public Board(int[][] tiles) {
        N = tiles.length;
        board = new int[N][N];

        /* copy array from tiles to board */
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = tiles[i][j];
            }
        }


    }
    public int tileAt(int i, int j) {

        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new IndexOutOfBoundsException();
        }
        return board[i][j];
    }
    public int size() {
        return N;
    }

    /** https://joshh.ug/neighbors.html */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;

    }

    /**
     *  Note that we do not count the blank square
     *  when computing the Hamming or Manhattan estimates !!!
     *  Both estimates will always be less or than equal to the true distance.
     *  For the Hamming estimate, this is true because
     *  each tile that is out of place must move at least once to reach its goal position.
     *  For the Manhattan estimate, this is true because
     *  each tile must move its Manhattan distance from its goal position.
     *
     */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) == 0) {
                    continue;
                }
                if (tileAt(i, j) != (i * N + j + 1)) {
                    count++;
                }
            }
        }
        return count;
    }
    public int manhattan() {
        int igoal, jgoal;
        int num;
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                num = tileAt(i, j);
                if (num == 0) {
                    continue;
                }
                if (num != (i * N + j + 1)) {
                    igoal = (num - 1) / N;
                    jgoal = num - igoal * N - 1;
                    count = count + Math.abs(i - igoal) + Math.abs(j - jgoal);
                }
            }
        }
        return count;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        if (this.N != ((Board) y).N) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != ((Board) y).board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    /* Returns the string representation of the board. */
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }




}
