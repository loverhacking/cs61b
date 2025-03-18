package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public boolean isTile(TETile[][] world, TETile t) {
        return world[x][y].equals(t);
    }

    public void drawTile(TETile[][] world, TETile t) {
        world[x][y] = t;
    }


}
