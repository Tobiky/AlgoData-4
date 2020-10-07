/*
    Author: Andreas Hammarstrand
    Written: 2020/10/05
    Updated: 2020/10/06
    Purpose:
        PathExists consists of one function, pathExists, which produces a bool
        that represents if a path exists or not.
    Usage:
        Import class to use in specific settings or execute main with the first
        argument set to a file containing nodes and edges in bidirectional
        format. The nodes should use strings as identifiers.

        Bidirectional format:
            {head 1} {tail 1}
            {head 2} {tail 2}
            etc

        Requires `WeightedAdjacencyList.java`, `BFS.java`, and
        `TestDataParsing.java` to function.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PathExists
{
    public static <Key> boolean pathExists(
            WeightedAdjacencyList<Key> graph,
            Key from,
            Key to)
    {
        return BFS.search(graph, from, to) != null;
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
            System.out.println(pathExists(graph, x, y));
        }
        while (in.hasNextLine());
    }
}
