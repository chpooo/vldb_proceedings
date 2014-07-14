import java.io.*;
import java.util.*;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class XLSImporter {

    public int ID = 0;

    String[][] days;

    public int _DAYNAME = 0;

    public int _DAYNUM = 1;

    ArrayList<Room> locations = new ArrayList<Room>();
    
    //String[] rooms;

    //String[] maps;

    int[][] periods;

    HashMap<String, Session> sessions = new HashMap<String, Session>();


    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.print("Generating XML..");
        StringBuffer buf = new StringBuffer();
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buf.append("<?xml-stylesheet type=\"text/xsl\" href=\"./lib/PrintProgramPdf.xsl\"?>\n");
        buf.append("<program xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        XLSImporter _importer = new XLSImporter();
        try {
            _importer.generateContent("FullProgram.xls", buf);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        buf.append("\n</program>");
        if (args.length == 0 || !args[0].equalsIgnoreCase("print")) {
        try {
            String filename = "FullProgram.xml";
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(filename, false), "UTF-8");
            // the true will append the new data
            fw.write(buf.toString());// appends the string to the file
            fw.close();
            System.out.println("done");
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("print")) {

            System.out.print("Generating PDF..");

            // Setup input and output files
            File xmlfile = new File("FullProgram.xml");
            File xsltfile = new File("PrintProgramPdf.xsl");
            File pdffile = new File("PrintProgram.pdf");
            ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();

            //create a new document
            Document document = new Document();
            PdfWriter pdfWriter = null;

            try {
                StringBuilder xslt = new StringBuilder();
                BufferedReader in = new BufferedReader(new FileReader(xsltfile));
                String line = null;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("<xsl:output")) {
                        line = line.replace("method=\"html\"", "method=\"xml\"");
                    }
                    xslt.append(line).append("\n");
                }
                in.close();
                StringReader xslt_reader = new StringReader(xslt.toString());

                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xslt_reader));

                // Set the value of a <param> in the stylesheet
                transformer.setParameter("versionParam", "2.0");

                // Setup input for XSLT transformation
                Source xmlSource = new StreamSource(xmlfile);
                Result result = new StreamResult(htmlStream);
                transformer.transform(xmlSource, result);

                //get Instance of the PDFWriter // Setup output
                pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdffile));

               //document header attributes
               document.addAuthor("dbTrento");
               document.addCreationDate();
               document.addProducer();
               document.addCreator("dbTrento");
               document.addTitle("VLDB");
               document.setPageSize(PageSize.A4);

               //open document
               document.open();

               //To convert a HTML file from the filesystem use FIS
               InputStreamReader fis = new InputStreamReader(new ByteArrayInputStream(htmlStream.toByteArray()));
               //InputStreamReader fis = new InputStreamReader(new FileInputStream("dbTrento.html"));
               //get the XMLWorkerHelper Instance
               XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
               //convert to PDF
               worker.parseXHtml(pdfWriter, document, fis);

            } catch (Exception e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            } finally {
                //close the document
               document.close();
               //close the writer
               if (pdfWriter != null) pdfWriter.close();
            }
            System.out.println("done");

        }
    }

    public void generateContent(String file, StringBuffer buf) throws Exception {
        WorkbookSettings wbs = new WorkbookSettings();
        wbs.setEncoding("cp1252"); //
        Workbook workbook = Workbook.getWorkbook(new File(file), wbs);
        readDays(workbook);
        readRooms(workbook);
        readPeriods(workbook);
        readSessions(workbook);
        readPapers(workbook);
        formatContent(buf);
    }

    private void formatContent(StringBuffer buf) {
        // Print the program at a glance
        // TBDone
        // Print the periods over all the days.
        for (int i = 0, imax = periods.length; i < imax; i++) {
            printPeriod(i, buf);
        }
    }

    void printPeriod(int period, StringBuffer buf) {
        int day = periods[period][0];
        String dayStr = days[day][0] + " " + days[day][1];
        int from = periods[period][1];
        int fromHour = (from / 100);
        int fromMin = from - (fromHour * 100);
        String fromStr = ((fromHour < 10) ? "0" + fromHour : fromHour) + ":"
                + ((fromMin < 10) ? "0" + fromMin : fromMin);
        int to = periods[period][2];
        int toHour = (to / 100);
        int toMin = to - (toHour * 100);
        String toStr = ((toHour < 10) ? "0" + toHour : toHour) + ":" + ((toMin < 10) ? "0" + toMin : toMin);

        buf.append("<period>\n");
        buf.append("\t<id>" + period + "</id>\n");
        buf.append("\t<name>" + dayStr + " " + fromStr + "-" + toStr + "</name>\n");
        // Generate the sessions
        ArrayList<Session> sorted = new ArrayList<Session>(sessions.values());
        Collections.sort(sorted);
        for (Session s : sorted) {
            if (s.periods.contains(period))
                s.print(buf, period);
        }
        buf.append("</period>\n");
    }


    void readDays(Workbook workbook) {
        Sheet sheet = workbook.getSheet("Days");
        int imax = sheet.getRows() - 1;
        days = new String[imax][2];
        for (int i = 0; i < imax; i++) {
            days[i][_DAYNAME] = sheet.getCell(0, i + 1).getContents();
            days[i][_DAYNUM] = sheet.getCell(1, i + 1).getContents();
        }
    }

    void readPeriods(Workbook workbook) {
        Sheet sheet = workbook.getSheet("Slots");
        int imax = sheet.getRows();
        int numOfPeriods = 0;
        for (int i = 0; i < imax; i++) {
            String tmp = sheet.getCell(0, i).getContents();
            if (tmp.equals("EOF"))
                break;
            numOfPeriods++;
        }
        numOfPeriods -= 2;
        periods = new int[numOfPeriods][3];
        for (int i = 0; i < periods.length; i++) {
            periods[i][0] = Integer.parseInt(sheet.getCell(0, i + 2).getContents()) - 1;
            periods[i][1] = Integer.parseInt(sheet.getCell(1, i + 2).getContents());
            periods[i][2] = Integer.parseInt(sheet.getCell(2, i + 2).getContents());
        }
    }

    void readRooms(Workbook workbook) {
        Sheet sheet = workbook.getSheet("Slots");
        int imax = sheet.getColumns();
        int numOfRooms = 0;
        for (int i = 0; i < imax; i++) {
            String tmp = sheet.getCell(i, 1).getContents();
            if (tmp.equals("EOF"))
                break;
            numOfRooms++;
        }
        numOfRooms -= 3;
        //rooms = new String[numOfRooms];
        //maps = new String[numOfRooms];
        for (int i = 0; i < numOfRooms; i++) {
            String room = sheet.getCell(i + 3, 1).getContents();
            String map = sheet.getCell(i + 3, 0).getContents();
            //rooms[i] = room;
            //maps[i] = map;
            Room location = new Room(room,map);
            locations.add(location);
        }
    }

    void readSessions(Workbook workbook) {
        Sheet sessions_sheet = workbook.getSheet("Sessions");
        for (int i = 1; i < sessions_sheet.getRows(); i++) {
            String name = sessions_sheet.getCell(0, i).getContents();
            String title = sessions_sheet.getCell(1, i).getContents();
            if (name.equals("EOF"))
                break;
            sessions.put(name, new Session(sessions.size(), name, title));
        }

        Sheet slots_sheet = workbook.getSheet("Slots");
        Sheet chair_sheet = workbook.getSheet("Chairs");
        for (int i = 0; i < periods.length; i++)
            for (int j = 0; j < locations.size(); j++) {
                String sessionName = slots_sheet.getCell(j + 3, i + 2).getContents();
                String chair = chair_sheet.getCell(j + 3, i + 2).getContents().replaceAll("&", "&amp;");
                if (sessionName != null && !sessionName.isEmpty()) {
                    Session session = sessions.get(sessionName);
                    session.chair = chair;
                    session.periods.add(i);
                    session.rooms.put(i,j);
                    session.room_name = locations.get(j).name;//rooms[j];
                    session.map = locations.get(j).map; //maps[j];
                }
            }
    }

    private void readPapers(Workbook workbook) {
        Sheet sheet = workbook.getSheet("Papers");
        int imax = sheet.getRows();
        Vector<Paper> papers = new Vector<Paper>();
        for (int i = 1; i < imax; i++) {
            String sessionName = sheet.getCell(0, i).getContents();
            if (sessionName.equals("EOF"))
                break;
            Session session = sessions.get(sessionName);
            if (session == null)
                throw new RuntimeException("Cannot find session " + sessionName + " in the list of sessions");

            int id = Integer.parseInt(sheet.getCell(1, i).getContents());
            String link = sheet.getCell(2, i).getContents();
            String title = sheet.getCell(3, i).getContents();
            title = title.replaceAll("&", "&amp;");
            String authors = sheet.getCell(4, i).getContents();
            authors = fixAuthorList(authors);
            String abstractTxt = sheet.getCell(5, i).getContents();
            abstractTxt = abstractTxt.replaceAll("&", "&amp;");
            Vector<String> photos = new Vector<String>();
            Vector<String> bios = new Vector<String>();
            for (int k = 6, kmax = sheet.getColumns(); k < (kmax-1); ) {
                String photo = sheet.getCell(k, i).getContents();
                String bio = sheet.getCell(k + 1, i).getContents();
                bio = bio.replaceAll("&", "&amp;");
                if (photo.equals(""))
                    break;
                photos.add(photo);
                bios.add(bio);
                k += 2;
            }
            String[][] authorInfo = new String[photos.size()][2];
            for (int k = 0; k < photos.size(); k++) {
                authorInfo[k][0] = photos.elementAt(k);
                authorInfo[k][1] = bios.elementAt(k);
            }
            session.papers.add(new Paper(id, link, title, authors, abstractTxt, authorInfo));
        }
    }

    private String fixAuthorList(String authors) {
        // remove the * 
        //authors = authors.replaceAll("\\*", "");

        // check if we are in the bad format. If not we are fine. 
        if (authors.indexOf(";") != -1) {
            if (authors.endsWith(", "))
                authors = authors.substring(0, authors.length() - 2) + "$$$$$$$";
            authors = authors.replaceAll(", ;", "@@@@@@@");
            authors = authors.replaceAll(", ", " (");
            authors = authors.replaceAll(";", "),");
            //TODO:fix that
            authors = authors.replaceAll("@@@@@@@", ",");
            authors = authors.replaceAll("$$$$$$$", ")");
        }

        authors = authors.replaceAll("  \\)", ")");
        authors = authors.replaceAll(" \\)", ")");
        authors = authors.replaceAll(" \\(\\)", "");

        authors = authors.replaceAll("&", "&amp;");

        return authors;
    }

}