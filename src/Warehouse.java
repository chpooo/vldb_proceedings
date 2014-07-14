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

public class Warehouse {

    
    /*
    
    int[][] periods;
    HashMap<String, Session> sessions = new HashMap<String, Session>();


    

    
    
    void printPeriod(int period, StringBuffer buf) {
        
    }
    
    if (args.length > 0 && args[0].equalsIgnoreCase("print"))
    {

        System.out.print("Generating PDF..");

        // Setup input and output files
        File xmlfile = new File("FullProgram.xml");
        File xsltfile = new File("PrintProgramPdf.xsl");
        File pdffile = new File("PrintProgram.pdf");
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();

        // create a new document
        Document document = new Document();
        PdfWriter pdfWriter = null;

        try
        {
            StringBuilder xslt = new StringBuilder();
            BufferedReader in = new BufferedReader(new FileReader(xsltfile));
            String line = null;
            while ((line = in.readLine()) != null)
            {
                if (line.startsWith("<xsl:output"))
                {
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

            // get Instance of the PDFWriter // Setup output
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdffile));

            // document header attributes
            document.addAuthor("dbTrento");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("dbTrento");
            document.addTitle("VLDB");
            document.setPageSize(PageSize.A4);

            // open document
            document.open();

            // To convert a HTML file from the filesystem use FIS
            InputStreamReader fis = new InputStreamReader(new ByteArrayInputStream(htmlStream.toByteArray()));
            // InputStreamReader fis = new InputStreamReader(new
            // FileInputStream("dbTrento.html"));
            // get the XMLWorkerHelper Instance
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            // convert to PDF
            worker.parseXHtml(pdfWriter, document, fis);

        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        finally
        {
            // close the document
            document.close();
            // close the writer
            if (pdfWriter != null)
                pdfWriter.close();
        }
    
    
    */
}