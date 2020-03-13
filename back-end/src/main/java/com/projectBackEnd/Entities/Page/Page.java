package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = Page.TABLENAME, uniqueConstraints = { @UniqueConstraint(columnNames = {Page.SLUG, Page.SITE})})
public class Page implements TableEntity {
    public static final String TABLENAME = "Pages";
    private static final String ID = "ID";
    public static final String SITE = "Site";
    public static final String SLUG = "Slug";

    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;
    @ManyToOne(targetEntity = Site.class)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = SITE, nullable = false)
    private Site site;

    @Column(name = SLUG, nullable = false)
    @Type(type="text")
    private String slug;
    @Column(name = INDEX)
    private Integer index;
    @Column(name = TITLE)
    @Type(type="text")
    private String title;
    @Column(name = CONTENT)
    @Type(type="text")
    private String content;

    public Page() {}


    public Page(String siteName, String slug, Integer index, String title, String content) {
        this.primaryKey = -1;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getBySiteName(siteName));
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;
    }
    public Page(Site site, String slug, Integer index, String title, String content) {
        this.primaryKey = -1;
        setSite(site);
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;
    }


    //Need to find out which of these two Micronaut will use. Or just use the getName, make it print something lol
    public Page(Integer ID, String siteName, String slug, Integer index, String title, String content) {
        this.primaryKey = ID;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getBySiteName(siteName));
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;
        System.out.println("Micronaut used Constructor 1! Delete The following constructor"); //TODO REMOVE
    }
    public Page(Integer ID, Site site, String slug, Integer index, String title, String content) {
        this.primaryKey = ID;
        setSite(site);
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;
        System.out.println("Micronaut used Constructor 2! Delete The above constructor"); //TODO Remove
    }
    public Integer getPrimaryKey() {
        return primaryKey;
    }
    public TableEntity copy(TableEntity newCopy) {
        Page newPageVersion = (Page) newCopy;
        setIndex(newPageVersion.getIndex());
        setTitle(newPageVersion.getTitle());
        setContent(newPageVersion.getContent());
        setSite(newPageVersion.getSite());
        setSlug(newPageVersion.getSlug());
        return newPageVersion;
    }
    public Site getSite() {
        return site;
    }
    public void setSite(Site site) {
        this.site = site;
    }

    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
    //Integer ID, String siteName, String slug, Integer index, String title, String content
    public String toString() {
        return "Page: " + getPrimaryKey() + ", from: " + getSite() + " : " + getSlug() + " : " + getIndex() + " : " + getTitle() + " : " + getContent();
    }
}