import org.junit.Test;
import static org.junit.Assert.*;


public class TestOffByN {

    @Test
    public void testEqualChars() {
        OffByN offByN0 = new OffByN(4);
        assertTrue(offByN0.equalChars('a', 'e'));
        assertTrue(offByN0.equalChars('e', 'a'));
        assertFalse(offByN0.equalChars('b', 'd'));
        assertFalse(offByN0.equalChars('d', 'a'));
        assertFalse(offByN0.equalChars('&', '%'));

        OffByN offByN1 = new OffByN(5);
        assertTrue(offByN1.equalChars('a', 'f'));
        assertTrue(offByN1.equalChars('f', 'a'));
        assertFalse(offByN1.equalChars('b', 'd'));
        assertFalse(offByN1.equalChars('d', 'a'));
        assertFalse(offByN1.equalChars('&', '%'));


    }
}
