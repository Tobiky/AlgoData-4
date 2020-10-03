import java.util.HashMap;
import java.util.LinkedList;

public class WeightedAdjacencyList<Key>
{
    private final HashMap<Key, Node<Key>> adjacencyList;

    public WeightedAdjacencyList()
    {
        adjacencyList = new HashMap<>();
    }

    // retrieves the node with meta with the given identifier
    public Node<Key> node(Key node)
    {
        return adjacencyList.get(node);
    }

    public void addNode(Key node)
    {
        Node<Key> nodeReference =
                new Node<>();

        nodeReference.Identifier = node;
        nodeReference.adjacent =
                new LinkedList<>();

        adjacencyList.put(node, nodeReference);
    }

    // adds a unidirectional edge from node a to node b, with the given weight
    public void addEdge(
            Key a,
            Key b,
            int weight)
    {
        KeyValuePair<Node<Key>, Integer> nodeAndEdge
                = new KeyValuePair<>();

        nodeAndEdge.Key = node(b);
        nodeAndEdge.Value = weight;

        // get node a and add node b as its adjacent
        adjacencyList
                .get(a)
                .adjacent
                    .add(nodeAndEdge);
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
                    .Identifier
                    .equals(head))
            {
                return true;
            }
        }

        return false;
    }
}
