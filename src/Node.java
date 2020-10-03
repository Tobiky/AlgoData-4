import java.util.LinkedList;

public class Node<Key>
{
    public Key Identifier;
    // list of adjacent nodes, which are represented by the target
    // node and the weighted edge to that node
    public LinkedList<KeyValuePair<Node<Key>, Integer>> adjacent;

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        sb.append(String.format("%s: [", Identifier));

        for (KeyValuePair<Node<Key>, Integer> kvp : adjacent)
        {
            sb.append(String.format("%s, ", kvp.Key.Identifier));
        }

        sb.append("]}");
        return sb.toString();
    }
}
