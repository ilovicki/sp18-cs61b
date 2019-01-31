import org.junit.Test;
import static org.junit.Assert.*;
public class TestFlik {

    @Test
    public void testFlik(){
        int a0 = 127;
        int b0 = 127;
        int a1 = 128;
        int b1 = 128;
        int a2 = 129;
        int b2 = 129;
        assertTrue(Flik.isSameNumber(a0, b0));
        assertTrue(Flik.isSameNumber(a1, b1));
        assertTrue(Flik.isSameNumber(a2, b2));

    }
}
