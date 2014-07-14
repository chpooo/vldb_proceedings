import java.util.ArrayList;
import java.util.List;


public class Period implements Comparable<Period>
{
    public int day;
    public int starttime;
    public int endtime;
    public List<Slot> slots;

    
    public Period(int day, int starttime, int endtime)  
    {
        this.day = day;
        this.starttime = starttime;
        this.endtime = endtime;
        this.slots = new ArrayList<Slot>(); ;
    }
    
    public int compareTo(Period p)
    {
        if (this.day < p.day)
            return -1;
        else if (this.day > p.day)
            return 1;
        else if (this.starttime < p.starttime) 
            return -1;
        else if (this.starttime > p.starttime) 
            return 1;
        else if (this.endtime < p.endtime) 
            return -1;
        else if (this.endtime > p.endtime) 
            return 1;
        else return 0;        
    }
    
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("Day " + day + ": "+ starttime + "-"+ endtime + "\n");
        for (int i=0, imax=slots.size(); i< imax; i++)
        {
            buf.append("\t" + slots.get(i) + "\n");
        }
        return buf.toString();
    }

}
