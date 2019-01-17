import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private Map<Long, Node> vertices = new LinkedHashMap<>();
    private Map<Long, Way> ways = new LinkedHashMap<>();
    private Trie nodesTrie = new Trie();
    static class Node {
        long id;
        double lon;
        double lat;
        Set<Long> adj;
        Map<String, String> extraInfo;
        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            this.adj = new HashSet<>();
            this.extraInfo = new HashMap<>();
        }
    }

    static class Way {
        long id;
        String name;
        String type;
        String maxSpeed;
        ArrayList<Long> nodes;
        Way(long id, ArrayList<Long> nodes, String name, String type, String maxSpeed) {
            this.id = id;
            this.nodes = nodes;
            this.name = name;
            this.type = type;
            this.maxSpeed = maxSpeed;
        }
    }



    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // Your code here.
        Map<Long, Node> temp = new LinkedHashMap<>();
        for (Node n: vertices.values()) {
            if (!n.adj.isEmpty()) {
                temp.put(n.id, n);
            }
        }
        vertices = temp;

    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return vertices.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return vertices.get(v).adj;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closest = 0;
        double minD = Double.MAX_VALUE;
        for (long w: vertices()) {
            double distToW = distance(lon, lat, lon(w), lat(w));
            if (distToW < minD) {
                minD = distToW;
                closest = w;
            }
        }
        return closest;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertices.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertices.get(v).lat;
    }

    void addNode(Node n) {
        vertices.put(n.id, n);
        String name = n.extraInfo.get("name");
        if (name != null) {
            nodesTrie.add(n.id, cleanString(name));
        }

    }
    Node getNode(long id) {
        return vertices.get(id);
    }
    String getNodeName(long id) {
        return vertices.get(id).extraInfo.get("name");
    }

    void addEdge(Node a, Node b) {
        if (a == null || b == null) {
            return;
        }
        a.adj.add(b.id);
        b.adj.add(a.id);
    }
    void addWay(Way w) {
        ways.put(w.id, w);
    }
    String getName(long nodeA, long nodeB) {
        for (Way w: ways.values()) {
            if (w.nodes.contains(nodeA) && w.nodes.contains(nodeB)) {
                if (w.name == null) {
                    break;
                }
                return w.name;
            }
        }
        return "";
    }

    List<String> getLocationsByPrefix(String prefix) {
        List<String> names = new ArrayList<>();
        List<Long> indices = nodesTrie.idsWithPrefix(cleanString(prefix));
        for (long idx: indices) {
            names.add(getNodeName(idx));
        }
        return names;
    }
    List<Map<String, Object>> getLocations(String locationName) {
        List results = new ArrayList();
        List<Long> indices = nodesTrie.idsWithPrefix(cleanString(locationName));
        for (long idx: indices) {
            Map<String, Node> item = new HashMap<>();
            item.put(getNodeName(idx), getNode(idx));
            results.add(item);
        }
        return results;
    }

    static class Trie {
        long index;
        Map<Character, Trie> links;

        Trie() {
            links = new TreeMap<>();
            index = -1;
        }

        void add(long idx, String key) {
            add(this, idx, key, 0);
        }
        private Trie add(Trie x, long idx, String key, int d) {
            if (x == null) {
                x = new Trie();
            }
            if (d == key.length()) {
                x.index = idx;
                return x;
            }
            char c = key.charAt(d);
            x.links.put(c, add(x.links.get(c), idx, key, d + 1));
            return x;
        }
        List<Long> idsWithPrefix(String prefix) {
            return getIds(subRoot(prefix));

        }

        private Trie subRoot(String prefix) {
            Trie cur = this;
            for (int i = 0; i < prefix.length(); i += 1) {
                char c = prefix.charAt(i);
                if (cur == null) {
                    return new Trie();
                }
                cur = cur.links.get(c);
            }
            return cur;
        }

        private List<Long> getIds(Trie t) {
            List<Long> indices = new ArrayList<>();
            if (t == null) {
                return indices;
            }
            if (t.index > 0) {
                indices.add(t.index);
                return indices;
            }
            for (Map.Entry<Character, Trie> entry: t.links.entrySet()) {
                for (Long idx: getIds(entry.getValue())) {
                    indices.add(idx);
                }
            }
            return indices;
        }
    }
}
