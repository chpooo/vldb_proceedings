import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class CPGenT
{

    Conference conference = new Conference();

    ArrayList<Room> rooms = new ArrayList<Room>();

    ArrayList<String> days = new ArrayList<String>();

    List<Period> timeperiods = new ArrayList<Period>();

    HashMap<String, Session> sessions = new HashMap<String, Session>();

    LinkedHashMap<String, ArrayList<Sponsor>> sponsors = new LinkedHashMap<String, ArrayList<Sponsor>>();

    public CPGenT(String inputFileName)
    {
        System.out.println("Importing the raw data ... ");
        new XLSImporter("FullProgram.xls", conference, rooms, days, timeperiods, sessions, sponsors);
        System.out.println("Data Imported.");
        System.out.print("Generating the XML ..");
        new ProgramGenerator("FullProgram.xml", conference, rooms, days, (ArrayList<Period>) timeperiods, sessions);
        System.out.println("done");
        System.out.print("Generating the Menu ...");
        new MenuGenerator("Menu.html", conference, rooms, days, (ArrayList<Period>) timeperiods, sessions, sponsors);
        System.out.println("done");
        System.out.print("Generating the Doors Menu ...");
        new DoorProgramGenerator("DoorsMenu.html", conference, rooms, days, (ArrayList<Period>) timeperiods, sessions, sponsors);
        System.out.println("done");

    }



    public static void main(String[] args)
    {
        CPGenT tmp = new CPGenT("FullProgram");
    }

}
