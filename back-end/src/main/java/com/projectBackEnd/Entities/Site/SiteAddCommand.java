package main.java.com.projectBackEnd.Entities.Site;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.


//@Introspected
public class SiteAddCommand {
    @NotNull
    private String name;

    public SiteAddCommand(){

    }

    public SiteAddCommand(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}
