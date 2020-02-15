package main.java.com.projectBackEnd;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = Page.TABLENAME)
/**
 * The Page objects as defined for use, also designated entities with tables
 * in the database for Hibernate and JavaX to identify for database management.
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class Page { //TODO extends Entity, for easier Json conversion for frontend management

    // Table Headers stored as public static final Strings
    public static final String TABLENAME = "Pages";
    private static final String SLUG = "Slug";
    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SLUG)
    private String slug;

    @Column(name = INDEX)
    private Integer index;

    @Column(name = TITLE)
    private String title;

    @Column(name = CONTENT)
    private String content;
    //Attributes for each will be stored here, under the names given by @Column.

    /**
     * Empty Constructor
     */
    public Page() {
    }

    public Page(String slug, Integer index, String title, String content) {
        this.slug = new SQLSafeString(slug).toString();
        this.index = index;
        this.title = title;
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
        this.content = new SQLSafeString(content).toString();
    }

    @Override
    public String toString() {
        return "Page: " + this.slug + ", " + this.index + ", " + this.title + ", " + this.content;
    }

    public static String getCreateQuery() {
        String createQuery = "CREATE TABLE " + new SQLSafeString(TABLENAME) + " (";
        createQuery += new SQLSafeString(SLUG) + " VARCHAR(255) NOT NULL, "; //Maybe static.makeSafe? No interface...
        createQuery += new SQLSafeString(INDEX) + " INTEGER NOT NULL, ";
        createQuery += new SQLSafeString(TITLE) + " TEXT, ";
        createQuery += new SQLSafeString(CONTENT) + " TEXT, ";
        createQuery += "PRIMARY KEY (" + new SQLSafeString(SLUG) + ")";
        createQuery += ");";
        System.out.println(createQuery);
        return createQuery;
    }

    public boolean equals(Page otherPage) {
        return getSlug().equals(otherPage.getSlug()) && (getIndex() == otherPage.getIndex()) && getTitle().equals(otherPage.getTitle()) &&
                getContent().equals(otherPage.getTitle());
    }
}
