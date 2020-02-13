package main.java.com.projectBackEnd;

import javax.persistence.*;

@Entity
@Table(name = Medicine.TABLENAME)

public class Medicine {

    // Table columns
    public static final String TABLENAME = "Medicines";
    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";

    @Id @GeneratedValue
    @Column(name="ID", unique = true)
    private int id;

    @Column(name="NAME", nullable = false)
    private String name;

    // Liquid, Tablet, Capsule, Injection, Topical, Suppositories, Drops, Inhalers
    @Column(name="TYPE",nullable = false)
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

}

