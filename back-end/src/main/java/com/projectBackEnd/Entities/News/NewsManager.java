package main.java.com.projectBackEnd.Entities.News;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.addAll;

/**
 * NewsManager defines methods for News objects to interact with the database.
 * This class extends the EntityManager.
 *
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class NewsManager extends EntityManager implements NewsManagerInterface  {

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

    /** Insert input News object into the database
     * @param news
     * @return added news
     */
    public News addNews(News news) {
        insertTuple(news);
        return news;
    }

    /** Insert a new News object into the database
     * @param date
     * @param pinned
     * @param description
     * @param title
     * @param urgent
     * @param content
     * @param slug
     * @return newly created News object added to db
     */
    public News addNews(Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug) {
        News newArticle = new News(date, pinned, description, title, urgent, content, slug);
        addNews(newArticle);
        return newArticle;
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
     * @param news
     * @return updated object
     */
    public News update(News news) {
        super.update(news);
        return news;
    }

    /**
     * Remove all news from database
     */
    public void deleteAll() {
        super.deleteAll();
    }

    /**
     * Delete input News object
     * @param news
     */
    public void delete(News news) {
        super.delete(news);
    }//TODO removable?

    /**
     * Delete News object by primary key
     * @param pk
     */
    public void delete(Integer pk) {
        super.delete(pk);
    }

    /**
     * @return list of all News objects in database
     */
    public List<News> getAllNews() {
        List<News> allNews = super.getAll();
        Stream<News> allPinnedNews = allNews.stream().filter(n -> n.isPinned()).sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        Stream<News> allUnpinnedNews = allNews.stream().filter(n -> !n.isPinned()).sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        return Stream.concat(allPinnedNews, allUnpinnedNews).collect(Collectors.toList());
    } //Sort by Pinned/date then date

    /**
     * Sort all the news by lowest date, in the following order :
     * Urgent and pinned, urgent only, pinned only, neither pinned nor urgent.
     * @return sorted list of news
     */
    public List<News> sort() {
        List<News> all = getAllNews();

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

    /**
     * @return list of all pinned news
     */
    public List<News> getAllPinnedNews() {
        List<News> allNews = super.getAll();
        return allNews.stream().filter(n -> n.isPinned()).sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
    }


}
