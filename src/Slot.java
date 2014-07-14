
public class Slot implements Comparable<Slot> 
{
    public Room room;
    public String session;
    public String chair;
    
    public Slot(Room room, String session, String chair)
    {
        this.room = room;
        this.session = session;
        this.chair = chair;
    }

    public int compareTo(Slot other)
    {
        if (this.room.order < other.room.order)
            return -1;
        else if (this.room.order > other.room.order)
            return 1;
        else return 0;
    }
    
    public String toString()
    {
        return "[" + room + "] " + session + " chaired by " + chair;
    }
}
