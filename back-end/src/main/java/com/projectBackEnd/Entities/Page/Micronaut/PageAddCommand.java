package main.java.com.projectBackEnd.Entities.Page.Micronaut;

import javax.validation.constraints.NotNull;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManagerInterface;
//import io.micronaut.core.annotation.Introspected;

/**
 * PageAddCommand is an implementation of the Command design pattern.
 * It creates mock Page objects and reduced memory use.
 * It is used by the controller to insert a Page object into the db.
 */

//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class PageAddCommand {

    private String site;
    @NotNull
    private String slug;
    @NotNull
    private String index;
    private String title;
    private String content;

    public PageAddCommand(){

    }

    public PageAddCommand(String site, String slug, Integer index, String title, String content){
        this.slug = slug;
        setIndex(index);
        this.title = title;
        this.content = content;
        setSite(site);
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        SiteManagerInterface siteManager = SiteManager.getSiteManager();
        if (siteManager.getBySiteSlug(site) != null) this.site = site;
    }

}
