package main.java.com.projectBackEnd.Entities.Site;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;

/**
 * SiteAddComment is an implementation of the Command design pattern.
 * It creates mock Site objects and reduced memory use.
 * It is used by the controller to insert a Site object into the database
 */
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class SiteAddCommand {

    @NotNull
    private String name;
    @NotNull
    private String slug;

    public SiteAddCommand(){}

    public SiteAddCommand(String slugName, String siteName){
        this.slug = slugName;
        this.name = siteName;
    }

    public String getName(){
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getSlug() { return slug; }
    public void setSlug(String newSlug) { this.slug = newSlug; }
}