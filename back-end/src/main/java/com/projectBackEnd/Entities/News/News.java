package main.java.com.projectBackEnd.Entities.News;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = News.TABLENAME)
public class News implements TableEntity{


        // Table columns (attributes)
    public static final String TABLENAME = "News";
    private static final String ID = "ID";
    private static final String DATE = "Date";
    private static final String PINNED = "Pinned";
    private static final String DESCRIPTION = "Description";
    private static final String TITLE = "Title";
    private static final String URGENT = "Urgent";
    private static final String CONTENT = "Content";
    private static final String SLUG = "Slug";

        // Private Fields : primaryKey, name, type
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;



    @NotNull
    @Temporal(TemporalType.DATE) //Change here if unsuitable.
    @Column(name = DATE, nullable = false)
    private Date date;

    @NotNull
    @Type(type="text")
    @Column(name = DESCRIPTION, nullable = false)
    private String description;
    @NotNull
    @Type(type="text")
    @Column(name = CONTENT, nullable = false)
    private String content;
    @NotNull
    @Type(type="text")
    @Column(name = SLUG, nullable = false, unique = true)
    private String slug;
    @NotNull
    @Type(type="text")
    @Column(name = TITLE, nullable = false)
    private String title;

    @NotNull
    @Column(name = URGENT, nullable = false)
    private boolean urgent;
    @NotNull
    @Column(name = PINNED, nullable = false)
    private boolean pinned;

    public News() {}

    public News(Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug) {
        this.primaryKey = -1;
        this.date = date;
        this.pinned = pinned;
        this.description = description;
        this.title = title;
        this.urgent = urgent;
        this.content = content;
        this.slug = slug;
    }
    public News(Integer primaryKey, Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug) {
        this.date = date;
        this.pinned = pinned;
        this.description = description;
        this.title = title;
        this.urgent = urgent;
        this.content = content;
        this.slug = slug;
        this.primaryKey = primaryKey;
    }

    @Override
    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isUrgent() { //Might need to become getUrgent since Jackson conversion
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
    @Override
    public TableEntity copy(TableEntity toCopy) {
        News newsToCopy = (News) toCopy;
        setDate(newsToCopy.getDate());
        setDescription(newsToCopy.getDescription());
        setContent(newsToCopy.getContent());
        setPinned(newsToCopy.isPinned());
        setTitle(newsToCopy.getTitle());
        setUrgent(newsToCopy.isUrgent());
        setSlug(newsToCopy.getSlug());
        return this;
    }

}
