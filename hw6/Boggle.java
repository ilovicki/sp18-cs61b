import java.util.List;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        if (k <= 0) {
            throw new IllegalArgumentException("k cannot be non-positive.");
        }

        // create a trie dictionary
        In inDic = new In(dictPath);
        if (!inDic.exists()) {
            throw new IllegalArgumentException("The dictionary file does not exist.");
        }
        Trie dict = new Trie();
        while (!inDic.isEmpty()) {
            String s = inDic.readString();
            dict.add(s);
        }
        inDic.close();

        // get the board information

        In inBoard = new In(boardFilePath);
        if (!inBoard.exists()) {
            return null;
        }
        ArrayList<Character> board = new ArrayList<>();
        int N = 0;
        int M = 0;
        int tempM = 0;
        while(inBoard.hasNextChar()) {
            char c = inBoard.readChar();
            if (c == '\n') {
                if (N > 0 && tempM != M) {
                    throw new IllegalArgumentException("Illegal board!");
                }
                N += 1;
                tempM = 0;
            } else {
                board.add(c);
                if (N == 0) {
                    M += 1;
                } else {
                    tempM += 1;
                }
            }
        }
        inBoard.close();
        if (N == 0 || M == 0) {
            throw new IllegalArgumentException("Illegal board.");
        }


        PriorityQueue<String> pq = new PriorityQueue(new strCmp());
        for (int i = 0; i < board.size(); i += 1) {
            List<Integer> visited = new ArrayList<>();
            String pre = "";
            Set<String> startAtI = getStrings(i, dict, board, visited, pre, N, M);
            for (String s: startAtI) {

                if (!pq.contains(s)) {
                    pq.add(s);
                }
            }
        }

        List<String> results = new ArrayList<>();
        for (int i = 0; i < k; i += 1) {
            if (pq.isEmpty()) {
                break;
            }
            results.add(pq.poll());
        }

        return results;
    }

    private static Set<String> getStrings(int start, Trie dic, List<Character> board,
                                           List<Integer> visited, String pre, int n, int m) {
        if (start < 0 || start > board.size() - 1) {
            throw new IllegalArgumentException();
        }
        Set<String> strs = new HashSet<>();
        Trie trie = dic;
        char c = board.get(start);
        if (trie != null && trie.links.containsKey(c)) {
            trie = trie.links.get(c);
            visited.add(start);
            pre += c;
            if (trie.exists) {
                strs.add(pre);
            }
            for (int j: neighbor(start, n, m)) {
                if (!visited.contains(j)) {
                    Set<String> next = getStrings(j, trie, board, visited, pre, n, m);
                    for (String s: next) {
                        strs.add(s);
                    }
                }
            }
        }
        return strs;
    }

    private static List<Integer> neighbor(int current, int n, int m) {
        if (current < 0 || current > n * m - 1) {
            throw new IllegalArgumentException("Current index is out of bounds.");
        }
        List<Integer> neighbors = new ArrayList<>();
        if (current % m != 0) {
            neighbors.add(current - 1);
            if (current > m) {
                neighbors.add(current - m - 1);
            }
            if (current < (n - 1) * m) {
                neighbors.add(current + m - 1);
            }

        }
        if ((current + 1) % m != 0) {
            neighbors.add(current + 1);
            if ((current > m - 1)) {
                neighbors.add(current - m + 1);
            }
            if (current < (n - 1) * m - 1) {
                neighbors.add(current + m + 1);
            }
        }
        if (current > m - 1) {
            neighbors.add(current - m);
        }
        if (current < (n - 1) * m) {
            neighbors.add(current + m);
        }
        return neighbors;

    }



    static class strCmp implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            int la = a.length();
            int lb = b.length();
            if (la == lb) {
                return a.compareTo(b);
            } else {
                return lb - la;
            }
        }
    }

    public static void main(String[] args) {
        int k = 7;
        String boardFilePath = "exampleBoard.txt";
        List<String> result = solve(k, boardFilePath);
        for (int i = 0; i < result.size(); i += 1) {
            if (i == 0) {
                System.out.print("[");
            }
            if (i < result.size() - 1) {
                System.out.print(result.get(i) + ", ");
            } else {
                System.out.print(result.get(i) + "]");
            }
        }
    }
}
