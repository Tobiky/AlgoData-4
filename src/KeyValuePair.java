public class KeyValuePair<Key, Value>
{
    public Key Key;
    public Value Value;

    @Override
    public String toString()
    {
        return String.format("<%s, %s>", Key, Value);
    }
}
