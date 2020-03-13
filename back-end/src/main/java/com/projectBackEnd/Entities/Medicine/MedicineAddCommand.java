package main.java.com.projectBackEnd.Entities.Medicine;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class MedicineAddCommand {

    @NotNull
    private String name;

    @NotNull
    private String type;

    public MedicineAddCommand(){

    }

    public MedicineAddCommand(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}

