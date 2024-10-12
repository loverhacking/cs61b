import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static CharacterComparator offByOne = new OffByN(5);

    @Test
    public void testEqualChars() {
        assertTrue(offByOne.equalChars('a', 'f'));
        assertTrue(offByOne.equalChars('f', 'a'));
        assertFalse(offByOne.equalChars('f', 'g'));
    }
}
