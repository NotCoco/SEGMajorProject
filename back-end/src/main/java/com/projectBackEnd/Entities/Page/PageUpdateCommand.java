package main.java.com.projectBackEnd.Entities.Page;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class PageUpdateCommand extends PageAddCommand {

    @NotNull
    private Integer primaryKey;


    public PageUpdateCommand(){
        super();
    }

    public PageUpdateCommand(Integer primaryKey, String siteName, String slug, Integer index, String title, String content){
        super(siteName, slug, index, title, content);
        this.primaryKey = primaryKey;
    }

    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }



}