import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class BFS
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
        LinkedList<Node<Key>> toSearch = new LinkedList<>();

        // go through all children and check them, if they are the correct node
        // return a path containing only that node.
        // otherwise add the node to a list that is to be searched (this is to
        // avoid checking if the node is marked)
        for (KeyValuePair<Node<Key>, Integer> nodeAndWeight
                : from.adjacent)
        {
            // if the node is marked, it's been visited so skip it
            if (marked.contains(nodeAndWeight.Key.Identifier))
            {
                continue;
            }

            // node that is searched for, return path containing only it
            if (nodeAndWeight.Key.Identifier.equals(to))
            {
                LinkedList<Key> path =
                        new LinkedList<>();

                // this algorithms doesn't add the currently parent on return
                // so it is manually done here
                path.add(from.Identifier);
                path.add(to);

                return path;
            }

            // the node was not correct, add it as marked and the list for
            // nodes which children to check
            marked.add(nodeAndWeight.Key.Identifier);
            toSearch.addLast(nodeAndWeight.Key);
        }


        // go through each adjacent and search for a path to the given node
        for (Node<Key> node : toSearch)
        {
            // recursively find the path
            LinkedList<Key> pathResult =
                    search(node, to, marked);

            // if the result wasn't null then a path from one of the adjacent
            // children nodes was found, at it has parent in path
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
