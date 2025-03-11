
public class Direction {

    private final int x;
    private final int y;
    private boolean isBacktrack;  // 标记是否为回溯阶段
    private String value = null;


    public Direction(int x, int y,
                     boolean backtrack, String value) {
        this.x = x;
        this.y = y;
        this.isBacktrack = backtrack;
        this.value = value;
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

    public boolean isBacktrack() {
        return this.isBacktrack;
    }

}
