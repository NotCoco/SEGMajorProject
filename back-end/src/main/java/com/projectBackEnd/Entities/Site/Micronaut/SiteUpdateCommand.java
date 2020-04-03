package main.java.com.projectBackEnd.Entities.Site.Micronaut;

import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;

//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class SiteUpdateCommand extends SiteAddCommand {

    @NotNull
    private int primaryKey;

    public SiteUpdateCommand(){}

    public SiteUpdateCommand(Integer id, String slug, String site){
        super(slug, site);
        this.primaryKey = id;
    }

    public int getPrimaryKey() { return primaryKey; }

}
