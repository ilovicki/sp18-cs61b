import java.util.HashMap;
import java.util.Map;
public class Trie {
    String item;
    Map<Character, Trie> links;
    public Trie() {
        item = null;
        links = new HashMap<>();
    }
    public void add(String s) {
        add(this, s, 0);
    }
    private Trie add(Trie t, String s, int d) {
        if (t == null) {
            t = new Trie();
        }
        if (d == s.length()) {
            t.item = s;
            return t;
        }
        char c = s.charAt(d);
        t.links.put(c, add(t.links.get(c), s, d + 1));
        return t;
    }
}
