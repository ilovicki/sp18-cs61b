import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {

    @Test
    public void randomTest() {
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        String message = "\n";

        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.25) {
                ads1.addLast(i);
                sad1.addLast(i);
                message += "addLast(" + i + ")\n";
            } else if (numberBetweenZeroAndOne >= 0.25 && numberBetweenZeroAndOne < 0.5) {
                ads1.addFirst(i);
                sad1.addFirst(i);
                message += "addFirst(" + i + ")\n";
            } else {
                if(ads1.isEmpty() || sad1.isEmpty()) {
                    continue;
                }
                if (numberBetweenZeroAndOne >= 0.5 && numberBetweenZeroAndOne < 0.75) {
                    Integer l1 = ads1.removeLast();
                    Integer l2 = sad1.removeLast();
                    message += "removeLast()\n";
                    assertEquals(message, l1, l2);
                } else {
                    Integer f1 = ads1.removeFirst();
                    Integer f2 = sad1.removeFirst();
                    message += "removeFirst()\n";
                    assertEquals(message, f1, f2);

                }
            }
        }
    }
}
