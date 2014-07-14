import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;


public class MenuGenerator
{
    Conference conference;

    ArrayList<Room> rooms;

    ArrayList<String> days;

    List<Period> timeperiods;

    HashMap<String, Session> sessions;

    HashMap<Paper, ArrayList<Slot>> paperInvIndx = new HashMap<Paper, ArrayList<Slot>>();

    HashMap<Slot, Period> slotInvIndx = new HashMap<Slot, Period>();

    LinkedHashMap<String, ArrayList<Sponsor>> sponsors = null;

    int eventId = 0;

    private String clean(String s)
    {
        return s.replaceAll("&", "&amp;");
    }


    public MenuGenerator(String menufilename, Conference conference, ArrayList<Room> rooms,
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

        StringBuffer eventbuf = new StringBuffer();
        generateMenu(buf, eventbuf);
        // Write the results to a file
        try
        {
            FileWriter fstream = new FileWriter("lib/events.json");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("{\"events\":[");

            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(menufilename, true), "UTF-8");
            fw.write(buf.toString());
            fw.close();
            printAbout(buf);
            // remove last , from eventbuf:
            eventbuf.deleteCharAt(eventbuf.length() - 1);

            out.write(eventbuf.toString());
            out.write("]}");
            out.close();
        }
        catch (IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
        // Copy the preface
        Utils.append("lib/MenuTail.html", menufilename);
    }



    void generateMenu(StringBuffer buf, StringBuffer eventbuf)
    {

        fillUpInvIndx();
        buf.append("<H1>" + conference.name + " Program</H1>\n");
        buf.append("<ul id=\"accordion\">\n");
        buf.append("<li style=\"background-color:#006699\"><a style=\"color:#ffffff\" href=\"AtAGlance.html\">At a Glance</a></li>");
        buf.append("</ul>\n<p></p>");

        buf.append("<ul id=\"accordion\">\n");
        printType("Keynotes", buf, eventbuf);
        printType("Panels", buf, eventbuf);
        printType("Tutorials", buf, eventbuf);
        printType("Research", buf, eventbuf);
        printType("Industry", buf, eventbuf);
        printType("Demos", buf, eventbuf);
        printType("Business & Awards", buf, eventbuf);
        printType("Social Events", buf, eventbuf);
        printType("Workshops", buf, eventbuf);
        buf.append("</ul>\n");

        buf.append("<p> </p>\n");

        printSlots(buf);
        printSponsors(buf);
        printAbout(buf);
        buf.append("<p>&nbsp;</p>\n");



    }

    void fillUpInvIndx()
    {
        for (int periodi = 0, numOfPeriods = timeperiods.size(); periodi < numOfPeriods; periodi++)
        {
            Period period = timeperiods.get(periodi);
            for (int si = 0, simax = period.slots.size(); si < simax; si++)
            {
                Slot slot = period.slots.get(si);
                Session session = sessions.get(slot.session);
                for (int i = 0, imax = session.papers.size(); i < imax; i++)
                {
                    Paper p = session.papers.get(i);
                    ArrayList<Slot> paperSlots = paperInvIndx.get(p);
                    if (paperSlots == null)
                    {
                        paperSlots = new ArrayList<Slot>();
                        paperInvIndx.put(p, paperSlots);
                    }
                    paperSlots.add(slot);
                }
                slotInvIndx.put(slot, period);
            }
        }
    }

    void printType(String interest, StringBuffer buf, StringBuffer eventbuf)
    {
        buf.append("\t<li style=\"background-color:#99CCCC;color:#000000\">" + interest + "</li>\n");
        buf.append("\t\t<ul>\n");

        for (int periodi = 0, numOfPeriods = timeperiods.size(); periodi < numOfPeriods; periodi++)
        {
            Period iterperiod = timeperiods.get(periodi);


            for (int si = 0, simax = iterperiod.slots.size(); si < simax; si++)
            {
                Slot slot = iterperiod.slots.get(si);
                Session session = sessions.get(slot.session);
                if (!session.kind.equals(interest))
                    continue;
                // Room room = rooms.get(slot.room.order);
                for (int i = 0, imax = session.papers.size(); i < imax; i++)
                {
                    Paper p = session.papers.get(i);
                    // We print info only on the first slot (appearance)
                    if (paperInvIndx.get(p).get(0) != slot)
                        continue;
                    buf.append("\t\t<li><i>" + clean(p.title) + "</i>");
                    if ((p.authors != null) && (!p.authors.trim().equals("")))
                    {
                        buf.append("\n\t\t<br/>by " + clean(p.authors));
                    }
                    ArrayList<Slot> tmpSlots = paperInvIndx.get(p);
                    for (int tmpsi = 0, tmpsimax = tmpSlots.size(); tmpsi < tmpsimax; tmpsi++)
                    {
                        Slot tmpSlot = tmpSlots.get(tmpsi);
                        Period period = slotInvIndx.get(tmpSlot);
                        String periodId = "D" + period.day + "F" + period.starttime + "T" + period.endtime;

                        int fromHour = (period.starttime / 100);
                        int fromMin = period.starttime - (fromHour * 100);
                        String startTime = ((fromHour < 10) ? "0" + fromHour : fromHour) + ":"
                            + ((fromMin < 10) ? "0" + fromMin : fromMin);
                        int toHour = (period.endtime / 100);
                        int toMin = period.endtime - (toHour * 100);
                        String endTime = ((toHour < 10) ? "0" + toHour : toHour) + ":"
                            + ((toMin < 10) ? "0" + toMin : toMin);

                        String time = startTime + "-" + endTime;
                        String day = days.get(period.day);
                        String sid = periodId + "R" + tmpSlot.room.order;
                        if (tmpsi == 0)
                            buf.append("\n\t\t<br/>");
                        // else buf.append(" and ");
                        if ((period.starttime == 0) && (period.endtime == 0))
                            buf.append("click <a href=\"lib/FullProgram.html#" + sid
                                + "\">here</a> for abstract and web site");
                        else
                        {

                            // print the calendar links
                            String calendarTitle = "Presentation of the paper: " + p.title + " written by "
                                + p.authors;
                            eventbuf.append("{\"day\":\"" + day + "\",\n\"fromHour\": " + fromHour
                                + ",\n\"fromMin\": " + fromMin + ",\n\"toHour\": " + toHour + ",\n\"toMin\": "
                                + toMin + ",\n\"location\": \"" + tmpSlot.room.name + "\",\n\"title\": \""
                                + JSONObject.escape(calendarTitle) + "\"\n},");
                            buf.append("\n<div id = \"eventcal_" + eventId + "\" ></div>");
                            // and print the time
                            buf.append("&nbsp;<a href=\"lib/FullProgram.html#" + sid + "\">" + day + " at " + time
                                + "</a><br/>");
                        }
                        eventId++;
                    }
                    buf.append("\n\t\t\t</li><HR/>\n");
                }
            }
        }
        buf.append("\t\t</ul>\n");
    }

