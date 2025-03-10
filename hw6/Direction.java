
public class Direction {

    private final int x;
    private final int y;
    private boolean isBacktrack;  // 标记是否为回溯阶段
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

    public void setValue(String newValue) {
        value = newValue;
    }

    public boolean isBacktrack() {
        return this.isBacktrack;
    }

    public void setBacktrack(boolean isBacktrack) {
        this.isBacktrack = isBacktrack;
    }

}
