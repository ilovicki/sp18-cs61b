import java.util.HashMap;
import java.util.Map;
public class Trie {
    boolean exists;
    Map<Character, Trie> links;
    public Trie() {
        exists = false;
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
            t.exists = true;
            return t;
        }
        char c = s.charAt(d);
        t.links.put(c, add(t.links.get(c), s, d + 1));
        return t;
    }
}
