import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS
{
    // searches for the given node `to` from given node `from` in
    // the given graph `graph`
    public static <Key> LinkedList<Key> search(
            Key from,
            Key to,
            WeightedAdjacencyList<Key> graph)
    {
        // if the graph does not contain either nodes, there can be no path so
        // nothing
        if (!graph.contains(from) || !graph.contains(to))
        {
            return null;
        }

        // otherwise we get the starting node and
        return search(
                    graph.node(from),
                    to);
    }

    // searches for the given node `to` from given node `from`
    public static <Key> LinkedList<Key> search(
            Node<Key> from,
            Key to)
    {
        // create a hash set with the start node in it already
        HashSet<Key> marked = new HashSet<>();
        marked.add(from.Identifier);

        return search(from, to, marked);
    }

    // recursively search for the given `to` node from the `from` node, while
    // skipping the nodes contained within `marked`
    private static <Key> LinkedList<Key> search(
            Node<Key> from,
            Key to,
            HashSet<Key> marked)
    {
        // the given node was found, return a list containing only that element
        // so that previous recursions can build onto it
        if (from.Identifier.equals(to))
        {
            LinkedList<Key> path =
                    new LinkedList<>();

            path.add(to);

            return path;
        }

        // go through each adjacent and search for a path to the given node
        for (KeyValuePair<Node<Key>, Integer> nodeAndWeight
                : from.adjacent)
        {
            // node has been marked, skip it
            if (marked.contains(
                    nodeAndWeight.Key
                            .Identifier))
            {
                continue;
            }

            marked.add(nodeAndWeight.Key.Identifier);

            // recursively find the path
            LinkedList<Key> pathResult =
                    search(nodeAndWeight.Key, to, marked);

            // if the result wasn't null then a path from one of the adjacent
            // nodes to the searched for node was found
            if (pathResult != null)
            {
                pathResult
                        .addFirst(from.Identifier);

                return pathResult;
            }
        }

        // no path was found, return null
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner file = new Scanner(new File(args[0]));

        // create graph from file
        WeightedAdjacencyList<String> graph =
                TestDataParsing.namedNodesBidirectional(file);

        // take input
        Scanner in = new Scanner(System.in);
        in.useDelimiter("[\\s\\n]+");

        do
        {
            // declare input to user
            System.out.print("Enter two nodes (separated by a space): ");

            // take in the two nodes and make sure they are uppercase as the
            // input data was in uppercase
            String x = in
                        .next()
                        .toUpperCase();

            if (x.equals("E"))
            {
                break;
            }

            String y = in
                        .next()
                        .toUpperCase();

            // from `x` to `y` in `graph`
            LinkedList<String> path = search(x, y, graph);

            // print path
            System.out.printf("Path: %s\n\n", path);
        }
        while (in.hasNextLine());
    }
}
