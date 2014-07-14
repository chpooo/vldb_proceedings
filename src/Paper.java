import java.util.ArrayList;

public class Paper
{
    public int id;

    public String link;

    public String title;

    public String authors;

    public String paperAbstract;

    public ArrayList<String> photos;

    public ArrayList<String> bios;

    public Paper(int id, String link, String title, String authors, String paperAbstract,
            ArrayList<String> photos, ArrayList<String> bios)
    {
        this.id = id;
        this.link = link;
        this.title = title;
        this.paperAbstract = paperAbstract;
        this.photos = photos;
        this.bios = bios;
        this.authors = authors;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append(id + ". " + title + "\nby " + authors + "\n");
        if (link != null && !link.isEmpty())
            buf.append("Downloadable at: " + link + "\n");
        if (paperAbstract != null && !paperAbstract.isEmpty())
            buf.append("Abstract:\n" + paperAbstract);
        /*
         * for (int i = 0, imax = authorData.length; i < imax; i++) {
         * buf.append("\t\t\t<authorInfo>\n"); if (authorData[i][PHOTO] != null
         * && !authorData[i][PHOTO].isEmpty()) buf.append("\t\t\t\t<photo>" +
         * authorData[i][PHOTO] + "</photo>\n"); if (authorData[i][BIO] != null
         * && !authorData[i][BIO].isEmpty()) buf.append("\t\t\t\t<bio>" +
         * authorData[i][BIO] + "</bio>\n");
         * buf.append("\t\t\t</authorInfo>\n"); }
         */
        if (photos.size() != 0)
            buf.append("+ author photos and bios\n");
        return buf.toString();
    }
}
