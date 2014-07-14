public class Room
{
    public int order;

    public String name;

    public String map;

    public Room(String name, String map, int order)
    {
        this.order = order;
        this.name = name;
        this.map = map;
    }

    public String toString()
    {
        return order + "." + name + " (see " + map + ")";
    }

}
