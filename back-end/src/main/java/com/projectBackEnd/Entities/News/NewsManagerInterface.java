package main.java.com.projectBackEnd.Entities.News;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface NewsManagerInterface {
    public void deleteAll();
    public void delete(News news); //TODO removable?
    public void delete(Serializable pk);
    public News update(News news);
    public List<News> getAllNews(); //Sort by Pinned/date then date
    public News addNews(News news);
    public News getByPrimaryKey(Integer id);
    public News getNewsBySlug(String slug);
    public News addNews(Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug);
    public List<News> getAllPinnedNews();
}
