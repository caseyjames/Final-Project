package FinalProject;

import java.util.Iterator;

/**
 * Represents vertices for graphs.
 *
 * @author Paymon Saebi
 */
@SuppressWarnings("UnusedDeclaration")
public class Vertex implements Comparable<Vertex> {
    private String name;
    private int inDegree;
    private Vertex cameFrom;
    private boolean isVisited;
    private int costFromStart;
    private MyLinkedList<Edge> adj;

    /**
     * Constructs a vertex object with an empty list of edges
     *
     * @param _name - The name variable identifying the vertex instance
     */
    public Vertex(String _name) {
        this.name = _name;
        this.inDegree = 0;
        this.cameFrom = null;
        this.isVisited = false;
        this.costFromStart = 0;
        this.adj = new MyLinkedList<Edge>();
    }

    /**
     * Adds an edge to this vertex's list of edges
     * Associates the other end of the edge to the otherVertex
     * Since this edge enters otherVertex, it also increases its inDegree
     *
     * @param otherVertex - vertex to be connected to this vertex
     */
    public void addEdge(Vertex otherVertex) {
        this.adj.addLast(new Edge(otherVertex));
        otherVertex.incInDegree();
    }

    /**
     * Adds an edge to this vertex's list of edges
     * Associates the other end of the edge to the otherVertex
     * Associates a cost weight to this edge between these two vertices
     * Since this edge enters otherVertex, it also increases its inDegree
     *
     * @param otherVertex - vertex to be connected to this vertex
     * @param weight      - the cost associated with this new edge
     */
    public void addEdgeWeighted(Vertex otherVertex, int weight) {
        this.adj.addLast(new Edge(otherVertex, weight));
        otherVertex.incInDegree();
    }

    /**
     * Getter method for the list iterator of the this vertex's edges
     *
     * @return this vertex's edge list iterator
     */
    public Iterator<Edge> edges() {
        return this.adj.iterator();
    }

    /**
     * getter method for the list of this vertex's edges
     *
     * @return this vertex's edge list
     */
    public MyLinkedList<Edge> getEdges() {
        return this.adj;
    }

    /**
     * Getter method for this vertex's identifying name
     *
     * @return this vertex's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter method for the vertex's having-been-visited status
     *
     * @param status - whether this vertex is been visited
     */
    public void setVisited(boolean status) {
        this.isVisited = status;
    }

    /**
     * Getter method for the vertex's having-been-visited status
     *
     * @return true if the vertex has been visited, false otherwise
     */
    public boolean getVisited() {
        return this.isVisited;
    }

    /**
     * Setter method for this vertex's previous vertex in a path
     *
     * @param previous - the vertex that came before this vertex in a path
     */
    public void setCameFrom(Vertex previous) {
        this.cameFrom = previous;
    }

    /**
     * Getter method for this vertex's previous vertex in a path
     *
     * @return the vertex that came before this vertex in a path
     */
    public Vertex getCameFrom() {
        return this.cameFrom;
    }

    /**
     * Setter method for this vertex's total cost from a starting point in a path
     *
     * @param dist - this vertex's accumulated cost from a starting point in a path
     */
    public void setCostFromStart(int dist) {
        this.costFromStart = dist;
    }

    /**
     * Getter method for this vertex's total cost from a starting point in a path
     *
     * @return this vertex's accumulated cost from a starting point in a path
     */
    public int getCostFromStart() {
        return this.costFromStart;
    }

    /**
     * Getter method for this vertex's inDegree, the number of edges that point to this vertex
     *
     * @return the number of edges that point to this vertex's
     */
    public int getInDegree() {
        return this.inDegree;
    }

    /**
     * Setter method for this vertex to decrement its inDegree by one
     */
    public void decInDegree() {
        this.inDegree--;
    }

    /**
     * Setter method for this vertex to increment its inDegree by one
     */
    public void incInDegree() {
        this.inDegree++;
    }

    /**
     * Compares this vertex with another for quality based on their name fields
     *
     * @param obj - the object representing the other vertex for comparison
     * @return true if this vertex's name was the same as the other's
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex))
            return false;

        Vertex other = (Vertex) obj;

        return this.name.compareTo(other.getName()) == 0;
    }
//
//    /**
//     * Generates a verbose string with the vertex's name and it's adjacent vertices' names
//     *
//     * @return the string representing this vertex
//     */
//    @Override
//    public String toString() {
//        String s = "Vertex " + name + " adjacent to ";
//
//        for (Edge anAdj : adj) s += anAdj.getOtherVertex().getName() + "  ";
//
//        return s;
//    }

    /**
     * The comparable implementation for vertices, for the priority queue used in Dijkstra's algorithm
     * Comparison is based on the the vertices total cost of traversal from a starting point in a path
     *
     * @return integer 0 if they have equal total costs, 1 if this vertex has a larger cost, and -1 if not
     */
    @SuppressWarnings("NullableProblems")
    public int compareTo(Vertex arg0) {
        if (arg0 == null)
            return 0;

        double diff = this.getCostFromStart() - arg0.getCostFromStart();

        if (diff > 0)
            return 1;
        else if (diff < 0)
            return -1;
        else
            return 0;
    }
//
//    public boolean hasEdge(Vertex v) {
//        for (Edge e : adj) {
//            if (e.getOtherVertex().equals(v))
//                return true;
//        }
//        return false;
//    }
}