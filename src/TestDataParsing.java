import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class TestDataParsing
{
    public static WeightedAdjacencyList<Integer> numericWeightedNodes(
            Scanner data)
    {
        throw new UnsupportedOperationException();
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
                    .apply(head)
                    .accept(tail);
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
                list -> head -> tail ->
                        list.addBiEdge(head, tail)
        );
    }

    // returns a graph with unidirectional edges from the data input
    public static WeightedAdjacencyList<String> namedNodesUnidirectional(
            Scanner data)
    {
        return namedNodes(
                data,
                list -> head -> tail ->
                        list.addEdge(head, tail)
        );
    }
}
