package main.java.com.projectBackEnd.Entities.OldPage;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = OldPage.TABLENAME)
/**
 * The OldPage objects as defined for use, also designated entities with tables
 * in the database for Hibernate and JavaX to identify for database management.
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class OldPage implements TableEntity { //TODO extends Entity, for easier Json conversion for frontend management

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
    public OldPage() {
    }

    public OldPage(Serializable slug, Integer index, String title, String content) {
        this.primaryKey = (String) slug;
        this.index = index;
        this.title = title;
        this.content = content;
    }

    //GETTERS AND SETTERS:

    public String getPrimaryKey() {
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
        return "OldPage: " + this.primaryKey + ", " + this.index + ", " + this.title + ", " + this.content;
    }

    public boolean equals(OldPage otherOldPage) {
        return getPrimaryKey().equals(otherOldPage.getPrimaryKey()) && (getIndex().equals(otherOldPage.getIndex())) &&
                getTitle().equals(otherOldPage.getTitle()) && getContent().equals(otherOldPage.getContent());
    }

    @Override
    public TableEntity copy(TableEntity newCopy) {
        OldPage newOldPage = (OldPage) newCopy;
        setContent(newOldPage.getContent());
        setIndex(newOldPage.getIndex());
        setTitle(newOldPage.getTitle());
        return this;
    }
}
