package main.java.com.projectBackEnd.Entities.News.Micronaut;

import javax.validation.constraints.NotNull;
import java.util.Date;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class NewsUpdateCommand extends NewsAddCommand {


    @NotNull
    private Integer id;

    public NewsUpdateCommand() {super();}

    public NewsUpdateCommand(Integer id, Date date, boolean pinned, String description, String title, boolean urgent,
                             String content, String slug) {
        super(date, pinned, description, title, urgent, content, slug);
        this.id = id;
    }

    public int getId() { return id; }

}
