package main.java.com.projectBackEnd.Entities.Page;

//TODO Appears the same as page? Is apparently Command Design Pattern
public class PageCommand {
    private String slug;
    private int index;
    private String title;
    private String content;
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
