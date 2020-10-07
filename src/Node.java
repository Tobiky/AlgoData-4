/*
    Author: Andreas Hammarstrand
    Written: 2020/10/02
    Updated: 2020/10/06
    Purpose:
        Node.java contains the definition for a graph node ADT, as well as an
        additional field for weighted path finders. It also contains a
        comparator for distances of nodes.
    Usage:
        Node.java should only be used for other ADT's or algorithms.

 */

import java.util.Comparator;
import java.util.LinkedList;

public class Node<Key>
{
    public Key identifier;
    public int distance;

    // list of adjacent nodes, which are represented by the target
    // node and the weighted edge to that node
    public LinkedList<KeyValuePair<Node<Key>, Integer>> adjacent;

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        sb.append(String.format("%s: [", identifier));

        for (KeyValuePair<Node<Key>, Integer> entry : adjacent)
        {
            sb.append(String.format("%s, ", entry.Key.identifier));
        }

        sb.append("]}");
        return sb.toString();
    }

    public static class NodeDistanceComparator<Key> implements Comparator<Node<Key>>
    {
        @Override
        public int compare(Node<Key> o1, Node<Key> o2)
        {
            return o1.distance - o2.distance;
        }
    }
}
