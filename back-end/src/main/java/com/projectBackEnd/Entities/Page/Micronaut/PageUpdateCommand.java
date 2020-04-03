package main.java.com.projectBackEnd.Entities.Page.Micronaut;
import javax.validation.constraints.NotNull;

//import io.micronaut.core.annotation.Introspected;
/**
 * PageUpdateCommand is an implementation of the Command design pattern.
 * It is used by the controller to update a Page object.
 */
public class PageUpdateCommand extends PageAddCommand {

    @NotNull
    private int primaryKey;


    public PageUpdateCommand(){
        super();
    }

    public PageUpdateCommand(Integer id, String site, String slug, Integer index, String title, String content){
        super(site, slug, index, title, content);
        this.primaryKey = id;
    }

    public int getPrimaryKey() { return primaryKey; }

}
