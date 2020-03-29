package main.java.com.projectBackEnd.Entities.Site.Micronaut;

import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;

//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class SiteUpdateCommand extends SiteAddCommand {

    @NotNull
    private Integer id;

    public SiteUpdateCommand(){}

    public SiteUpdateCommand(Integer id, String slug, String site){
        super(slug, site);
        this.id = id;
    }
    public void setId(Integer id) { this.id = id; }
    public Integer getId() { return id; }

}
