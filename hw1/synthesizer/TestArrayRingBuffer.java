package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        assertEquals(1, (int) arb.dequeue());
        assertTrue(arb.fillCount() == 2);
        assertEquals(2, (int) arb.peek());
        assertTrue(arb.fillCount() == 2);
        Iterator<Integer> iter = arb.iterator();
        assertTrue(iter.hasNext());
        assertEquals(2, (int) iter.next());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
