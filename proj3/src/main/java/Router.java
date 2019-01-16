import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */

    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        List<Long> results = new ArrayList<>();
        Long s = g.closest(stlon, stlat);
        Long t = g.closest(destlon, destlat);
        Map<Long, Double> bestDists = new HashMap<>();
        Map<Long, Long> parents = new HashMap<>();
        bestDists.put(s, 0.0);
        class CmpNode implements Comparator<Long> {
            @Override
            public int compare(Long a, Long b) {
                double cmp = bestDists.get(a) + g.distance(a, t)
                        - (bestDists.get(b) + g.distance(b, t));
                if (cmp < 0) {
                    return -1;
                } else if (cmp > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        CmpNode cmp = new CmpNode();
        PriorityQueue<Long> pq = new PriorityQueue<>(cmp);
        pq.add(s);
        while (!pq.isEmpty()) {
            Long v = pq.poll();
            if (v.equals(t)) {
                break;
            }
            for (Long w: g.adjacent(v)) {
                Double tempDist = bestDists.get(v) + g.distance(v, w);
                if (!bestDists.containsKey(w)) {
                    bestDists.put(w, tempDist);
                    parents.put(w, v);
                    pq.add(w);
                } else if (pq.contains(w) && tempDist < bestDists.get(w)) {
                    bestDists.put(w, tempDist);
                    parents.put(w, v);
                }
            }
        }
        Stack<Long> path = new Stack<>();
        Long cur = t;
        while (cur != null) {
            path.push(cur);
            cur = parents.get(cur);
        }
        while (!path.isEmpty()) {
            results.add(path.pop());
        }
        return results;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> results = new ArrayList<>();
        List<Double> bearings = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Double> relativeBearings = new ArrayList<>();
        for (int i = 0; i < route.size() - 1; i += 1) {
            Long a = route.get(i);
            Long b = route.get(i + 1);
            bearings.add(g.bearing(a, b));
            distances.add(g.distance(a, b));
            names.add(g.getName(a, b));
        }
        relativeBearings.add(0.0);
        for (int j = 1; j < bearings.size(); j += 1) {
            relativeBearings.add(bearings.get(j) - bearings.get(j - 1));
        }
        String lastName = names.get(0);
        double lastDistance = distances.get(0);
        NavigationDirection nd = new NavigationDirection();
        nd.direction = getDirection(relativeBearings.get(0));
        nd.way = lastName;
        for (int k = 1; k < distances.size(); k += 1) {
            if (!names.get(k).equals(lastName)) {
                nd.distance = lastDistance;
                results.add(nd);
                lastName = names.get(k);
                lastDistance = distances.get(k);
                nd = new NavigationDirection();
                nd.direction = getDirection(relativeBearings.get(k));
                nd.way = lastName;
            } else {
                lastDistance += distances.get(k);
            }
        }
        nd.distance = lastDistance;
        results.add(nd);
        return results;
    }
    private static int getDirection(double relBear) {
        if (relBear == 0.0) {
            return 0;
        } else if (relBear >= - 15 && relBear <= 15) {
            return 1;
        } else if (relBear >= - 30 && relBear < - 15) {
            return 2;
        } else if (relBear > 15 && relBear <= 30) {
            return 3;
        } else if (relBear >= - 100 && relBear < - 30) {
            return 5;
        } else if (relBear > 30 && relBear <= 100) {
            return 4;
        } else if (relBear >= - 180 && relBear < - 100) {
            return 6;
        } else if (relBear > 100 && relBear <= 180) {
            return 7;
        } else if (relBear < - 180) {
            return getDirection(relBear + 360);
        } else {
            return getDirection(relBear - 360);
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
