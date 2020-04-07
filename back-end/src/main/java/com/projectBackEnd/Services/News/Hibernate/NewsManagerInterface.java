package main.java.com.projectBackEnd.Services.News.Hibernate;

import java.util.List;


/**
 *  Methods used by NewsManager for database queries related to News objects
 */
public interface NewsManagerInterface {

    News addNews(News news);

    News update(News news);

    News getByPrimaryKey(Integer id);

    News getNewsBySlug(String slug);

    List<News> getAllNews();

    void delete(Integer pk);

    void deleteAll();

}