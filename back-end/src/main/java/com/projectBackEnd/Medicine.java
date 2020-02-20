package main.java.com.projectBackEnd;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = Medicine.TABLENAME)

// TODO : Commenting

public class Medicine implements TableEntity{

    // Table columns
    public static final String TABLENAME = "Medicine";
    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;

    @Column(name = NAME, nullable = false)
    @Type(type = "text")
    private String name;

    @Column(name = TYPE)
    @Type(type="text")
    private String type;
    // Liquid, Tablet, Capsule, Injection, Topical, Suppositories, Drops, Inhalers

    /**
     * Constructors
     */
    public Medicine(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Getters and setters; ID cannot be changed

    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = new SQLSafeString(name).toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = new SQLSafeString(type).toString();
    }

    @Override
    public String toString() {
        return "Medicine: " + this.primaryKey + ", " + this.name + ", " + this.type;
    }

    public static String getCreateQuery() {
        String createQuery = "CREATE TABLE " + TABLENAME + " (";
        createQuery += ID + " INTEGER NOT NULL AUTO-INCREMENT, ";
        createQuery += NAME + " VARCHAR(255) NOT NULL, ";
        createQuery += TYPE + " VARCHAR(255), ";
        createQuery += "PRIMARY KEY (" + ID + "));";
        System.out.println(createQuery);
        return createQuery;
    }

    public boolean equals(Medicine otherMed) {
        return (getPrimaryKey() == otherMed.getPrimaryKey()) && getName().equals(otherMed.getName()) &&
                getType().equals(otherMed.getType());
    }

    @Override
    public TableEntity imitate(TableEntity toCopy) {
        Medicine medToCopy = (Medicine) toCopy;
        setName(medToCopy.getName());
        setType(medToCopy.getType());
        return this;
    }


}

