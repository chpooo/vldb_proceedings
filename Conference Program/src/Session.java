import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class Session implements Comparable<Session> {
    public int id = -1;
    public String name;
    public String title;
    public String chair;
    public String map;
    public Hashtable<Integer,Integer> rooms = new Hashtable<Integer,Integer>();
    public HashSet<Integer> periods = new HashSet<Integer>();
    public String room_name;
    public List<Paper> papers = new ArrayList<Paper>();


    public Session(int id, String name, String title){
        this.id = id;
        this.name = name;
        this.title = title;
    }

    public void print(StringBuffer buf, int period)
    {
        buf.append("\t<slot>\n");
        buf.append("\t\t<id>" + period + "_" + rooms.get(period) + "</id>\n");
        buf.append("\t\t<name>" + name);
        if (title != null && !title.isEmpty()) buf.append(": " + title);
        buf.append("</name>\n");
        buf.append("\t\t<location>\n\t\t\t<room>" + room_name + "</room>\n\t\t\t<map>" + map
            + "</map>\n\t\t</location>\n");
        if (chair != null && !chair.isEmpty()) buf.append("\t\t<chair>" + chair + "</chair>\n");
        // and now print the papers
        for (Paper p : papers)
        {
            p.print(buf);
            //if (name.startsWith("Key")) {StringBuffer sb = new StringBuffer(); p.print(sb); System.out.println(sb);}
        }

        buf.append("\t</slot>\n");
    }

    public int compareTo(Session o) {
        return this.id - o.id;
    }
}
