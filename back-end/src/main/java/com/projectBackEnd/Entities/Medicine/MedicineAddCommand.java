package main.java.com.projectBackEnd.Entities.Medicine;
import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class MedicineAddCommand {


    private String name;


    private String type;

    MedicineAddCommand(){

    }

    MedicineAddCommand(String name, String type){
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

