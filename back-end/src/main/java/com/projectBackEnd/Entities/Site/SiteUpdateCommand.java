package main.java.com.projectBackEnd.Entities.Site;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;

//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class SiteUpdateCommand extends SiteAddCommand {

    @NotNull
    private Integer id;

    public SiteUpdateCommand(){}

    public SiteUpdateCommand(Integer id, String siteSlug, String siteName){
        super(siteSlug, siteName);
        this.id = id;
    }

    public Integer getId() { return id; }

}
