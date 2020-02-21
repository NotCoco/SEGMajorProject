package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.TableEntity;
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
    public static final String TABLENAME = "Pages";
    public static final String SLUG = "Slug";
    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SLUG)
    private String primaryKey;

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
        this.primaryKey = (String) slug;
        this.index = index;
        this.title = title;
        this.content = content;
    }

    //GETTERS AND SETTERS:

    public Serializable getPrimaryKey() {
        return primaryKey;
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
        return "Page: " + this.primaryKey + ", " + this.index + ", " + this.title + ", " + this.content;
    }

    public boolean equals(Page otherPage) {
        return getPrimaryKey().equals(otherPage.getPrimaryKey()) && (getIndex() == otherPage.getIndex()) &&
                getTitle().equals(otherPage.getTitle()) && getContent().equals(otherPage.getContent());
    }

    @Override
    public TableEntity copy(TableEntity newCopy) {
        Page newPage = (Page) newCopy;
        setContent(newPage.getContent());
        setIndex(newPage.getIndex());
        setTitle(newPage.getTitle());
        return this;
    }
}
