import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class CopyOfSession implements Comparable<CopyOfSession> {
    public int id = -1;
    public String name;
    public String title;
    public String chair;
    public Room location;
    public Hashtable<Integer,Integer> rooms = new Hashtable<Integer,Integer>();
    public HashSet<Integer> periods = new HashSet<Integer>();
    public List<Paper> papers = new ArrayList<Paper>();


    public CopyOfSession(int id, String name, String title){
        this.id = id;
        this.name = name;
        this.title = title;
    }


    public int compareTo(CopyOfSession o) {
        return this.id - o.id;
    }
}
