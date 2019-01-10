import edu.princeton.cs.algs4.Queue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> out = new Queue<>();
        while (!items.isEmpty()) {
            Item cur = items.dequeue();
            Queue<Item> curQ = new Queue<>();
            curQ.enqueue(cur);
            out.enqueue(curQ);
        }
        return out;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> out = new Queue<>();
        while (!(q1.isEmpty() && q2.isEmpty())) {
            Item min = getMin(q1, q2);
            out.enqueue(min);
        }
        return out;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
//        int n = items.size();
//        if (n == 0 || n == 1) {
//            return items;
//        }
//        Queue<Item> left = new Queue<>();
//        Queue<Item> right = new Queue<>();
//        for (int i = 0; i < n / 2; i += 1) {
//            left.enqueue(items.dequeue());
//        }
//        while (!items.isEmpty()) {
//            right.enqueue(items.dequeue());
//        }
//        return mergeSortedQueues(mergeSort(left), mergeSort(right));
        Queue<Queue<Item>> origin = makeSingleItemQueues(items);
        int n = origin.size();
        while (n != 1) {
            Queue<Queue<Item>> temp = new Queue<>();
            for (int i = 0; i < n / 2; i += 1) {
                temp.enqueue(mergeSortedQueues(origin.dequeue(), origin.dequeue()));
            }
            if (n % 2 == 1) {
                temp.enqueue(origin.dequeue());
            }
            origin = temp;
            n = temp.size();
        }
        return origin.dequeue();
    }
    public static void main(String[] args) {
        Queue<String> students = new Queue<String>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        System.out.print("Queue before sorting: ");
        for (String s: students) {
            System.out.print(s + " ");
        }
        System.out.println();
        Queue<String> actual = mergeSort(students);
        System.out.print("Queue after sorting: ");
        for (String a: actual) {
            System.out.print(a + " ");
        }
        System.out.println();

    }
}
