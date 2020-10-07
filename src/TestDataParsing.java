/*
    Author: Andreas Hammarstrand
    Written: 2020/10/02
    Updated: 2020/10/07
    Purpose:
        TestDataParsing.java parses data from selected formats into graphs of
        WeightedAdjacencyList.

        These formats are:

        1. numericWeightedNodes
            Bidirectional Edges
            {head} {tail} {weight}
            {tail} {head} {weight}
            where head, tail, and weight are integers

        2. namedNodesUnidirectional
            Unidirectional Edges
            {tail} {head}
            where head and tail are Strings

        3. namedNodesBidirectional
            Bidirectional Edges
            {tail} {head}
            where head and tail are Strings

    Usage:
        Import the class and use the static functions to parse data from
        selected formats.

        Requires `WeightedAdjacencyList.java` to function.
 */

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class TestDataParsing
{
    public static WeightedAdjacencyList<Integer> numericWeightedNodes(
            Scanner data,
            int maxNodes)
    {
        if (maxNodes < 0)
        {
            maxNodes = Integer.MAX_VALUE;
        }

        // create new empty graph
        WeightedAdjacencyList<Integer> graph =
                new WeightedAdjacencyList<>();

        // save previous delimiter and use better one
        Pattern previousPattern = data.delimiter();

        // the first two lines of the file is just the number of nodes and
        // edges, skip those
        data.nextLine();
        data.nextLine();

        // [\s\n]+ will include all whitespaces and newline characters
        // between words
        data.useDelimiter("[\\s\\n]+");

        // go through each line of data
        while (graph.nodes() < maxNodes && data.hasNext())
        {
            // gets the first node which represents the tail of the edge, if
            // the edge is unidirectional
            int tail =
                    data.nextInt();

            // gets the second node which represents the head of the edge, if
            // the edge is unidirectional
            int head =
                    data.nextInt();

            int weight =
                    data.nextInt();

            // if the the graph does not contain the node, add it
            if (!graph.contains(tail))
            {
                graph.addNode(tail);
            }

            if (!graph.contains(head))
            {
                graph.addNode(head);
            }

            // adds a bidirectional edge with weight as the cost
            graph.addBiEdge(tail, head, weight, weight);

            // in data is in directional format, so edges appear twice
            data.nextLine();
            data.nextLine();
        }

        // restore old delimiter
        data.useDelimiter(previousPattern);

        return graph;
    }

    // creates a graph represented by a weighted adjacency list using the in
    // data and the appender to append the data items with a custom method
    private static WeightedAdjacencyList<String> namedNodes(
            Scanner data,
            Function<
                    WeightedAdjacencyList<String>,
                    Function<
                            String,
                            Consumer<String>>> appender)
    {
        // create new empty graph
        WeightedAdjacencyList<String> graph =
                new WeightedAdjacencyList<>();

        // save previous delimiter and use better one
        Pattern previousPattern = data.delimiter();

        // [\s\n]+ will include all whitespaces and newline characters
        // between words
        data.useDelimiter("[\\s\\n]+");

        // go through each line of data
        while (data.hasNext())
        {
            // gets the first node which represents the tail of the edge, if
            // the edge is unidirectional
            String tail =
                    data.next();

            // gets the second node which represents the head of the edge, if
            // the edge is unidirectional
            String head =
                    data.next();

            // if the the graph does not contain the node, add it
            if (!graph.contains(tail))
            {
                graph.addNode(tail);
            }

            if (!graph.contains(head))
            {
                graph.addNode(head);
            }

            // use the appender method to append the edge
            appender
                    .apply(graph)
                    .apply(tail)
                    .accept(head);
        }

        // restore old delimiter
        data.useDelimiter(previousPattern);

        return graph;
    }

    // returns a graph with bidirectional edges from the data input
    public static WeightedAdjacencyList<String> namedNodesBidirectional(
            Scanner data)
    {
        return namedNodes(
                data,
                list -> tail -> head ->
                        list.addBiEdge(tail, head)
        );
    }

    // returns a graph with unidirectional edges from the data input
    public static WeightedAdjacencyList<String> namedNodesUnidirectional(
            Scanner data)
    {
        return namedNodes(
                data,
                list -> tail -> head ->
                        list.addEdge(tail, head)
        );
    }
}
