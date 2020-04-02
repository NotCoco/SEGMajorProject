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
 * The have an auto-increment 'ID', a site to which they belong, a slug, an index for their position.
 * Each page has an ID, a Site to which it belongs, a Slug, an Index and a Content.
 *
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */

@Entity
@Table(name = Page.TABLENAME, uniqueConstraints = {@UniqueConstraint(columnNames = {Page.SLUG, Page.SITE})})
public class Page implements TableEntity {

    // 'Page' database table name and attributes
    public static final String TABLENAME = "Pages";
    private static final String ID = "ID";
    public static final String SITE = "Site";
    public static final String SLUG = "Slug";
    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";


    // Auto-incremented and unique primary key 'ID' in the database
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;

    // Parent site to which the Page belongs
    @ManyToOne(targetEntity = Site.class)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = SITE, nullable = false)
    private Site site;

    // Slug of the Page (for identification)
    @Column(name = SLUG, nullable = false)
    @Type(type="text")
    private String slug;

    // Index of the page : its position on the site
    @Column(name = INDEX, nullable = false)
    private Integer index;

    // Title of the Page
    @Column(name = TITLE)
    @Type(type="text")
    private String title;

    // Content of the Page
    @Column(name = CONTENT)
    @Type(type="text")
    private String content;


    /**
     * Default constructor
     */
    public Page() {}


    /**
     * Main constructor for Page object creation
     * Primary key is auto-incremented in the database
     * @param siteSlug  Slug of the parent site for this Page
     * @param slug      Slug of the Page
     * @param index     Index of the Page
     * @param title     Title of the Page
     * @param content   Content of the Page
     */
    public Page(String siteSlug, String slug, Integer index, String title, String content) {

        this.primaryKey = -1;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getSiteBySlug(siteSlug));
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;

    }

    /**
     * Constructor taking the primary key ID
     * @param ID        Primary key for this Page
     * @param siteSlug  Slug of the parent site for this Page
     * @param slug      Slug of the Page
     * @param index     Index of the Page
     * @param title     Title of the Page
     * @param content   Content of the Page
     */
    public Page(Integer ID, String siteSlug, String slug, Integer index, String title, String content) {

        this.primaryKey = ID;
        SiteManagerInterface s = SiteManager.getSiteManager();
        setSite(s.getSiteBySlug(siteSlug));
        setSlug(slug);
        this.index = index;
        this.title = title;
        this.content = content;

    }


    /**
     * Get primary key ID for this object
     * @return primary key
     */
    public Integer getPrimaryKey() {
        return primaryKey;
    }


    /**
     * Get Site to which the Page belongs to
     * @return site
     */
    public Site getSite() {
        return site;
    }


    /**
     * Set Site to which the Page belongs to input Site
     * @param site Parent site of the Page
     */
    public void setSite(Site site) {
        this.site = site;
    }


    /**
     * Get the slug of the Page
     * @return slug value
     */
    public String getSlug() {
        return slug;
    }


    /**
     * Set the slug of the Page to the input slug
     * @param slug New slug value
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }


    /**
     * Get the title of the page
     * @return title
     */
    public String getTitle() {
        return title;
    }


    /**
     * Set the title of the page as the input title value
     * @param title New title value
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Get content of the Page
     * @return content
     */
    public String getContent() {
        return content;
    }


    /**
     * Set content of the Page as the input content value
     * @param content New content value
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * Get the index of the Page
     * @return index
     */
    public Integer getIndex() {
        return index;
    }


    /**
     * Set the index of the Page as the input index value
     * @param index New index value
     */
    public void setIndex(Integer index) {
        this.index = index;
    }


    /**
     * Copy the values of the input TableEntity object
     * @param toCopy   Page to copy
     * @return updated Page object
     */
    @Override
    public TableEntity copy(TableEntity toCopy) {

        Page newPageVersion = (Page) toCopy;
        setIndex(newPageVersion.getIndex());
        setTitle(newPageVersion.getTitle());
        setContent(newPageVersion.getContent());
        setSite(newPageVersion.getSite());
        setSlug(newPageVersion.getSlug());
        return newPageVersion;

    }

}