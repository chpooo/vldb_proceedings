import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class XLSImporter
{

    Workbook workbook;

    Conference conference = null;
    // public int ID = 0;

    ArrayList<Room> rooms = null;

    ArrayList<String> days = null;

    List<Period> timeperiods = null;

    HashMap<String, Session> sessions = null;

    LinkedHashMap<String, ArrayList<Sponsor>> sponsors = null;


    public XLSImporter(String excelFileName, Conference conference, ArrayList<Room> rooms, ArrayList<String> days,
            List<Period> timeperiods, HashMap<String, Session> sessions, LinkedHashMap<String, ArrayList<Sponsor>> sponsors)
    {
        this.rooms = rooms;
        this.days = days;
        this.sessions = sessions;
        this.timeperiods = timeperiods;
        this.conference = conference;
        this.sponsors = sponsors;

        try
        {
            readProgram(excelFileName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    void readSessions()
    {
        Sheet sessions_sheet = workbook.getSheet("Sessions");
        for (int si = 1; si < sessions_sheet.getRows(); si++)
        {
            String name = sessions_sheet.getCell(0, si).getContents();
            String title = sessions_sheet.getCell(1, si).getContents();
            String kind = sessions_sheet.getCell(2, si).getContents();
            if (name.equals("EOF"))
                break;
            Session session = new Session(name, title, kind);
            sessions.put(name, session);
            // And now read also the papers of that session
            //if (name.startsWith("Demo"))
            //    System.out.println("");
            Sheet papers_sheet = workbook.getSheet("Papers");
            for (int pi = 1, pimax = papers_sheet.getRows(); pi < pimax; pi++)
            {
                String currSession = papers_sheet.getCell(0, pi).getContents();
                if (currSession.equals("EOF"))
                    break;
                if (!currSession.equals(name))
                    continue;
                int pid = Integer.parseInt(papers_sheet.getCell(1, pi).getContents());
                String plink = papers_sheet.getCell(2, pi).getContents();
                String ptitle = papers_sheet.getCell(3, pi).getContents();
                String pabstract = papers_sheet.getCell(5, pi).getContents();
                String pauthors = fixAuthorList(papers_sheet.getCell(4, pi).getContents());
                ArrayList<String> photos = new ArrayList<String>();
                ArrayList<String> bios = new ArrayList<String>();
                for (int k = 6, kmax = papers_sheet.getColumns(); k < (kmax - 1);)
                {
                    String photo = papers_sheet.getCell(k, pi).getContents();
                    String bio = papers_sheet.getCell(k + 1, pi).getContents();
                    if (photo.equals("") && bio.equals(""))
                        break;
                    photos.add(photo);
                    bios.add(bio);
                    k += 2;
                }
                Paper tmpPaper = new Paper(pid, plink, ptitle, pauthors, pabstract, photos, bios);
                session.papers.add(tmpPaper);
            }
        }
    }

    void readDays()
    {
        Sheet sheet = workbook.getSheet("Days");
        int imax = sheet.getRows();
        for (int i = 0; i < imax; i++)
        {
            String day = sheet.getCell(0, i).getContents();
            if (day.equals("EOF"))
                break;
            days.add(day);
        }
    }

    void readRooms()
    {
        Sheet sheet = workbook.getSheet("Slots");
        int imax = sheet.getColumns();
        for (int i = 3; i < imax; i++)
        {
            String room = sheet.getCell(i, 1).getContents();
            if (room.equals("EOF"))
                break;
            String map = sheet.getCell(i, 0).getContents();
            Room location = new Room(room, map, i - 3);
            rooms.add(location);
        }
    }

    void readPeriods()
    {
        Sheet sheet = workbook.getSheet("Slots");
        for (int i = 2, imax = sheet.getRows(); i < imax; i++)
        {
            String tmp = sheet.getCell(0, i).getContents();
            if (tmp.equals("EOF"))
                break;
            int day = Integer.parseInt(sheet.getCell(0, i).getContents()) - 1;
            int start = Integer.parseInt(sheet.getCell(1, i).getContents());
            int end = Integer.parseInt(sheet.getCell(2, i).getContents());
            Period period = new Period(day, start, end);
            timeperiods.add(period);
            // Now we read the slots for that period
            readSlotsInLine(period.slots, i);
            // and sort these slots
            Collections.sort(period.slots);
        }

        // Sort the time periods
        Collections.sort(timeperiods);
    }

    private void readSlotsInLine(List<Slot> slots, int line)
    {
        Sheet slotSheet = workbook.getSheet("Slots");
        int imax = rooms.size() + 3;
        for (int i = 3; i < imax; i++)
        {
            String session = slotSheet.getCell(i, line).getContents();
            if ((session == null) || (session.trim().equals("")))
                continue;
            String chair = workbook.getSheet("Chairs").getCell(i, line).getContents();
            if ((chair != null) && (chair.trim().equals("")))
                chair = null;
            Slot slot = new Slot(rooms.get(i - 3), session, chair);
            slots.add(slot);
        }
    }

    public void readProgram(String file) throws Exception
    {
        WorkbookSettings wbs = new WorkbookSettings();
        wbs.setEncoding("cp1252"); //
        workbook = Workbook.getWorkbook(new File(file), wbs);
        
        Sheet sheet = workbook.getSheet("Main");
        conference.name = sheet.getCell(1, 0).getContents();
        conference.credits = sheet.getCell(1, 1).getContents();
        
        // Read the Contents
        readSessions();
        // Read the program
        readDays();
        readRooms();
        readPeriods();
        readSponsors();
    }

    private void readSponsors()
    {
        ArrayList<Sponsor> list = null;
        Sheet sheet = workbook.getSheet("Sponsors");
        for (int i = 1, imax = sheet.getRows(); i < imax; i++)
        {
            String type = sheet.getCell(0, i).getContents();
            if (type.equals("EOF"))
                break;
            String name = sheet.getCell(1, i).getContents();
            // If it is a type declaration
            if (name.trim().equals(""))
            {
                list = sponsors.get(type);
                if (list == null) 
                { 
                    list = new ArrayList<Sponsor>();
                    sponsors.put(type, list);
                }
                continue;
            }
            // now we are sure we have no type so we read the rest of the fields
            String image = sheet.getCell(2, i).getContents();
            int height = Integer.parseInt(sheet.getCell(3, i).getContents());
            String link = sheet.getCell(4, i).getContents();
            list.add(new Sponsor(name, image, height, link));
        }

        // Sort the time periods
        Collections.sort(timeperiods);
    }
    

    private String fixAuthorList(String authors)
    {
        // remove the *
        // authors = authors.replaceAll("\\*", "");

        // check if we are in the bad format. If not we are fine.
        if (authors.indexOf(";") != -1)
        {
            if (authors.endsWith(", "))
                authors = authors.substring(0, authors.length() - 2) + "$$$$$$$";
            authors = authors.replaceAll(", ;", "@@@@@@@");
            authors = authors.replaceAll(", ", " (");
            authors = authors.replaceAll(";", "),");
            // TODO:fix that
            authors = authors.replaceAll("@@@@@@@", ",");
            authors = authors.replaceAll("$$$$$$$", ")");
        }

        authors = authors.replaceAll("  \\)", ")");
        authors = authors.replaceAll(" \\)", ")");
        authors = authors.replaceAll(" \\(\\)", "");

        return authors;
    }

}