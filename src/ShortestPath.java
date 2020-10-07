/*
    Author: Andreas Hammarstrand
    Written: 2020/10/05
    Updated: 2020/10/06
    Purpose:
        ShortestPath.java attempts at finding the shortest path between two
        nodes, by passing through a set of nodes (that can be the zero set).
        This is done through Dijkstra's algorithm between each pair of nodes.
    Usage:
        The first argument is the file containing the unidirectional data
        points, e.i:
            {head} {tail} {weight}
            {tail} {head} {weight}

        The second argument is the output times and data points in CSV format

        The third argument is the max amount of nodes to record for the graph

        Only the first argument is required. The other two are optional.
        Once the program has processed the graph file, the user can input
        a series of nodes to have the algorithm search through. The first
        node is the starting node and the last node is the goal node.

        Requires `WeightedAdjacencyList.java` and `TestDataParsing.java` to
        function.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ShortestPath
{
    public static <Key> LinkedList<Key> search(
            WeightedAdjacencyList<Key> graph,
            Key from,
            Key to)
    {
        // the keys that have been marked as visited
        HashSet<Key> marked =
                new HashSet<>();

        // gives the previous node for the given node identifier, through
        // the end node the fastest path can be found
        HashMap<Key, Node<Key>> previousNodes =
                new HashMap<>();

        // a new queue that is sorted based on the distance of the node from
        // the start
        PriorityQueue<Node<Key>> toVisit =
                new PriorityQueue<>(
                        new Node.NodeDistanceComparator<>());

        // initial state
        marked.add(from);
        toVisit.add(graph.node(from));

        // while there are nodes to visit, do Dijkstra's algorithm
        while (!toVisit.isEmpty())
        {
            // get the node with the shortest distance in the queue
            Node<Key> node = toVisit.remove();

            // end node was found, trace pathing into linked list
            if (node.identifier.equals(to))
            {
                LinkedList<Key> path =
                        new LinkedList<>();

                // iterate backwards and add onto the list
                Node<Key> current = node;
                do
                {
                    path.addFirst(current.identifier);
                    current = previousNodes.get(current.identifier);
                }
                while (current != null);

                return path;
            }

            // go through the adjacent nodes
            for (KeyValuePair<Node<Key>, Integer> nodeAndWeight
                        : node.adjacent)
            {
                // skip all marked nodes
                if (!marked.contains(nodeAndWeight.Key.identifier))
                {
                    int distance = node.distance + nodeAndWeight.Value;

                    // if the distance is less than whats found in the adjacent
                    // node assign it the new distance and set the predecessor
                    // to be the current node (not the adjacent)
                    if (distance < nodeAndWeight.Key.distance)
                    {
                        nodeAndWeight.Key.distance =
                                distance;

                        previousNodes.put(
                                nodeAndWeight.Key.identifier,
                                node);
                    }

                    // add the adjacent node to the marked set and the visit
                    // list
                    toVisit.add(nodeAndWeight.Key);
                    marked.add(nodeAndWeight.Key.identifier);
                }
            }
        }

        // no path found
        return null;
    }

    // initializes the nodes' meta information to fit this algorithm
    public static <Key> void initializeNodes(WeightedAdjacencyList<Key> graph, Key start)
    {
        // set all nodes except the starting node to max, or infinity if
        // appropriate
        for (Key key : graph)
        {
            graph.node(key).distance = Integer.MAX_VALUE;
        }

        // starting node distance is set to 0
        graph.node(start).distance = 0;
    }

    public static void main(String[] args) throws IOException
    {
        Scanner file = new Scanner(new File(args[0]));

        // if there is a second argument, that argument is for the data
        // collection.
        // the file of that path is created if it did not already exist
        FileWriter executionTime = null;
        if (args.length > 1)
        {
            File execTimeFile = new File(args[1]);

            // if file was created, print header. otherwise just append to file
            if (execTimeFile.createNewFile())
            {
                executionTime = new FileWriter(execTimeFile);
                executionTime.write("Time, Nodes, Edges, Total, Bypass Count\n");
                executionTime.flush();
            }
            else
            {
                executionTime = new FileWriter(execTimeFile, true);
            }
        }

        // sets the maximum amount of nodes to read in
        int maxNodes = -1;
        if (args.length > 2)
        {
            maxNodes = Integer.parseInt(args[2]);
        }

        // create graph from file
        WeightedAdjacencyList<Integer> graph =
                TestDataParsing.numericWeightedNodes(file, maxNodes);

        file.close();

        // for input
        Scanner in = new Scanner(System.in);

        while (true)
        {
            // declare input to user
            System.out.println("Enter a series of nodes to pass through.\n" +
                    "\tFirst node will be the start and last will be the end " +
                    "node");

            // take input
            String input =
                    in.nextLine();

            // breakout condition
            if (input.toLowerCase().equals("e"))
            {
                break;
            }

            // parse input to correct values:
            // Integer{space}Integer{space}...{space}Integer
            String[] values =
                    input.split("\\s+");

            // the inputted integers are the start station, the midway
            // stations, and the ending station respectively
            List<Integer> stations =
                    new ArrayList<>();

            for (String val : values)
            {
                stations.add(Integer.parseInt(val));
            }

            // the total path, from first to last through the given extra nodes
            LinkedList<Integer> path
                    = new LinkedList<>();


            long start = System.nanoTime();
            // go through all stations pairwise
            for (int i = 0; i < stations.size() - 1; i++)
            {
                // set all nodes to their default Dijkstra state.
                // adding the parental nodes, which is what creates the path,
                // is dependant on distances from selected start and end nodes
                initializeNodes(graph, stations.get(0));

                // get the path between the two points
                LinkedList<Integer> subpath =
                        search(graph, stations.get(i), stations.get(i + 1));

                // there was no from selected pair of nodes, break execution
                // and clear path to flag that no path exists
                if (subpath == null)
                {
                    path.clear();
                    break;
                }

                // first will always be duplicate to last of previous subpath
                subpath.removeFirst();

                // add the subpath to the total path
                path.addAll(subpath);
            }
            long time = System.nanoTime() - start;

            // if some path was found, correct errors and print it
            // also print execution time
            if (!path.isEmpty())
            {
                // re-add the very first node
                path.addFirst(stations.get(0));

                System.out.printf("Path: %s\n", path);
                System.out.printf("Time: %f s\n\n", time / 1e9f);

                if (executionTime != null)
                {
                    executionTime.write(
                            String.format(
                                    "%d, %d, %d, %d, %d\n",
                                    time,
                                    graph.nodes(),
                                    graph.edges(),
                                    graph.nodes() + graph.edges(),
                                    stations.size() - 2
                            )
                    );
                    executionTime.flush();
                }
            }
            // no path was found, inform user
            else
            {
                System.out.println("No path\n");
            }
        }
    }
}
