/*
    Author: Andreas Hammarstrand
    Written: 2020/10/03
    Updated: 2020/10/06
    Purpose:
        BFS.java attempts at implementing Breadth First Search for graphs in
        the WeightedAdjacencyList structure.
    Usage:
        Import to use on any graph that is in the WeightedAdjacencyList
        structure or use the main function with the path to a text file as the
        first argument. The text file should be a file only containing
        bidirectional edges where the nodes use string identifiers.

        Requires `WeightedAdjacencyList.java` and `TestDataParsing.java` to
        function.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BFS
{
    // searches for the given node `to` from given node `from` in
    // the given graph `graph`
    public static <Key> LinkedList<Key> search(
            WeightedAdjacencyList<Key> graph,
            Key from,
            Key to)
    {
        // if the graph does not contain either nodes, there can be no path so
        // nothing
        if (!graph.contains(from) || !graph.contains(to))
        {
            return null;
        }

        // otherwise we get the starting node and
        return search(graph.node(from), to);
    }

    // searches for the given node `to` from given node `from`
    public static <Key> LinkedList<Key> search(
            Node<Key> from,
            Key to)
    {
        // create a set containing the marked nodes
        HashSet<Key> marked =
                new HashSet<>();

        marked.add(from.identifier);

        // all the paths built up
        Queue<LinkedList<Node<Key>>> toSearch =
                new LinkedList<>();

        // the initial path; the starting node
        LinkedList<Node<Key>> firstPath =
                new LinkedList<>();
        firstPath.add(from);

        toSearch.add(firstPath);

        // check all paths within the path queue until the correct one is found
        // otherwise return null
        while (!toSearch.isEmpty())
        {
            // get the first path in the queue
            LinkedList<Node<Key>> path =
                    toSearch.remove();

            // last node of the list, on path to parent
            Node<Key> endNode =
                    path.getLast();

            // if a path containing the end node was found, return said path
            // but only with the keys (the Node<Key> structure is not relevant
            // to the user)
            if (endNode.identifier.equals(to))
            {
                LinkedList<Key> identifierPath =
                        new LinkedList<>();

                for (Node<Key> node : path)
                {
                    identifierPath.addLast(node.identifier);
                }

                return identifierPath;
            }

            for (KeyValuePair<Node<Key>, Integer> nodeAndWeight
                        : endNode.adjacent)
            {
                if (!marked.contains(nodeAndWeight.Key.identifier))
                {
                    // copy the path to the parent node
                    LinkedList<Node<Key>> newPath =
                            new LinkedList<>(path);

                    // add the current adjacent
                    newPath.addLast(nodeAndWeight.Key);

                    // enqueue path
                    toSearch.add(newPath);

                    // set node as marked
                    marked.add(nodeAndWeight.Key.identifier);
                }
            }
        }

        return null;
    }


    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner file = new Scanner(new File(args[0]));

        // create graph from file
        WeightedAdjacencyList<String> graph =
                TestDataParsing.namedNodesUnidirectional(file);

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
            LinkedList<String> path = search(graph, x, y);

            // print path
            System.out.printf("Path: %s\n\n", path);
        }
        while (in.hasNextLine());
    }
}
