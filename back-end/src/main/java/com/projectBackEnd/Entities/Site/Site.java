package main.java.com.projectBackEnd.Entities.Site;

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

    // Private fields
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

    public Site(int ID, String siteName) {
        this.primaryKey = ID;
        this.name = siteName;
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

    @Override
    public String toString() {
        return "Site: " + primaryKey +", Name: " + name;
    }

     /**
     * Copy the values of the input site object
     * @param toCopy
     * @return updated site object
     */
    @Override
    public TableEntity copy(TableEntity toCopy) {
        Site siteToCopy = (Site) toCopy;
        setName(siteToCopy.getName());
        return siteToCopy;
    }


}
