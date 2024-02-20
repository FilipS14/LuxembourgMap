import org.w3c.dom.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class Graph {
    public static final float WIDTH = 500.f;
    public static final float HEIGHT = 750.f;

    public static Vector<Node> nodeArray;
    public static LinkedList<Edge>[] adjacencyList;
    public static Vector<Edge> edgeArray;
    private static int maxLongitude;
    private static int maxLatitude;
    private static int minLongitude;
    private static int minLatitude;

    public Graph() {
        try {
            readFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        scaleAndTranslateCoordinatesBasedOnBounds();
    }

    protected static void readFile() throws Exception {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream("hartaLuxembourg.xml");
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag();
        streamReader.nextTag();
        nodeArray = new Vector<>();
        edgeArray = new Vector<>();
        // Citirea nodurilor si construirea nodeList
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement() && "node".equals(streamReader.getLocalName())) {
                int id = Integer.parseInt(streamReader.getAttributeValue(null, "id"));
                int longitude = Integer.parseInt(streamReader.getAttributeValue(null, "longitude"));
                int latitude = Integer.parseInt(streamReader.getAttributeValue(null, "latitude"));
                nodeArray.add(new Node(id, longitude, latitude));
            }
            streamReader.next();
        }

        adjacencyList = new LinkedList[nodeArray.size()];
        for (int i = 0; i < nodeArray.size(); i++) {
            adjacencyList[i] = new LinkedList<>();
        }

        // Resetați streamReader pentru a începe din nou să citească de la început
        in = new FileInputStream("hartaLuxembourg.xml");
        streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag();
        streamReader.nextTag();

        // Citirea arcelor și construirea arcList și adjacencyList
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement() && "arc".equals(streamReader.getLocalName())) {
                int from = Integer.parseInt(streamReader.getAttributeValue(null, "from"));
                int to = Integer.parseInt(streamReader.getAttributeValue(null, "to"));
                int length = Integer.parseInt(streamReader.getAttributeValue(null, "length"));
                edgeArray.add(new Edge(from, to, length, Color.BLACK));
                addEdgeToAdjacencyList(from, to, length);
            }
            streamReader.next();
        }

    }
    private static void updateBoundsFromNodes() {
        if (nodeArray.isEmpty()) {
            return;
        }

        Node firstNode = nodeArray.get(0);
        maxLatitude = firstNode.getLatitude();
        maxLongitude = firstNode.getLongitude();
        minLatitude = firstNode.getLatitude();
        minLongitude = firstNode.getLongitude();

        for (int i = 1; i < nodeArray.size(); i++) {
            Node node = nodeArray.get(i);
            int latitude = node.getLatitude();
            int longitude = node.getLongitude();

            maxLatitude = Math.max(maxLatitude, latitude);
            maxLongitude = Math.max(maxLongitude, longitude);
            minLatitude = Math.min(minLatitude, latitude);
            minLongitude = Math.min(minLongitude, longitude);
        }
    }

    private static void addEdgeToAdjacencyList(int nodeStartID, int nodeEndID, int cost) {
        Edge forwardEdge = new Edge(nodeStartID, nodeEndID, cost, Color.BLUE);
        Edge backwardEdge = new Edge(nodeEndID, nodeStartID, cost, Color.BLUE);

        adjacencyList[nodeStartID].add(forwardEdge);
        adjacencyList[nodeEndID].add(backwardEdge);
    }


    protected static void scaleAndTranslateCoordinatesBasedOnBounds() {
        updateBoundsFromNodes();

        for (Node node : nodeArray) {
            int longitude = node.getLongitude();
            int latitude = node.getLatitude();
            int length = maxLongitude - minLongitude;
            int width = maxLatitude - minLatitude;

            float scaleX = WIDTH / length;
            float scaleY = HEIGHT / width;

            longitude = (int) ((longitude - minLongitude) * scaleY);
            latitude = (int) ((latitude - minLatitude) * scaleX);


            node.setLatitude(latitude + 200);
            node.setLongitude((int)(HEIGHT - longitude));
        }
    }

    protected static int findNodeByApproximateCoordinates(int x, int y) {
        for (Node node : nodeArray) {
            if (Math.abs(node.getLongitude() - y) < 5 && Math.abs(node.getLatitude() - x) < 5) {
                return node.getId();
            }
        }
        return -1;
    }


    protected static void Dijkstra(int start, int end) {
        int numberOfNodes = nodeArray.size();
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(numberOfNodes, Comparator.comparingInt(Pair::getFirst));

        int[] distance = new int[numberOfNodes];//distanta minima de la S la fiecare nod
        Arrays.fill(distance, Integer.MAX_VALUE);
        pq.add(new Pair<>(0, start));
        distance[start] = 0;

        boolean[] visited = new boolean[numberOfNodes];

        int[] parent = new int[numberOfNodes];
        Arrays.fill(parent, -1);

        while (!pq.isEmpty()) {
            int currentNode = pq.peek().getSecond();
            pq.poll();
            visited[currentNode] = true;
            for (Edge edge : adjacencyList[currentNode]) {
                int neighborNode = edge.getIdEnd();
                int cost = edge.getCost();
                if (!visited[neighborNode] && distance[neighborNode] > distance[currentNode] + cost) {
                    parent[neighborNode] = currentNode;
                    distance[neighborNode] = distance[currentNode] + cost;
                    pq.add(new Pair<>(distance[neighborNode], neighborNode));
                }
            }
        }

        colorEdgesOnShortestPath(parent, end, Color.RED);
    }

    private static void colorEdgesOnShortestPath(int[] parent, int destinationNodeID, Color pathColor) {
        for (int currentNode = destinationNodeID; parent[currentNode] != -1; currentNode = parent[currentNode]) {
            int previousNode = parent[currentNode];
            colorEdgeBetweenNodes(previousNode, currentNode, pathColor);
        }
    }

    private static void colorEdgeBetweenNodes(int startNodeID, int endNodeID, Color color) {
        for (Edge edge : edgeArray) {
            if ((edge.getIdStart() == startNodeID || edge.getIdEnd() == startNodeID) &&
                    (edge.getIdEnd() == endNodeID || edge.getIdStart() == endNodeID)) {
                edge.setColor(color);
            }
        }
    }
}
