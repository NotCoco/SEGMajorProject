package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;

import java.util.List;

/**
 * All Page related functions for querying the database that won't be recognised by REST
 * //TODO Filter the interfaces!
 */
public interface PageManagerInterface extends PageRestInterface {
    public Page createPage(String slug, Integer index, String title, String content);
    public void createAndSavePage(String slug, Integer index, String title, String content);
    public void update(Page page);
    public void updateContentBySlug(String slug, String newContent);
    public void updateTitleBySlug(String slug, String newTitle);
    public void deleteByPrimaryKey(String slug);
    public void updateIndexBySlug(String slug, Integer newIndex);
    public SessionFactory getSessionFactory();
    public List<Page> getAll();
    public Page findBySlug(String slug);
    public void deleteAll();
    public void insertTuple(Page page);
    public void getAllPagesByTitle(String title);
} //TODO Order this so it looks good.
