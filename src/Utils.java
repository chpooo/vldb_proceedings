import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class Utils
{
    public static void copy(String source, String destination)
    {
        try
        {

            File f1 = new File(source);
            File f2 = new File(destination);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void append(String source, String destination)
    {
        try
        {

            File f1 = new File(source);
            File f2 = new File(destination);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2,true);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
