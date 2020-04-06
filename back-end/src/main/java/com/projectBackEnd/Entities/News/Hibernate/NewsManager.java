package main.java.com.projectBackEnd.Entities.News.Hibernate;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import javax.persistence.PersistenceException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * NewsManager defines methods to interact with the News table in the database.
 * This class extends the EntityManager - supplying it with the rest of its interface methods.
 *
 * Inspiration : https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class NewsManager extends EntityManager implements NewsManagerInterface {

    private static NewsManagerInterface newsManager;

    /**
     * Default private constructor (Singleton design pattern)
     */
    private NewsManager() {

        super();
        setSubclass(News.class);
        HibernateUtility.addAnnotation(News.class);
        newsManager = this;

    }


    /**
     * Get news manager interface
     * @return newsManager If none has been defined, create NewsManager()
     */
    public static NewsManagerInterface getNewsManager() {
        if (newsManager != null) return newsManager;
        else return new NewsManager();
    }


    /**
     * Insert a News object into the database
     * @param news  News object to add to the database
     * @return added News object
     */
    public News addNews(News news) {
        if (getNewsBySlug(news.getSlug()) != null) throw new PersistenceException();
        insertTuple(news);
        return news;
    }


    /**
     * Update a News object in the database
     * @param updatedVersion  News with updated attributes
     * @return updated object
     */
    public News update(News updatedVersion) {
        News newsMatch = getNewsBySlug(updatedVersion.getSlug());
        if (newsMatch != null && !newsMatch.getPrimaryKey().equals(updatedVersion.getPrimaryKey())) {
            throw new PersistenceException();
        }
        return (News) super.update(updatedVersion);
    }


    /**
     * Retrieve News object corresponding to input primary key from database
     * @param id    Primary key of object to find in database
     * @return  retrieved object
     */
    public News getByPrimaryKey(Integer id) {
        return (News) super.getByPrimaryKey(id);
    }


    /**
     * Retrieve News object corresponding to input slug from database
     * @param slug  Slug of object to find in database
     * @return first news object with input slug ; else null
     */
    public News getNewsBySlug(String slug) {
        List<News> matches = getAllNews().stream().filter(p -> p.getSlug().equals(slug)).collect(Collectors.toList());
        return matches.size()>= 1 ? matches.get(0) : null;
    }


    /**
     * Get list of all the News objects stored in News table of the database
     * @return list of all News objects in database
     */
    public List<News> getAllNews() {
        List<News> allNews = super.getAll();
        return sort(allNews);
    }


    /**
     * Delete News object corresponding to given primary key from database
     * @param pk    Primary key of News object to remove
     */
    public void delete(Integer pk) {
        super.delete(pk);
    }


    /**
     * Remove all News from the database
     */
    public void deleteAll() {
        super.deleteAll();
    }


    /**
     * Sort all the News from the database by lowest date, in the following order :
     * Urgent and pinned, urgent only, pinned only, neither pinned nor urgent.
     * @return sorted list of news
     */
    private static List<News> sort(List<News> all) {

        // Get pinned AND urgent
        Stream<News> pinnedAndUrgent = all.stream().filter(n -> n.isPinned() && n.isUrgent())
                .sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        // Get urgent only
        Stream<News> urgentDates = all.stream().filter(n -> !n.isPinned() && n.isUrgent())
                .sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        // Get pinned only
        Stream<News> pinnedDates = all.stream().filter(n -> n.isPinned() && !n.isUrgent())
                .sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        // Get neither pinned nor urgent
        Stream<News> regular = all.stream().filter(n -> !n.isPinned() && !n.isUrgent())
                .sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));

        // Concat all lists together to make sorted list
        List<News> sorted = Stream.concat(Stream.concat(Stream.concat(pinnedAndUrgent, urgentDates), pinnedDates), regular)
                .collect(Collectors.toList());

        return sorted;

    }


}