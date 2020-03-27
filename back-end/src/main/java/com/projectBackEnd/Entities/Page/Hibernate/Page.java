package main.java.com.projectBackEnd.Entities.Page.Hibernate;

import main.java.com.projectBackEnd.Entities.Site.Hibernate.Site;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManagerInterface;
import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Page objects are database entities for the table 'Page' defined in this class.
 * The have an auto-increment 'ID', a site to which they belong, a slug, an index for their position
 * within the site, a title, and some content.
 *
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
@Entity
@Table(name = Page.TABLENAME, uniqueConstraints = {@UniqueConstraint(columnNames = {Page.SLUG, Page.SITE})})
public class Page implements TableEntity {

    // Table columns

    public static final String TABLENAME = "Pages";
    private static final String ID = "ID";
    public static final String SITE = "Site";
    public static final String SLUG = "Slug";
    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";

    // Private fields

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

    @Column(name = INDEX, nullable = false)
    private Integer index;

    @Column(name = TITLE)
    @Type(type="text")
    private String title;

    @Column(name = CONTENT)
    @Type(type="text")
    private String content;

    /**
     * Constructors :
     * Empty, constructor using siteName, constructor user site, and constructor requiring primary key
     */
    public Page() {}

    public Page(String siteSlug, String slug, Integer index, String title, String content) {
        this.primaryKey = -1;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getBySiteSlug(siteSlug));
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;
    }

    public Page(Integer ID, String siteSlug, String slug, Integer index, String title, String content) {
        this.primaryKey = ID;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getBySiteSlug(siteSlug));
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;
    }

    /**
     * Getters and setters
     * Primary Key cannot be changed in the database
     */

    public Integer getPrimaryKey() {
        return primaryKey;
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

    /**
     * Copy the values of the input TableEntity object
     * @param newCopy
     * @return updated Page object
     */
    @Override
    public TableEntity copy(TableEntity newCopy) {
        Page newPageVersion = (Page) newCopy;
        setIndex(newPageVersion.getIndex());
        setTitle(newPageVersion.getTitle());
        setContent(newPageVersion.getContent());
        setSite(newPageVersion.getSite());
        setSlug(newPageVersion.getSlug());
        return newPageVersion;
    }

}