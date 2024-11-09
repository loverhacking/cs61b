package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.Stack;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 1;
    public static final int HEIGHT = 1;

    private static Random random;
    Stack<TETile[][]> stack = new Stack<>();



    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {

        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] world = null;
        char input0 = input.charAt(0);
        switch (Character.toUpperCase(input0)) {
            case 'N':
                world = newGame(input);
                break;
            case 'L':
                world = loadGame();
                break;
            case 'Q':
                System.exit(0);
                break;
            default:
                break;
        }

        return world;
    }

    private TETile[][] loadGame() {
        if (stack.empty()) {
            System.exit(0);
            return null;
        } else {
            return stack.pop();
        }


    }

    private TETile[][] newGame(String input) {

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        int indexS = input.toLowerCase().indexOf('s');
        long seed = convertSeed(input.substring(1, indexS));

        initialMaze(world);
        createMaze(world, seed);
        savegame(world);
        return world;

    }

    private void savegame(TETile[][] world) {
        stack.push(world);
    }

    private long convertSeed(String seedString) {
        return Long.valueOf(seedString.toString());
    }

    private static void drawWall(TETile[][] world, int x1, int x2, int y1, int y2, int x, int y) {

        for (int i = x1; i <= x2; i++) {
            world[i][y] = Tileset.WALL;
        }
        for (int i = y1; i <= y2; i++) {
            world[x][i] = Tileset.WALL;
        }
    }
    private static int convertBooleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }
    private static boolean isConnectTwoArea(TETile[][] world, int rx, int ry) {

        int numIsWall = convertBooleanToInt(world[rx - 1][ry].equals(Tileset.WALL))
                +  convertBooleanToInt(world[rx + 1][ry].equals(Tileset.WALL))
                +  convertBooleanToInt(world[rx][ry - 1].equals(Tileset.WALL))
                +  convertBooleanToInt(world[rx][ry + 1].equals(Tileset.WALL));

        return numIsWall > 2;
    }

    private static void initialMaze(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }


    private static void createMazeInOneArea(TETile[][] world, int x1, int y1,
                                            int x2, int y2, int x, int y) {

        // randow pick 3 walls
        int[] r = {0, 0, 0, 0};
        r[Game.random.nextInt(4)] = 1;

        // make holes at random points on the wall
        for (int i = 0; i < 4; i++) {
            if (r[i] == 0) {
                int rx = x;
                int ry = y;

                switch (i) {
                    case 0:
                        do {
                            rx = x1 + Game.random.nextInt(x - x1);
                        } while (isConnectTwoArea(world, rx, ry));
                        break;
                    case 1:
                        do {
                            ry = y + 1 + Game.random.nextInt(y2 - y);
                        } while (isConnectTwoArea(world, rx, ry));
                        break;
                    case 2:
                        do {
                            rx = x + 1 + Game.random.nextInt(x2 - x);
                        } while (isConnectTwoArea(world, rx, ry));
                        break;
                    case 3:
                        do {
                            ry = y1 + Game.random.nextInt(y - y1);
                        } while (isConnectTwoArea(world, rx, ry));
                        break;
                    default:
                        break;
                }
                world[rx][ry] = Tileset.FLOOR;
            }


        }
    }

    private static void createWallBoundary(TETile[][] world, int x1, int x2, int y1, int y2) {
        for (int x = x1 - 1; x <= x2 + 1; x += 1) {
            world[x][y1 - 1] = Tileset.WALL;
            world[x][y2 + 1] = Tileset.WALL;
        }

        for (int y = y1 - 1; y <= y2 + 1; y += 1) {
            world[x1 - 1][y] = Tileset.WALL;
            world[x2 + 1][y] = Tileset.WALL;
        }
    }

    private static void createMaze(TETile[][] world, long seed) {

        if (WIDTH < 2 || HEIGHT < 2) {
            return;
        }
        Game.random = new Random(seed);
        int x1 = 1 + Game.random.nextInt(WIDTH - 2);
        int x2 = x1 + 1 + Game.random.nextInt(WIDTH - 2 - x1);
        int y1 = 1 + Game.random.nextInt(HEIGHT - 2);
        int y2 = y1 + 1 + Game.random.nextInt(HEIGHT - 2 - y1);

        createWallBoundary(world, x1, x2, y1, y2);

        createMaze(world, x1, y1, x2, y2);
        fillupMaze(world, x1, y1, x2, y2);
        createLockedDoor(world, x1, y1, x2, y2);
    }

    private static void createLockedDoor(TETile[][] world, int x1, int y1, int x2, int y2) {
        int flag = Game.random.nextInt(4);
        int num;
        switch (flag) {
            case 0:
                do {
                    num = x1 + Game.random.nextInt(x2 - x1 + 1);
                } while (world[num][y1].equals(Tileset.WALL));
                world[num][y1 - 1] = Tileset.LOCKED_DOOR;
                break;
            case 1:
                do {
                    num = x1 + Game.random.nextInt(x2 - x1 + 1);
                } while (world[num][y2].equals(Tileset.WALL));
                world[num][y2 + 1] = Tileset.LOCKED_DOOR;
                break;
            case 2:
                do {
                    num = y1 + Game.random.nextInt(y2 - y1 + 1);
                } while (world[x1][num].equals(Tileset.WALL));
                world[x1 - 1][num] = Tileset.LOCKED_DOOR;
                break;
            case 3:
                do {
                    num = y1 + Game.random.nextInt(y2 - y1 + 1);
                } while (world[x2][num].equals(Tileset.WALL));
                world[x2 + 1][num] = Tileset.LOCKED_DOOR;
                break;
            default:
        }

    }

    private static void fillupMaze(TETile[][] world, int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x += 1) {
            for (int y = y1; y <= y2; y += 1) {
                if (!world[x][y].equals(Tileset.WALL)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * based on recursive partition algorithm
     */
    public static void createMaze(TETile[][] world, int x1, int y1, int x2, int y2) {

        // base case
        if (x2 - x1 < 2 || y2 - y1 < 2) {
            return;
        }

        // random pick point
        int x = x1 + 1 + Game.random.nextInt(x2 - x1 - 1);
        int y = y1 + 1 + Game.random.nextInt(y2 - y1 - 1);

        // draw wall
        drawWall(world, x1, x2, y1, y2, x, y);

        // recursive partition to continue divide maze
        createMaze(world, x1, y1, x - 1, y - 1);
        createMaze(world, x + 1, y + 1, x2, y2);
        createMaze(world, x + 1, y1, x2, y - 1);
        createMaze(world, x1, y + 1, x - 1, y2);

        // create maze in one area
        createMazeInOneArea(world, x1, y1, x2, y2, x, y);

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playWithInputString("N234S");
    }

}
