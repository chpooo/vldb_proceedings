import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class DoorProgramGenerator
{
    Conference conference;

    ArrayList<Room> rooms;

    ArrayList<String> days;

    List<Period> timeperiods;

    HashMap<String, Session> sessions;

    HashMap<Paper, ArrayList<Slot>> paperInvIndx = new HashMap<Paper, ArrayList<Slot>>();

    HashMap<Slot, Period> slotInvIndx = new HashMap<Slot, Period>();

    LinkedHashMap<String, ArrayList<Sponsor>> sponsors = null;

    private String clean(String s)
    {
        return s.replaceAll("&", "&amp;");
    }


    public DoorProgramGenerator(String menufilename, Conference conference, ArrayList<Room> rooms,
            ArrayList<String> days, ArrayList<Period> timeperiods, HashMap<String, Session> sessions,
            LinkedHashMap<String, ArrayList<Sponsor>> sponsors)
    {
        this.rooms = rooms;
        this.days = days;
        this.timeperiods = timeperiods;
        this.sessions = sessions;
        this.conference = conference;
        this.sponsors = sponsors;

        // Copy the preface
        Utils.copy("lib/MenuHead.html", menufilename);
        // create the menu
        StringBuffer buf = new StringBuffer();
        printSlots(buf);
        // Write the results to a file
        try
        {
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(menufilename, true), "UTF-8");
            fw.write(buf.toString());
            fw.close();
        }
        catch (IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
        // Copy the preface
        Utils.append("lib/MenuTail.html", menufilename);
    }



    private void printSlots(StringBuffer buf)
    {


        int prevDay = -1;
        for (int periodi = 0, numOfPeriods = timeperiods.size(); periodi < numOfPeriods; periodi++)
        {
            Period period = timeperiods.get(periodi);
            if ((period.starttime == 0) && (period.endtime == 0))
                continue;
            int currDay = period.day;
            String day = days.get(currDay);

            int fromHour = (period.starttime / 100);
            int fromMin = period.starttime - (fromHour * 100);
            String startTime = ((fromHour < 10) ? "0" + fromHour : fromHour) + ":"
                + ((fromMin < 10) ? "0" + fromMin : fromMin);
            int toHour = (period.endtime / 100);
            int toMin = period.endtime - (toHour * 100);
            String endTime = ((toHour < 10) ? "0" + toHour : toHour) + ":" + ((toMin < 10) ? "0" + toMin : toMin);


            for (int si = 0, simax = period.slots.size(); si < simax; si++)
            {
                Slot slot = period.slots.get(si);
                Session session = sessions.get(slot.session);
                String title = session.title;
                String name = session.name;
                if (session != null && (!title.trim().equals("")))
                    name += ": " + title;

                // Print the QR Code
                buf.append("<center><table width=\"100%\"><TR><TD align=\"left\"><font size=+3><b>" + slot.room.name
                    + "</b></font><TD/><TD align=\"right\"><img height=\"120\" src=\"http://lab.okkam.it/vldb/qrcodes/DAY_"
                    + (period.day + 1) + "_" + slot.room.name.substring(5) + ".png\"/></TD></TR></TABLE></center>\n");

                // Print the time and Date of the Conference
                buf.append("<ul id=\"accordion\">\n");
                buf.append("\t<li style=\"font-size:22px;font-weight:bold\">" + day + " " + startTime + "-" + endTime + "</li>\n");
                buf.append("</ul>\n");

                buf.append("<div style=\"font-size:18px;color:#CA3200\">" + name + "</div>");
                buf.append("<div style=\"font-weight:bold;font-size:16px\"><ul>");
                for (int i = 0, imax = session.papers.size(); i < imax; i++)
                {
                    Paper paper = session.papers.get(i);
                    buf.append("<li>" + paper.title + "<br/>\n<i style=\"font-weight:normal;color:#2CA9A9\">" + paper.authors + "</i></li>\n");
                }
                buf.append("</ul></div>");
                buf.append("<div style=\"page-break-after:always\"></div>\n");
            }
            // buf.append("\t<li style=\"background-color:#990000;color:#ffffff\">"
            // + day + "</li>\n");
        }
    }
}
