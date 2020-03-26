package main.java.com.projectBackEnd.Entities.Page;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
//import io.micronaut.core.annotation.Introspected;
/**
 * PageUpdateCommand is an implementation of the Command design pattern.
 * It is used by the controller to update a Page object.
 */
public class PageUpdateCommand extends PageAddCommand {

    @NotNull
    private int id;


    public PageUpdateCommand(){
        super();
    }

    public PageUpdateCommand(Integer id, String site, String slug, Integer index, String title, String content){
        super(site, slug, index, title, content);
        this.id = id;
    }

    public int getId() { return id; }

}
