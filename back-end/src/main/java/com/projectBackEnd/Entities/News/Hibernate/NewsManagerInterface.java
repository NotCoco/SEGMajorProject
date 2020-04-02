package main.java.com.projectBackEnd.Entities.News.Hibernate;

import java.io.Serializable;
import java.util.List;


/**
 *  Methods used by NewsManager for database queries related to News objects
 */
public interface NewsManagerInterface {

    public News addNews(News news);

    public News update(News news);

    public News getByPrimaryKey(Integer id);

    public News getNewsBySlug(String slug);

    public List<News> getAllNews();

    public void delete(Serializable pk);

    public void deleteAll();

}