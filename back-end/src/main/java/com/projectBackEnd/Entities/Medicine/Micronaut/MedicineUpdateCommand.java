package main.java.com.projectBackEnd.Entities.Medicine.Micronaut;

import javax.validation.constraints.NotNull;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//import io.micronaut.core.annotation.Introspected;

/**
 * MedicineUpdateCommand is an implementation of the Command design pattern.
 * It creates mock medicine objects and reduced memory use.
 * It is used by the controller to update a medicine object.
 */
//@Introspected
public class MedicineUpdateCommand extends MedicineAddCommand {

    @NotNull
    private int id;

    public MedicineUpdateCommand() {
        super();
    }

    public MedicineUpdateCommand(int id, String name, String type) {
        super(name, type);
        this.id = id;
    }

    public int getId() { return id; }

}
