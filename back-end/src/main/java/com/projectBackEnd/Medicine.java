package main.java.com.projectBackEnd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = Medicine.TABLENAME)

public class Medicine {

    // Table columns
    public static final String TABLENAME = "Medicines";
    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";

    @Id
    @Column(name = ID)
    private Integer id;     // Unique ID

    @Column(name = NAME)
    private String name;

    @Column(name = TYPE)
    // Liquid, Tablet, Capsule, Injection, Topical, Suppositories, Drops, Inhalers
    private String type;


    /**
     * Constructors
     */
    public Medicine(String name, String type) {
        this.name = new SQLSafeString(name).toString();
        this.type = new SQLSafeString(type).toString();
    }

    // Unsure if it should stay
    public Medicine(Integer id, String name, String type) {
        this.id = id;
        this.name = new SQLSafeString(name).toString();
        this.type = new SQLSafeString(type).toString();
    }

    // Getters and setters; ID cannot be changed

    public  Integer getId() {
        return id;
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
        return "Medicine: " + this.id + ", " + this.name + ", " + this.type;
    }

    public static String getCreateQuery() {
        String createQuery = "CREATE TABLE " + new SQLSafeString(TABLENAME) + " (";
        createQuery += new SQLSafeString(ID) + " INTEGER NOT NULL AUTO-INCREMENT, ";
        createQuery += new SQLSafeString(NAME) + " VARCHAR(255) NOT NULL, ";
        createQuery += new SQLSafeString(TYPE) + " VARCHAR(255), ";
        createQuery += "PRIMARY KEY (" + new SQLSafeString(ID) + ")";
        createQuery += ");";
        System.out.println(createQuery);
        return createQuery;
    }
}

