import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    static Map<String, Trie> dictMap;

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
        if (dictMap == null || !dictMap.containsKey(dictPath)) {
            dictMap = new HashMap<>();
            In inDic = new In(dictPath);
            if (!inDic.exists()) {
                throw new IllegalArgumentException("The dictionary file does not exist.");
            }
            Trie dict = new Trie();
            String[] words = inDic.readAllStrings();
            for (String word: words) {
                dict.add(word);
            }
            inDic.close();
            dictMap.put(dictPath, dict);
        }
        Trie dict = dictMap.get(dictPath);
        // get the board information
        In inBoard = new In(boardFilePath);
        if (!inBoard.exists()) {
            return null;
        }
        String[] boardStr = inBoard.readAllStrings();
        int N = boardStr.length;
        int M = boardStr[0].length();
        char[] board = new char[N * M];
        for (int i = 0; i < N; i += 1) {
            char[] temp = boardStr[i].toCharArray();
            if (temp.length != M) {
                throw new IllegalArgumentException("Illegal board!");
            }
            System.arraycopy(temp, 0, board, i * M, M);
        }
        inBoard.close();


        PriorityQueue<String> pq = new PriorityQueue(new StrCmp());
        int interval = 1;
        if (N > 20) {
            interval = N / 20;
        }
        for (int i = 0; i < N; i += interval) {
            int start = i * M;
            int end = (i + interval) * M;
            if (end >= N * M) {
                end = N * M;
            }
            Set<String> words = getStrings(start, end, dict, board, N, M);
            for (String word: words) {
                pq.add(word);
            }
        }


        List<String> results = new ArrayList<>();
        while(results.size() < k) {
            if (pq.isEmpty()) {
                break;
            }
            String temp = pq.poll();
            if (results.contains(temp)) {
                continue;
            } else {
                results.add(temp);
            }

        }

        return results;
    }

    //recursive solution
//    private static Set<String> getStrings(int start, Trie dic, char[] board,
//                                           List<Integer> visited, String pre, int n, int m) {
//        if (start < 0 || start > board.length - 1) {
//            throw new IllegalArgumentException();
//        }
//        Set<String> strs = new HashSet<>();
//        Trie trie = dic;
//        char c = board[start];
//        if (trie != null && trie.links.containsKey(c)) {
//            trie = trie.links.get(c);
//            visited.add(start);
//            pre += c;
//            if (trie.exists) {
//                strs.add(pre);
//            }
//            for (int j: neighbor(start, n, m)) {
//                List<Integer> jthVisited = new ArrayList<>(visited);
//                if (!jthVisited.contains(j)) {
//                    Set<String> next = getStrings(j, trie, board, jthVisited, pre, n, m);
//                    for (String s: next) {
//                        strs.add(s);
//                    }
//                }
//            }
//        }
//        return strs;
//    }

    private static class Node {
        int id;
        Trie ref;
        List<Integer> saw;
        Node(int id, Trie ref, List<Integer> saw) {
            this.id = id;
            this.ref = ref;
            this.saw = saw;
        }
    }

    private static Set<String> getStrings(int start, int end, Trie dic,
                                          char[] board, int n, int m) {
        Set<String> strs = new HashSet<>();
        ArrayDeque<Node> queue = new ArrayDeque<>();
        for (int i = start; i < end; i += 1) {
            List<Integer> ithSaw = new ArrayList<>();
            Node node = new Node(i, dic, ithSaw);
            queue.addLast(node);
        }
        while (!queue.isEmpty()) {
            Node cur = queue.pollFirst();
            int index = cur.id;
            Trie trie = cur.ref;
            List<Integer> curSaw = cur.saw;
            char c = board[index];
            if (trie == null || !trie.links.containsKey(c)) {
                continue;
            }
            trie = trie.links.get(c);
            if (trie.exists) {
                String curStr = "";
                for (int i: curSaw) {
                    curStr += board[i];
                }
                curStr += c;
                strs.add(curStr);
            }
            List<Integer> nextSaw = new ArrayList<>(curSaw);
            nextSaw.add(index);
            for (int i: neighbor(index, n, m)) {
                if (!nextSaw.contains(i)) {
                    queue.addLast(new Node(i, trie, nextSaw));
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



    static class StrCmp implements Comparator<String> {
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
        String boardFilePath = "smallBoard.txt";
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
