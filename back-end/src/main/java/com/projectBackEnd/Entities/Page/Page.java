package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

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

    @Column(name = SLUG, nullable = false, unique = true)
    @Type(type="test")
    private String slug;

    @Column(name = TITLE)
    @Type(type="text")
    private String title;

    @Column(name = CONTENT)
    @Type(type="text")
    private String content;

    @Column(name = INDEX)
    private Integer index; //These can't be duplicates

    public Page() {}
    public Page(String siteName, String slug, int Index, String title, String content) {

    }
    public Page(Site siteName, String slug, int Index, String title, String content) {

    }

    //Need to find out which of these two Micronaut will use. Or just use the getName, make it print something lol
    public Page(Integer ID, String siteName, String slug, int Index, String title, String content) {

    }
    public Page(Integer ID, Site site, String slug, int Index, String title, String content) {

    }

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