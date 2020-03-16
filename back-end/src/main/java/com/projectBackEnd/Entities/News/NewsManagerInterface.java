package main.java.com.projectBackEnd.Entities.News;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 *  Methods used by NewsManager for database queries related to News objects
 */
public interface NewsManagerInterface {

    public News addNews(News news);
    public News addNews(Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug);
    public News update(News news);
    public News getByPrimaryKey(Integer id);
    public News getNewsBySlug(String slug);
    public void deleteAll();
    public void delete(News news); //TODO removable?
    public void delete(Serializable pk);
    public List<News> getAllNews();
    public List<News> sort();
    public List<News> getAllPinnedNews();

}
