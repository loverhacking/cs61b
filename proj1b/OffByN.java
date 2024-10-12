
public class OffByN implements CharacterComparator {

    private int charDistance;

    /** construct OffByN */
    OffByN(int N) {
        charDistance = N;
    }

    /** Returns true if characters are different by exactly N. */
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == charDistance;
    }


}
