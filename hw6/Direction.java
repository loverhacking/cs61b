
public class Direction {

    private final int x;
    private final int y;

    /** whether the node is in backtracking stage */
    private final boolean isBacktrack;

    /** record the current searched string */
    private final String value;

    /** record the location node in Trie */
    private final Trie.TrieNode node;

    public Direction(int x, int y,
                     boolean backtrack, String value, Trie.TrieNode node) {
        this.x = x;
        this.y = y;
        this.isBacktrack = backtrack;
        this.value = value;
        this.node = node;
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

    public Trie.TrieNode getNode() {
        return this.node;
    }

}
