package main.java.com.projectBackEnd.Entities.Page;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class PageAddCommand {

    private Site site;
    @NotNull
    private String slug;
    @NotNull
    private String index;
    private String title;
    private String content;

    public PageAddCommand(){

    }

    public PageAddCommand(String siteName, String slug, Integer index, String title, String content){
        this.slug = slug;
        setIndex(index);
        this.title = title;
        this.content = content;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getBySiteName(siteName));
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getIndex() {
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException n) {
        return null; }
    }

    public void setIndex(Integer index) {
        this.index = index.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}