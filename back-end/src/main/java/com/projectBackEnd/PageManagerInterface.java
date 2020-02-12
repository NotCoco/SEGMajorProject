package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;

import java.util.List;

public interface PageManagerInterface {
    public Page createPage(String slug, Integer index, String content);
    public void createAndSavePage(String slug, Integer index, String content);
    public void update(Page page);
    public void updateContentBySlug(String slug, String newContent);
    public void deleteByPrimaryKey(String slug);
    public void updateIndexBySlug(String slug, Integer newIndex);
    public SessionFactory getSessionFactory();
    public List<Page> getAll();
    public Page findBySlug(String slug);
    public void deleteAll();
    public void insertTuple(Page page);
}
