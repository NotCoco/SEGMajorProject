package main.java.com.projectBackEnd.Entities.Site;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = Site.TABLENAME)
public class Site implements TableEntity {
    public static final String TABLENAME = "Sites";
    public static final String ID = "ID";
    private static final String SITENAME = "Name";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;

    @Type(type="text")
    @Column(name = SITENAME, nullable=false, unique=true)
    private String name;

    public Site() {}
    public Site(String siteName) {
        this.primaryKey = -1;
        this.name = siteName;
    }
    public Site(Integer ID, String siteName) {
        this.primaryKey = ID;
        this.name = siteName;
    }

    @Override
    public String toString() {
        return "Site: " + primaryKey +", Name: " + name;
    }

    public boolean equals(Site otherSite) {
        return getPrimaryKey().equals(otherSite.getPrimaryKey()) && getName().equals(otherSite.getName());
    }

    @Override
    public TableEntity copy(TableEntity toCopy) {
        Site siteToCopy = (Site) toCopy;
        setName(siteToCopy.getName());
        return siteToCopy;
    }

    @Override
    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
