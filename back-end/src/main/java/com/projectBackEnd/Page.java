package main.java.com.projectBackEnd;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = Page.TABLENAME)
/**
 * The Page objects as defined for use, also designated entities with tables
 * in the database for Hibernate and JavaX to identify for database management.
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class Page implements TableEntity { //TODO extends Entity, for easier Json conversion for frontend management

    // Table Headers stored as public static final Strings
    public static final String TABLENAME = "Page"; //Hibernate requires this to be the same as class name
    public static final String SLUG = "Slug";
    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SLUG)
    private String slug;

    @Column(name = INDEX, nullable=false)
    private Integer index;

    @Column(name = TITLE)
    @Type(type="text")
    private String title;

    @Column(name = CONTENT)
    @Type(type="text")
    private String content;
    //Attributes for each will be stored here, under the names given by @Column.

    /**
     * Empty Constructor
     */
    public Page() {
    }

    public Page(Serializable slug, Integer index, String title, String content) {
        this.slug = (String) slug;
        this.index = index;
        this.title = title;
        this.content = content;
    }

    //GETTERS AND SETTERS:

    public Serializable getSlug() {
        return slug;
    }

    public Serializable getPrimaryKey() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
        this.content = content;
    }

    @Override
    public String toString() {
        return "Page: " + this.slug + ", " + this.index + ", " + this.title + ", " + this.content;
    }
    //TODO Below method unnecessary hibernate does it for you.
    public static String getCreateQuery() {
        String createQuery = "CREATE TABLE " + TABLENAME + " (";
        createQuery += SLUG + " VARCHAR(255) NOT NULL, "; //Maybe static.makeSafe? No interface...
        createQuery += INDEX + " INTEGER NOT NULL, ";
        createQuery += TITLE + " TEXT, ";
        createQuery += CONTENT + " TEXT, ";
        createQuery += "PRIMARY KEY (" + SLUG + ")";
        createQuery += ");";
        System.out.println(createQuery);
        return createQuery;
    }

    public boolean equals(Page otherPage) {
        return getSlug().equals(otherPage.getSlug()) && (getIndex() == otherPage.getIndex()) && getTitle().equals(otherPage.getTitle()) &&
                getContent().equals(otherPage.getContent());
    }
}
