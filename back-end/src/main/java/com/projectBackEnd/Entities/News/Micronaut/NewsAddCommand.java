package main.java.com.projectBackEnd.Entities.News.Micronaut;

import javax.validation.constraints.NotNull;
import java.util.Date;
//import io.micronaut.core.annotation.Introspected;
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class NewsAddCommand {

    @NotNull
    private Date date; //Can be updated to take a long of the time!

    @NotNull
    private String description;

    @NotNull
    private String content;

    @NotNull
    private String slug;

    @NotNull
    private String title;

    @NotNull
    private boolean urgent;

    @NotNull
    private boolean pinned;

    public NewsAddCommand(){}

    public NewsAddCommand(Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug) {
        this.date = date;
        this.pinned = pinned;
        this.description = description;
        this.title = title;
        this.urgent = urgent;
        this.content = content;
        this.slug = slug;
    }

    /**
     * Getters and setters
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUrgent() { //Might need to become getUrgent since Jackson conversion
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

}
