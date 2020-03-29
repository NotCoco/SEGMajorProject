package main.java.com.projectBackEnd.Entities.Page.Micronaut;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.


//@Introspected
public class PagePatchCommand {

    @NotNull
    private int id;

    @NotBlank
    private String slug;

    @NotNull
    private int index;

    public PagePatchCommand() {

    }

    public PagePatchCommand(int id, String slug, int index) {
        this.id = id;
        this.slug = slug;
        this.index = index;
    }

    public void setSlug(String slug){
        this.slug = slug;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }
}
