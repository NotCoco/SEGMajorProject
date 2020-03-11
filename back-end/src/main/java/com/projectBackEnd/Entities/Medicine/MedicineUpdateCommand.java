package main.java.com.projectBackEnd.Entities.Medicine;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class MedicineUpdateCommand {


    private int id;


    private String name;


    private String type;

    public MedicineUpdateCommand(){

    }

    public MedicineUpdateCommand(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
