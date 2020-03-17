package main.java.com.projectBackEnd.Entities.Medicine;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;

/**
 * MedicineUpdateCommand is an implementation of the Command design pattern.
 * It creates mock medicine objects and reduced memory use.
 * It is used by the controller to update a medicine object.
 */
//TODO Reinsert Tags as I couldn't get them to compile on IntelliJ
//@Introspected
public class MedicineUpdateCommand {

    private int id;
    @NotNull
    private String name;
    @NotNull
    private String type;

    public MedicineUpdateCommand() {};

    public MedicineUpdateCommand(int id, String name, String type) {
        this.id = id;
        this.name = (name != null && "".equals(name.trim())) ? "Unnamed" : name;
        this.type = (type != null && "".equals(type.trim())) ? "Undefined" : type;
    }

    /**
     * Getters and setters
     */

    public int getId() { return id; }

    public String getName(){ return name; }

    public String getType(){
        return type;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = (name != null && "".equals(name.trim())) ? "Unnamed" : name;
    }

    public void setType(String type) {
        this.type = (type != null && "".equals(type.trim())) ? "Undefined" : type;
    }
}
