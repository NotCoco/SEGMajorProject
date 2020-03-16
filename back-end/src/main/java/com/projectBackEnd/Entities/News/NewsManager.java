package main.java.com.projectBackEnd.Entities.News;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.addAll;

public class NewsManager extends EntityManager implements NewsManagerInterface  {
    private static NewsManagerInterface newsManager;
    private NewsManager() {
        super();
        setSubclass(News.class);
        HibernateUtility.addAnnotation(News.class);

        newsManager = this;
    }
    public static NewsManagerInterface getNewsManager() {
        if (newsManager != null) return newsManager;
        else return new NewsManager();
    }
    public void deleteAll() {
        super.deleteAll();
    }
    public void delete(News news) {
        super.delete(news);
    }//TODO removable?
    public void delete(Integer pk) {
        super.delete(pk);
    }
    public News update(News news) {
        super.update(news);
        return news;
    }
    public List<News> getAllNews() {
        List<News> allNews = super.getAll();
        Stream<News> allPinnedNews = allNews.stream().filter(n -> n.isPinned()).sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        Stream<News> allUnpinnedNews = allNews.stream().filter(n -> !n.isPinned()).sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
        return Stream.concat(allPinnedNews, allUnpinnedNews).collect(Collectors.toList());
    } //Sort by Pinned/date then date
    public List<News> getAllPinnedNews() {
        List<News> allNews = super.getAll();
        return allNews.stream().filter(n -> n.isPinned()).sorted(Comparator.comparing(News::getDate, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
    }
    public News addNews(News news) {
        insertTuple(news);
        return news;
    }
    public News getByPrimaryKey(Integer id) {
        return (News) super.getByPrimaryKey(id);
    }
    public News getNewsBySlug(String slug) {
        List<News> matches = getAllNews().stream().filter(p -> p.getSlug().equals(slug)).collect(Collectors.toList());
        return matches.size()>= 1 ? matches.get(0) : null;
    }

    public News addNews(Date date, boolean pinned, String description, String title, boolean urgent, String content, String slug) {
        News newArticle = new News(date, pinned, description, title, urgent, content, slug);
        addNews(newArticle);
        return newArticle;
    }

}
