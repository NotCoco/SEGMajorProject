package main.java.com.projectBackEnd;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAGE")
/**
 * The Page objects as defined for use, also designated entities with tables
 * in the database for Hibernate and JavaX to identify for database management.
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class Page {

    // Table Headers stored as public static final Strings
    public static final String TABLENAME = "Page";
    private static final String SLUG = "Slug";
    private static final String INDEX = "`Index`";
    private static final String CONTENT = "Content";

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SLUG)
    private String slug;

    @Column(name = INDEX)
    private Integer index;

    @Column(name = CONTENT)
    private String content;
    //Attributes for each will be stored here, under the names given by @Column.

    /**
     * Empty Constructor
     */
    public Page() {
    }

    public Page(String slug, Integer index, String content) {
        this.slug = new SQLSafeString(slug).toString();
        this.index = index;
        this.content = new SQLSafeString(content).toString();
    }

    //GETTERS AND SETTERS:

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = new SQLSafeString(slug).toString();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = new SQLSafeString(content).toString();
    }

    @Override
    public String toString() {
        return "Page: " + this.slug + ", " + this.index + ", " + this.content;
    }

    public static String getCreateQuery() {
        String createQuery = "CREATE TABLE " + new SQLSafeString(TABLENAME) + " (";
        createQuery += new SQLSafeString(SLUG) + " VARCHAR(255) NOT NULL, "; //Maybe static.makeSafe? No interface...
        createQuery += new SQLSafeString(INDEX) + " INTEGER NOT NULL, ";
        createQuery += new SQLSafeString(CONTENT) + " TEXT, ";
        createQuery += "PRIMARY KEY (" + new SQLSafeString(SLUG) + ")";
        createQuery += ");";
        System.out.println(createQuery);
        return createQuery;
    }
}
