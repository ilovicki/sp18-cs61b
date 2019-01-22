/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // Implement LSD Sort
        int M = Integer.MIN_VALUE;
        int N = asciis.length;
        for (String s: asciis) {
            int len = s.length();
            M = M > len ? M : len;
        }
        String[] sorted = new String[N];
        System.arraycopy(asciis, 0, sorted, 0, N);
        for (int i = M - 1; i >= 0; i -= 1) {
            sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int N = asciis.length;
        int[] ithChar = new int[N];
        for (int j = 0; j < N; j += 1) {
            String s = asciis[j];
            int sLen = s.length();
            if (sLen <= index) {
                ithChar[j] = 0;
            } else {
                ithChar[j] = (int) s.charAt(index);
            }
        }

        int[] counts = new int[256];
        for (int i : ithChar) {
            counts[i]++;
        }

        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < 256; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[N];
        for (int i = 0; i < N; i += 1) {
            int item = ithChar[i];
            int place = starts[item];
            sorted[place] = asciis[i];
            starts[item] += 1;
        }
        System.arraycopy(sorted, 0, asciis, 0, N);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
