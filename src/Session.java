import java.util.ArrayList;
import java.util.List;

public class Session 
{
    public String name;

    public String title;

    public List<Paper> papers;
    
    public String kind;

    public Session(String name, String title, String kind)
    {
        this.name = name;
        this.title = title;
        this.kind = kind;
        this.papers = new ArrayList<Paper>();
    }

    public void print(StringBuffer buf, int period)
    {
       /* 
         buf.append("\t<slot>\n");
        buf.append("\t\t<id>" + period + "_" + rooms.get(period) + "</id>\n");
        buf.append("\t\t<name>" + name);
        if (title != null && !title.isEmpty())
            buf.append(": " + title);
        buf.append("</name>\n");
        buf.append("\t\t<location>\n\t\t\t<room>" + location.name + "</room>\n\t\t\t<map>" + location.map
            + "</map>\n\t\t</location>\n");
        if (chair != null && !chair.isEmpty())
            buf.append("\t\t<chair>" + chair + "</chair>\n");
        // and now print the papers
        for (Paper p : papers)
        {
            p.print(buf);
            // if (name.startsWith("Key")) {StringBuffer sb = new
            // StringBuffer(); p.print(sb); System.out.println(sb);}
        }

        buf.append("\t</slot>\n");
                */
    }
}