    private void printSlots(StringBuffer buf)
    {
        buf.append("<ul id=\"accordion\">\n");

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

            if (currDay != prevDay)
            {
                buf.append("\t<li style=\"background-color:#990000;color:#ffffff\">" + day + "</li>\n");
                buf.append("\t\t<ul>\n\t\t</ul>\n");
                prevDay = currDay;
            }

            String periodString = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + startTime + "-" + endTime;
            buf.append("\t<li>" + periodString + "</li>\n");
            buf.append("\t\t<ul>\n");
            String periodStr = "D" + period.day + "F" + period.starttime + "T" + period.endtime;
            for (int si = 0, simax = period.slots.size(); si < simax; si++)
            {
                Slot slot = period.slots.get(si);
                String id = periodStr + "R" + slot.room.order;
                // String place = "<a href=\"images/" + slot.room.map + "\">" +
                // slot.room.name + "</a>";
                Session session = sessions.get(slot.session);
                String title = session.title;
                String name = session.name;
                if ((session.papers.size() != 0)
                    && (session.name.contains("Tutorial") || session.name.contains("Panel") || session.name.contains("Keynote")))
                {
                    Paper paper = session.papers.get(0);
                    title = paper.title;
                }

                if (session != null && (!title.trim().equals("")))
                    name += ": " + title;
                buf.append("\t\t<li><a href=\"lib/FullProgram.html#" + id + "\">" + name + "</a> </li>\n");
                // String chair = "";
                // if ((slot.chair != null) && (!slot.chair.equals("")))
                // chair += " chaired by " + slot.chair;
                // buf.append(place + chair + "<HR/></li>\n");
            }
            buf.append("\t\t</ul>\n");
        }
        buf.append("</ul>\n");
    }

    private void printSponsors(StringBuffer buf)
    {
        buf.append("<ul id=\"accordion\">\n\t<li style=\"background-color:#FFCC00\">Sponsors</li>\n\t\t<ul>\n");
        Set<String> keys = sponsors.keySet();

        Iterator<String> it = keys.iterator();
        while (it.hasNext())
        {
            String type = it.next();
            buf.append("\t\t<li style=\"background-color:#99CCCC;color:#000000\"><b>" + type + "</b></li>\n");
            ArrayList<Sponsor> list = sponsors.get(type);
            buf.append("\t\t<li>\n");
            for (int i = 0, imax = list.size(); i < imax; i++)
            {
                Sponsor sponsor = list.get(i);
                buf.append("\t\t\t<a href=\"" + sponsor.link + "\"><img src=\"" + sponsor.image
                    + "\" Hspace=\"30\" Vspace=\"30\" height=" + sponsor.height + "\" border=0/></a>\n");
            }
            buf.append("\t\t</li>\n");
        }
        buf.append("\t\t</ul>\n</ul>\n");
    }

    private void printAbout(StringBuffer buf)
    {
        buf.append("<p/>\n<ul id=\"accordion\">\n\t<li>About</li>\n\t\t<ul>\n");
        buf.append("\t\t<li>\n");
        buf.append("\t\t\t<center>\n\t\t\t<H1>CPGT</H1>\n\t\t\t<p/>\n\t\t\t<h3>Conference Program Generation Tool</h3>\n\t\t\t<p/>\n");
        buf.append("\t\t\t<p><a href=\"http://db.disi.unitn.eu/pages/programBooklet\">http://db.disi.unitn.eu/pages/programBooklet</a></p>\n");
        buf.append("\t\t\tFor <b>" + conference.name + "</b><p/>\n");
        buf.append("\t\t\t<i>" + conference.credits + "</i><p/>\n");
        buf.append("\t\t\t&nbsp;<p/>&nbsp;<p/>");
        buf.append("\t\t\t<hr/><i>(c)2013 The dbTrento group. The CPGT was originally created for the needs of VLDB 2013 by Nick Tsitsarau, Siarhei Bykau and Yannis Velegrakis</i>");
        buf.append("\t\t</li>\n");
        buf.append("\t\t</ul>\n");
        buf.append("</ul>");

    }

}
