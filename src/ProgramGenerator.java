import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProgramGenerator
{
    Conference conference;

    ArrayList<Room> rooms;

    ArrayList<String> days;

    List<Period> timeperiods;

    HashMap<String, Session> sessions;

    private String clean(String s)
    {
        return s.replaceAll("&", "&amp;");
    }


    public ProgramGenerator(String inputFileName, Conference conference, ArrayList<Room> rooms,
            ArrayList<String> days, ArrayList<Period> timeperiods, HashMap<String, Session> sessions)
    {
        this.rooms = rooms;
        this.days = days;
        this.timeperiods = timeperiods;
        this.sessions = sessions;
        this.conference = conference;

        StringBuffer buf = new StringBuffer();
        generateXML(buf);
        // Write the results to a file
        try
        {
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(inputFileName, false), "UTF-8");
            fw.write(buf.toString());
            fw.close();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource("./lib/" + inputFileName.replaceFirst("\\.xml", "\\.xsl")));
            FileOutputStream html = new FileOutputStream("./lib/" + inputFileName.replaceFirst("\\.xml", "\\.html"));
            BufferedReader template = new BufferedReader(new FileReader("./lib/FullProgramAjax.html"));
            String line = null;
            while ((line = template.readLine()) != null)
            {
                if (!line.contains("<!-- content -->")) {
                    html.write(line.concat("\n").getBytes());
                } else {
                    transformer.transform(new StreamSource(inputFileName), new StreamResult(html));
                }
            }
            template.close();
            html.close();
        }
        catch (Exception ioe)
        {
            System.err.println("Exception: " + ioe.getMessage());
        }

    }



    void generateXML(StringBuffer buf)
    {
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buf.append("<?xml-stylesheet type=\"text/xsl\" href=\"./lib/PrintProgramPdf.xsl\"?>\n");
        buf.append("<program xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");

        for (int periodi = 0, numOfPeriods = timeperiods.size(); periodi < numOfPeriods; periodi++)
        {
            Period period = timeperiods.get(periodi);
            print(period, buf);
        }

        buf.append("\n</program>");
    }

    private void print(Period p, StringBuffer buf)
    {
        String dayStr = days.get(p.day);
        int fromHour = (p.starttime / 100);
        int fromMin = p.starttime - (fromHour * 100);
        String fromStr = ((fromHour < 10) ? "0" + fromHour : fromHour) + ":"
            + ((fromMin < 10) ? "0" + fromMin : fromMin);
        int toHour = (p.endtime / 100);
        int toMin = p.endtime - (toHour * 100);
        String toStr = ((toHour < 10) ? "0" + toHour : toHour) + ":" + ((toMin < 10) ? "0" + toMin : toMin);

        String pid = "D" + p.day + "F" + p.starttime + "T" + p.endtime;
        buf.append("<period>\n");
        buf.append("\t<id>" + clean(pid) + "</id>\n");
        String timing = fromStr + "-" + toStr;
        if (p.starttime == p.endtime)
            timing = "";
        else timing = dayStr + " " + timing;
        buf.append("\t<name>" + clean(timing) + "</name>\n");
        // Generate the slots
        for (Slot slot : p.slots)
        {
            print(slot, pid, buf);
        }
        buf.append("</period>\n");
    }

    private void print(Slot s, String pid, StringBuffer buf)
    {
        String sid = pid + "R" + s.room.order;
        buf.append("\t<slot>\n");
        buf.append("\t\t<id>" + clean(sid) + "</id>\n");
        buf.append("\t\t<name>" + clean(s.session));
        Session session = sessions.get(s.session);
        if (session == null)
            System.err.println(s.session + " Session was not found in the list of sessions");
        String stitle = session.title;
        if (stitle != null && !stitle.isEmpty())
            buf.append(": " + clean(stitle));
        buf.append("</name>\n");
        Room room = rooms.get(s.room.order);
        buf.append("\t\t<location>\n\t\t\t<room>" + clean(room.name) + "</room>\n\t\t\t<map>" + clean(room.map)
            + "</map>\n\t\t</location>\n");
        if (s.chair != null && !s.chair.isEmpty())
            buf.append("\t\t<chair>" + clean(s.chair) + "</chair>\n");
        // and now print the papers
        for (Paper p : session.papers)
        {
            print(p, buf);
        }
        buf.append("\t</slot>\n");
    }

    private void print(Paper p, StringBuffer buf)
    {
        buf.append("\t\t<paper>\n");
        buf.append("\t\t\t<id>" + clean(p.id + "") + "</id>\n");
        if (p.link != null && !p.link.isEmpty())
            buf.append("\t\t\t<link>" + clean(p.link) + "</link>\n");
        buf.append("\t\t\t<title>" + clean(p.title) + "</title>\n");
        buf.append("\t\t\t<authors>" + clean(p.authors) + "</authors>\n");
        if (p.paperAbstract != null && !p.paperAbstract.isEmpty())
            buf.append("\t\t\t<abstract>" + clean(p.paperAbstract) + "</abstract>\n");


        for (int i = 0, imax = p.bios.size(); i < imax; i++)
        {
            buf.append("\t\t\t<authorInfo>\n");
            String photo = p.photos.get(i);
            if (photo != null && !photo.isEmpty())
                buf.append("\t\t\t\t<photo>" + photo + "</photo>\n");
            String bio = p.bios.get(i);
            if (bio != null && !bio.isEmpty())
                buf.append("\t\t\t\t<bio>" + clean(bio) + "</bio>\n");
            buf.append("\t\t\t</authorInfo>\n");
        }
        buf.append("\t\t</paper>\n");
    }

}
