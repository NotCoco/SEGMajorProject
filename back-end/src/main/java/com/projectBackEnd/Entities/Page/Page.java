package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.TableEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = Page.TABLENAME)
public class Page implements TableEntity {
    public static final String TABLENAME = "Pages";
    private static final String SLUG = "Slug";
    private static final String ID = "ID";
    private static final String INDEX = "`Index`";
    private static final String TITLE = "Title";
    private static final String CONTENT = "Content";
    private static final String SITE = "Site";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;

    @ManyToOne
    @JoinColumn(name = SITE, referencedColumnName = Site.SITENAME)
    private Site site;


    public Integer getPrimaryKey() {
        return primaryKey;
    }
    public TableEntity copy(TableEntity newCopy) {
        return null;
    }

}

/*
    public OldPage(Serializable slug, Integer index, String title, String content) {
        this.primaryKey = (String) slug;
        this.index = index;
        this.title = title;
        this.content = content;
    }

 */