package FinalProject;

import java.util.*;

/**
 * Represents a graph structure (a set of vertices each with a set of edges).
 *
 * @author Paymon Saebi
 */
public class Graph {


    /**
     * The graph underlying structure is a HashMap
     * Holds a set of vertices (String name mapped to Vertex instance)
     */

    private HashMap<String, Vertex> vertices;
    private boolean isDirected, isWeighted;
    public static final int infinity = Integer.MAX_VALUE;

    /**
     * Constructs an empty graph.
     * <p/>
     * Instantiates a new HashMap structure to hold the graph's vertices
     */
    public Graph() {
        vertices = new HashMap<String, Vertex>();
        isDirected = false;
        isWeighted = false;
    }

    /**
     * Set the graph's directed-ness indicator
     * <p/>
     * * @param directed - boolean indicating whether the graph will be directed
     */
    public void setDirected(boolean directed) {
        this.isDirected = directed;
    }

    /**
     * Get the graph's directed-ness indicator
     *
     * @return true if the graph is directed, false otherwise
     */
    public boolean getDirected() {
        return this.isDirected;
    }

    /**
     * Set the graph weighted-ness indicator
     *
     * @param weighted - boolean indicating whether the graph will be weighted
     */
    public void setWeighted(boolean weighted) {
        this.isWeighted = weighted;
    }

    /**
     * Get the graph weighted-ness indicator
     *
     * @return true if the graph is weighted, false otherwise
     */
    public boolean getWeighted() {
        return this.isWeighted;
    }

    /**
     * get the graphs list of vertices
     *
     * @return the HashMap structure holding the graph's vertices
     */
    public HashMap<String, Vertex> getVertices() {
        return this.vertices;
    }

    /**
     * Adds to the graph an edge from the vertex with name "name1" to the vertex with name "name2".
     * If the graph is not directed also adds an edge from vertex with name "name2" to vertex with name "name1".
     * If either vertex does not already exist in the graph, it is added.
     *
     * @param name1 - name of the first vertex
     * @param name2 - name of the other vertex
     */
    public void addEdge(String name1, String name2) {
        Vertex vertex1;
        if (vertices.containsKey(name1))
            vertex1 = vertices.get(name1);
        else {
            vertex1 = new Vertex(name1);
            vertices.put(name1, vertex1);
        }

        Vertex vertex2;
        if (vertices.containsKey(name2))
            vertex2 = vertices.get(name2);
        else {
            vertex2 = new Vertex(name2);
            vertices.put(name2, vertex2);
        }

        vertex1.addEdge(vertex2);
        if (!isDirected)
            vertex2.addEdge(vertex1);
    }

    /**
     * Adds to the graph an edge from the vertex with name "name1" to the vertex with name "name2".
     * If the graph is not directed also adds an edge from vertex with name "name2" to vertex with name "name1".
     * TIf either vertex does not already exist in the graph, it is added.
     * The edge is associated with the "weight".
     *
     * @param name1  - name of the first vertex
     * @param name2  - name of the other vertex
     * @param weight - the cost to traverse the edge
     */
    public void addEdgeWeighted(String name1, String name2, int weight) {
        Vertex vertex1;
        if (vertices.containsKey(name1))
            vertex1 = vertices.get(name1);
        else {
            vertex1 = new Vertex(name1);
            vertices.put(name1, vertex1);
        }

        Vertex vertex2;
        if (vertices.containsKey(name2))
            vertex2 = vertices.get(name2);
        else {
            vertex2 = new Vertex(name2);
            vertices.put(name2, vertex2);
        }

        vertex1.addEdgeWeighted(vertex2, weight);
        if (!isDirected)
            vertex2.addEdgeWeighted(vertex1, weight);
    }

    public Graph copy() {
        Graph graphCopy = new Graph();
        graphCopy.setDirected(this.getDirected());
        graphCopy.setWeighted(this.getWeighted());

        for (Vertex vertex : vertices.values()) {
            for (Edge e : vertices.get(vertex.getName()).getEdges())
                graphCopy.addEdge(vertex.getName(), e.getOtherVertex().getName());
        }

        return graphCopy;

    }

    /**
     * Performs Dijkstra's routine on a weighted graph to determine the cheapest path from start vertex to a goal vertex.
     * (See Lecture 19 for the algorithm.)
     * <p/>
     * Uses Java's PriorityQueue class to find the "unvisited vertex with smallest distance from start".
     * See the API for PriorityQueue, and ask the course staff if you need help.
     *
     * @param startName - Name of the starting vertex in the path
     * @param goalName  - Name of the ending vertex in the path
     * @return a list of the vertices that make up the cheapest path from the starting vertex (inclusive) to the
     * ending vertex (inclusive) based on weight associated with the edges between the graphs vertices
     * @throws UnsupportedOperationException if the graph is not weighted, or there are no vertices in the graph
     *                                       with the names startName or goalName
     */
    public List<String> dijkstrasShortestPath(String startName, String goalName) {
        // Throw exception if graph isn't weighted
        if (!getWeighted())
            throw new UnsupportedOperationException();

        // Throw exception if startName and goalName do not belong to any vertices in the graph
        HashMap<String, Vertex> map = getVertices();
        if (!map.containsKey(startName) || !map.containsKey(goalName))
            throw new UnsupportedOperationException();

        LinkedList<String> path = new LinkedList<String>();
        // Check if startName is already what we are looking for
        if (startName.equals(goalName)) {
            path.addFirst(startName);
            return path;
        }

        // Iterate through vertices of the graph an instantiate their costFromStart to infinity.
        Collection<Vertex> allVertecies = getVertices().values();
        Iterator<Vertex> verts = allVertecies.iterator();
        for (int i = 0; i < allVertecies.size(); i++)
            verts.next().setCostFromStart(infinity);

        // Except for our starter node
        map.get(startName).setCostFromStart(0);

        Vertex goal = map.get(goalName);

        //Start out priority queue
        PriorityQueueHEAP<Vertex> queue = new PriorityQueueHEAP<Vertex>();
        queue.add(map.get(startName));
        // Go through entire queue
        while (queue.size()!= 0) {
            Vertex temp = queue.deleteMin();

            // Exit loop if we find our goal
            if (temp.equals(goal))
                break;
            // Set each node we visit to visited.
            temp.setVisited(true);
            Iterator<Edge> edges = temp.getEdges().iterator();

            // Iterate through temp's edges
            for (int i = 0; i < temp.getEdges().size(); i++) {
                Edge nEdge = edges.next();
                Vertex n = nEdge.getOtherVertex();

                // Check costFromStart, vertex position in queue or add it to queue
                if (n.getCostFromStart() > temp.getCostFromStart() + nEdge.getWeight()) {
                    if (n.getCostFromStart() == infinity)
                        queue.add(n);
                    else {
                        // Update vertex priority be dequeing, updating and then requeing.
                        //queue.deleteMin(n);
                        n.setCostFromStart(temp.getCostFromStart() + nEdge.getWeight());
                        queue.add(n);
                    }
                    n.setCameFrom(temp);
                    n.setCostFromStart(temp.getCostFromStart() + nEdge.getWeight());
                }
            }
        }
        // Base case for no path.
        if (goal.getCameFrom() == null)
            return path;

        // Base case for start = goal
        for (Vertex v = goal; v != null; v = v.getCameFrom()) {
            path.addFirst(v.getName());
        }

        return path;
    }

}
