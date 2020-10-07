/*
    Author: Andreas Hammarstrand
    Written: 2020/09/30
    Updated: 2020/10/06
    Purpose:
        WeightedAdjacencyList.java is a adjacency list that supports actions
        for undirected, directed, and weighted graphs.
    Usage:
        Import as a class to use the structure or execute the main function to
        test the structure
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class WeightedAdjacencyList<Key> implements Iterable<Key>
{
    private final HashMap<Key, Node<Key>> adjacencyList;

    private int edges;
    private int nodes;

    public int edges() { return edges; }
    public int nodes() { return nodes; }

    public WeightedAdjacencyList()
    {
        adjacencyList = new HashMap<>();
    }

    // retrieves the node with meta with the given identifier
    public Node<Key> node(Key node)
    {
        return adjacencyList.get(node);
    }

    // add the node to the graph
    public void addNode(Key node)
    {
        Node<Key> nodeReference =
                new Node<>();

        nodeReference.identifier = node;
        nodeReference.adjacent =
                new LinkedList<>();

        adjacencyList.put(node, nodeReference);

        nodes++;
    }

    // adds a unidirectional edge from node a to node b, with the given weight
    public void addEdge(
            Key a,
            Key b,
            int weight)
    {
        KeyValuePair<Node<Key>, Integer> nodeAndEdge
                = new KeyValuePair<>(node(b), weight);

        // get node a and add node b as its adjacent
        adjacencyList
                .get(a)
                .adjacent
                    .add(nodeAndEdge);

        edges++;
    }

    // adds a unidirectional edge from node a to node b, with no weight
    public void addEdge(
            Key a,
            Key b)
    {
        addEdge(a, b,0);
    }

    // adds a bidirectional wedge between A and B, with the weights from A to
    // B and B to A, respectively
    public void addBiEdge(
            Key a,
            Key b,
            int weightAB,
            int weightBA)
    {
        addEdge(a, b, weightAB);
        addEdge(b, a, weightBA);
    }

    // adds a bidirectional wedge between A and B, with no weights in between
    public void addBiEdge(
            Key a,
            Key b)
    {
        addBiEdge(a, b, 0, 0);
    }

    // checks if the specified node is contained within the graph
    public boolean contains(
            Key node)
    {
        return adjacencyList.containsKey(node);
    }

    // checks if given (unidirectional) edge is contained within the graph
    public boolean containsEdge(
            Key tail,
            Key head)
    {
        // go through the list of adjacents for the given tail and check
        // if one of them is the given head
        for (KeyValuePair<Node<Key>, Integer> nodeAndWeight
                : adjacencyList.get(tail).adjacent)
        {
            if (nodeAndWeight
                    .Key
                    .identifier
                    .equals(head))
            {
                return true;
            }
        }

        return false;
    }

    // returns an iterator over the nodes
    @Override
    public Iterator<Key> iterator()
    {
        return adjacencyList.keySet().iterator();
    }
}
