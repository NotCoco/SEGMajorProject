package main.java.com.projectBackEnd.Entities.News.Hibernate;

import main.java.com.projectBackEnd.Entities.News.Hibernate.News;

import java.io.Serializable;
import java.util.List;
/**
 *  Methods used by NewsManager for database queries related to News objects
 */
public interface NewsManagerInterface {
    public List<News> getAllNews();
    public void deleteAll();
    public News addNews(News news);
    public News getByPrimaryKey(Integer id);
    public News getNewsBySlug(String slug);
    public void delete(Serializable pk);
    public News update(News news);

}
