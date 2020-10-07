/*
    Author: Andreas Hammarstrand
    Written: 2020/10/02
    Updated: 2020/10/06
    Purpose:
        KeyValuePair.java contains a definition for a two-value tuple.
    Usage:
        Node.java should only be used for other ADT's or algorithms.
 */

public class KeyValuePair<Key, Value>
{
    public final Key Key;
    public Value Value;

    public KeyValuePair(Key key, Value value)
    {
        this.Key = key;
        this.Value = value;
    }
}
