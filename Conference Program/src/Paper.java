
public class Paper
{
    public int id;
    public String link;
    public String title;
    public String authors;
    public String paperAbstract;
    public String[][] authorData;
    public static int PHOTO = 0;
    public static int BIO = 1;
    
    public Paper(int id, String link, String title, String authors, String paperAbstract, String[][] authorData)
    {
        this.id = id;
        this.link = link;
        this.title = title;
        this.paperAbstract = paperAbstract;
        this.authorData = authorData;
        this.authors = authors;
    }

    public void print(StringBuffer buf)
    {
        buf.append("\t\t<paper>\n");
        buf.append("\t\t\t<id>" + id + "</id>\n");
        if (link != null && !link.isEmpty())
            buf.append("\t\t\t<link>" + link + "</link>\n");
        buf.append("\t\t\t<title>" + title + "</title>\n");
        buf.append("\t\t\t<authors>" + authors + "</authors>\n");
        if (paperAbstract != null && !paperAbstract.isEmpty())
            buf.append("\t\t\t<abstract>" + paperAbstract + "</abstract>\n");
        for (int i = 0, imax = authorData.length; i < imax; i++)
        {
            buf.append("\t\t\t<authorInfo>\n");
            if (authorData[i][PHOTO] != null && !authorData[i][PHOTO].isEmpty())
                buf.append("\t\t\t\t<photo>" + authorData[i][PHOTO] + "</photo>\n");
            if (authorData[i][BIO] != null && !authorData[i][BIO].isEmpty())
                buf.append("\t\t\t\t<bio>" + authorData[i][BIO] + "</bio>\n");
            buf.append("\t\t\t</authorInfo>\n");
        }
        buf.append("\t\t</paper>\n");

    }
}
