import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private class Node implements Comparable, Serializable {
        char ch;
        int freq;
        Node left;
        Node right;
        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Object other) {
            if (other == null || other.getClass() != this.getClass()) {
                throw new IllegalArgumentException("Not the same class.");
            }
            Node that = (Node) other;
            return this.freq - that.freq;
        }

    }
    Node binaryTrie;
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry: frequencyTable.entrySet()) {
            Node ithNode = new Node(entry.getKey(), entry.getValue(), null, null);
            pq.add(ithNode);
        }
        while (pq.size() > 1) {
            Node l = pq.poll();
            Node r = pq.poll();
            Node up = new Node('\0', l.freq + r.freq, l, r);
            pq.add(up);
        }
        binaryTrie = pq.poll();
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node trie = binaryTrie;
        String s = "";
        char character = '\0';
        for (int i = 0; i <= querySequence.length(); i += 1) {
            if (trie.left == null && trie.right == null) {
                character = trie.ch;
                break;
            }
            if (querySequence.bitAt(i) == 0) {
                trie = trie.left;
                s += '0';
            } else {
                trie = trie.right;
                s += '1';
            }
        }
        BitSequence bs = new BitSequence(s);
        Match match = new Match(bs, character);
        return match;
    }
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> table = new HashMap<>();
        Map<Character, String> tableStr = tableHelper(binaryTrie);
        for (Map.Entry<Character, String> entry: tableStr.entrySet()) {
            table.put(entry.getKey(), new BitSequence(entry.getValue()));
        }
        return table;
    }
    private Map<Character, String> tableHelper(Node trie) {
        Map<Character, String> table = new HashMap<>();
        if (trie.left == null && trie.right == null) {
            char character = trie.ch;
            String bs = "";
            table.put(character, bs);
            return table;
        }
        for (Map.Entry<Character, String> entryLeft: tableHelper(trie.left).entrySet()) {
            table.put(entryLeft.getKey(), '0' + entryLeft.getValue());
        }
        for (Map.Entry<Character, String> entryRight: tableHelper(trie.right).entrySet()) {
            table.put(entryRight.getKey(), '1' + entryRight.getValue());
        }
        return table;
    }
}
