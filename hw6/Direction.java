
public class Direction {

    private final int x;
    private final int y;
    private int visited;
    private String value = null;


    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getValue() {
        return this.value;
    }


    public int getVisited() {
        return this.visited;
    }


    public void setVisited(int visited) {
        this.visited = visited;
    }

    public void setValue(String newValue) {
        value = newValue;
    }

}
