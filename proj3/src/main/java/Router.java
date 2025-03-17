
import java.util.*;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static GraphDB graph;
    private static long startId;
    private static long endId;
    private static PriorityQueue<GNode> pq;
    private static TreeMap<Long, Double> distTo;
    private static TreeMap<Long, Long> edgeTo;
    private static TreeSet<Long> marked;

    private static class GNode {
        final long id;
        final double priority;
        private GNode(long id) {
            this.id = id;
            this.priority = distTo.get(id) + h(id);

        }
    }

    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {



        graph = g;
        startId = g.closest(stlon, stlat);
        endId = g.closest(destlon, destlat);

        distTo = new TreeMap<>();
        edgeTo = new TreeMap<>();
        marked = new TreeSet<>();

        for (Long id: graph.vertices()) {
            distTo.put(id, Double.MAX_VALUE);
        }

        distTo.put(startId, 0.0);
        edgeTo.put(startId, startId);

        pq = new PriorityQueue<>(new Comparator<GNode>() {

            @Override
            public int compare(GNode o1, GNode o2) {
                return Double.compare(o1.priority, o2.priority);
            }
        });

        pq.offer(new GNode(startId));

        aStar();

        LinkedList<Long> path = new LinkedList<>();

        long temp = endId;
        while (temp != startId) {
            if (edgeTo.get(temp) == null) {
                return new LinkedList<>();
            }
            path.addFirst(temp);
            temp = edgeTo.get(temp);
        }
        path.addFirst(startId);


        return path;
    }

    private static void aStar() {


        while (!pq.isEmpty()) {
            GNode gNodeId = pq.poll();

            long nodeId = gNodeId.id;

            if (marked.contains(nodeId)) {
                continue;
            }

            if (nodeId == endId) {
                return;
            }

            marked.add(nodeId);

            for (long neighbor: graph.adjacent(nodeId)) {
                double newDist = distTo.get(nodeId)
                        + graph.distance(nodeId, neighbor);
                if (newDist < distTo.get(neighbor)) {
                    distTo.put(neighbor, newDist);
                    edgeTo.put(neighbor, nodeId);
                }
                pq.offer(new GNode(neighbor));

            }
        }

    }

    private static double h(long id) {
        return graph.distance(id, endId);
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

        List<NavigationDirection> directions = new ArrayList<>();

        Long first= route.get(0);
        String firstName = g.edges.get(new GraphDB.Edge(first, route.get(1)));
        String preName = firstName;
        double totalDist = 0.0;

        /** a flag signs is whether the first way */
        boolean isStart = true;

        NavigationDirection nd = new NavigationDirection();
        nd.way = firstName;
        nd.direction = 0;

        for (int i = 0; i < route.size(); i++) {

            if (i == 0) {
                continue;
            }
            String curName = g.edges.get(new GraphDB.Edge(route.get(i - 1), route.get(i)));
            double dist = g.distance(route.get(i - 1), route.get(i));

            // a change in way
            if (!curName.equals(preName)) {

                if (isStart) {
                    isStart = false;
                }

                // pre way
                nd.way = preName.equals("unknown road") ? "": preName;
                nd.distance = totalDist;
                directions.add(nd);

                // next way
                nd = new NavigationDirection();
                nd.way = preName.equals("unknown road") ? "": curName;

                double pre = g.bearing(route.get(i - 2), route.get(i - 1));
                double cur = g.bearing(route.get(i - 1), route.get(i));
                nd.direction = calDirection(pre, cur);

                totalDist = dist;
                preName = curName;
            } else {
                totalDist += dist;
            }

            if (i == route.size() - 1) {
                nd.distance = totalDist;
                directions.add(nd);
            }

        }
        return directions;
    }

    private static int calDirection(double pre, double cur) {

        double relartiveBear = cur - pre;


        if (relartiveBear < -180) {
            relartiveBear += 360;
        } else if (relartiveBear > 180) {
            relartiveBear -= 360;
        }

        if (relartiveBear > -15 && relartiveBear < 15) {
            return 1;
        } else if (relartiveBear > -30 && relartiveBear < -15) {
            return 2;
        } else if (relartiveBear > 15 && relartiveBear < 30) {
            return 3;
        } else if (relartiveBear > 30 && relartiveBear < 100) {
            return 4;
        } else if (relartiveBear > -100 && relartiveBear < -30) {
            return 5;
        } else if (relartiveBear < -100) {
            return 6;
        } else {
            return 7;
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
