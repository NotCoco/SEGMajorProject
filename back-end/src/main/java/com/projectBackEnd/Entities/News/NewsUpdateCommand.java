package main.java.com.projectBackEnd.Entities.News;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
