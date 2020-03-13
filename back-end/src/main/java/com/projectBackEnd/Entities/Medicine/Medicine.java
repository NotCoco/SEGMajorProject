package main.java.com.projectBackEnd.Entities.Medicine;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import javax.validation.constraints.NotNull;


/**
 * Medicine objects are database entities for the table 'Medicine' defined in this class.
 * The have three attributes :
 *    - auto-increment primary key as 'ID' in table
 *    - name as 'Name',
 *    - type as 'Type' (Liquid, Tablet, Capsule, Injection, Topical, Suppositories, Drops, Inhalers).
 *
 *    https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
@Entity
@Table(name = Medicine.TABLENAME)
public class Medicine implements TableEntity {

    // Table columns (attributes)
    public static final String TABLENAME = "Medicines";
    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";

    // Private Fields : primaryKey, name, type
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer primaryKey;

    @NotNull
    @Type(type = "text")
    @Column(name = NAME, nullable = false)
    private String name;

    @NotNull
    @Type(type="text")
    @Column(name = TYPE, nullable = false)
    private String type;


    /**
     * Constructors : empty constructor, default constructor & cosntructor
     * that takes the primary key id for object re-creation
     */
    public Medicine() {
    }

    public Medicine(String name, String type) {
        this.primaryKey = -1;
        this.name = name == "" ? "Unnamed" : name;
        this.type = type == "" ? "Undefined" : type;
    }

    // Constructor taking id
    public Medicine(Integer id, String name, String type) {
        this.primaryKey = id;
        this.name = name == "" ? "Unnamed" : name;
        this.type = type == "" ? "Undefined" : type;
    }


    /**
     * Getters and setters; ID cannot be changed
     */
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


    /**
     * @return String indicating of private field values of the object
     */
    @Override
    public String toString() {
        return "Medicine: " + this.primaryKey + ", " + this.name + ", " + this.type;
    }


    /**
     * Check if this object is the same as another medicine object
     * @param otherMed
     * @return boolean, true if same object
     */
    public boolean equals(Medicine otherMed) {
        return (getPrimaryKey().equals(otherMed.getPrimaryKey())) && getName().equals(otherMed.getName()) &&
                getType().equals(otherMed.getType());
    }


    /**
     * Copy the values of the input medicine object
     * @param toCopy
     * @return this, updated medicine object
     */
    @Override
    public TableEntity copy(TableEntity toCopy) {
        Medicine medToCopy = (Medicine) toCopy;
        setName(medToCopy.getName());
        setType(medToCopy.getType());
        return this;
    }


}

