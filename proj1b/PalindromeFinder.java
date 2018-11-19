/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {

    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();
        int N = 6;

        CharacterComparator cc = new OffByN(N);
        int sum = 0;

        while (!in.isEmpty()) {
            String word = in.readString();

            if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
                System.out.print(word + " ");
                sum += 1;
            }
        }
        System.out.println();
        System.out.println("There are " + sum + " OffBy" + N + " palindromes in the file.");
    }
}
