package main.java.com.projectBackEnd.Entities.News.Hibernate;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * NewsManager defines methods for News objects to interact with the database.
 * This class extends the EntityManager.
 *
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class NewsManager extends EntityManager implements NewsManagerInterface {

    private static NewsManagerInterface newsManager;

    private NewsManager() {
        super();
        setSubclass(News.class);
        HibernateUtility.addAnnotation(News.class);
        newsManager = this;
    }

    /** Get news manager interface
     * @return newsManager ; if none has been defined, create NewsManager()
     */
    public static NewsManagerInterface getNewsManager() {
        if (newsManager != null) return newsManager;
        else return new NewsManager();
    }

    /**
     * @return list of all News objects in database
     */
    public List<News> getAllNews() {
        List<News> allNews = super.getAll();
        return sort(allNews);
    }


    /** Insert input News object into the database
     * @param news
     * @return added news
     */
    public News addNews(News news) {
        if (getNewsBySlug(news.getSlug()) != null) return new News();
        insertTuple(news);
        return news;
    }

    /** Retrieve news object from database using primary key
     * @param id
     * @return retrieved object
     */
    public News getByPrimaryKey(Integer id) {
        return (News) super.getByPrimaryKey(id);
    }

    /** Retrieve news object from database using slug
     * @param slug
     * @return first news object with input slug ; else null
     */
    public News getNewsBySlug(String slug) {
        List<News> matches = getAllNews().stream().filter(p -> p.getSlug().equals(slug)).collect(Collectors.toList());
        return matches.size()>= 1 ? matches.get(0) : null;
    }

    /**
     * Update input News object
     * @param updatedVersion
     * @return updated object
     */
    public News update(News updatedVersion) {
        News n = getNewsBySlug(updatedVersion.getSlug());
        if (n != null && n.getPrimaryKey() != updatedVersion.getPrimaryKey()) return new News();
        return (News) super.update(updatedVersion);
    }

    /**
     * Delete News object by primary key
     * @param pk
     */
    public void delete(Integer pk) {
        super.delete(pk);
    }

    /**
     * Remove all news from database
     */
    public void deleteAll() {
        super.deleteAll();
    }


    /**
     * Sort all the news by lowest date, in the following order :
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
