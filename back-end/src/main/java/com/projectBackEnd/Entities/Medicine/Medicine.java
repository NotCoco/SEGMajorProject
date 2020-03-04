package main.java.com.projectBackEnd.Entities.Medicine;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = Medicine.TABLENAME)

// TODO (Jeanne): Commenting

public class Medicine implements TableEntity{

    // Table columns
    public static final String TABLENAME = "Medicines";
    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Medicine() {
    }

    public Medicine(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Constructor taking id 
    public Medicine(Integer id, String name, String type) {
        this.primaryKey = id;
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
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Medicine: " + this.primaryKey + ", " + this.name + ", " + this.type;
    }

    public boolean equals(Medicine otherMed) {
        return (getPrimaryKey() == otherMed.getPrimaryKey()) && getName().equals(otherMed.getName()) &&
                getType().equals(otherMed.getType());
    }

    @Override
    public TableEntity copy(TableEntity toCopy) {
        Medicine medToCopy = (Medicine) toCopy;
        setName(medToCopy.getName());
        setType(medToCopy.getType());
        return this;
    }

}

