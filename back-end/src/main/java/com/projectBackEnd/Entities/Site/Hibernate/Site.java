package main.java.com.projectBackEnd.Entities.Site.Hibernate;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
/**
 * Site objects are database entities for the table 'Site' defined in this class.
 * They can be considered as a category of pages, one site is created for each condition.
 * The have two attributes :
 *    - auto-increment primary key as 'ID' in table
 *    - site name as 'Name'.
 *
 *    https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
@Entity
@Table(name = Site.TABLENAME)
public class Site implements TableEntity {

    // Table columns
    public static final String TABLENAME = "Sites";
    public static final String ID = "ID";
    public static final String SITENAME = "Name";
    public static final String SITESLUG = "Slug";

    // Private fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;

    @Type(type="text")
    @Column(name = SITENAME, nullable=false)
    private String name;

    @Type(type="text")
    @Column(name = SITESLUG, nullable=false, unique=true)
    private String slug;

    public Site() {}

    public Site(String siteSlug, String siteName) {
        this.primaryKey = -1;
        this.name = siteName;
        setSlug(siteSlug);
    }

    public Site(Integer ID, String siteSlug, String siteName) {
        this.primaryKey = ID;
        this.name = siteName;
        setSlug(siteSlug);
    }

    /**
     * Getters and setters
     * Primary Key cannot be changed (auto-increment)
     */

    @Override
    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
        SiteManagerInterface checker = SiteManager.getSiteManager();
        Site found = checker.getSiteBySlug(slug);
        if (found != null && checker.getByPrimaryKey(primaryKey) == null ) this.slug = null;

    }

    public String getSlug() { return slug; }

     /**
     * Copy the values of the input site object
     * @param toCopy
     * @return updated site object
     */
    @Override
    public TableEntity copy(TableEntity toCopy) {
        Site siteToCopy = (Site) toCopy;
        setName(siteToCopy.getName());
        setSlug(siteToCopy.getSlug());
        return siteToCopy;
    }


}
