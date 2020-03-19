package main.java.com.projectBackEnd.Entities.Medicine;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.

/**
 * MedicineAddCommand is an implementation of the Command design pattern.
 * It creates mock medicine objects and reduced memory use.
 * It is used by the controller to insert a medicine object into the database/
 */
//@Introspected
public class MedicineAddCommand {

    @NotNull
    private String name;

    @NotNull
    private String type;

    public MedicineAddCommand(){}

    public MedicineAddCommand(String name, String type){
        //setName(name)
        //setType(type)
        this.name = (name != null && "".equals(name.trim())) ? "Unnamed" : name;
        this.type = (type != null && "".equals(type.trim())) ? "Undefined" : type;
    }

    /**
     * Getters and setters
     */
    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public void setName(String name) {
        this.name = (name != null && "".equals(name.trim())) ? "Unnamed" : name;
    }

    public void setType(String type) {
        this.type = (type != null && "".equals(type.trim())) ? "Undefined" : type;
    }
}

