package main.java.com.projectBackEnd;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * PageManager class that deals with interacting with the database itself with respect to Pages.
 * Inspiration:
 * //https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class PageManager implements PageManagerInterface {

    /**
     * Creates a page object
     * @param slug The slug of the new page
     * @param index The index of the new page
     * @param title The title of the new page
     * @param content The content of the new page.
     */
    public Page createPage(String slug, Integer index, String title, String content) {
        return new Page(slug, index, title, content);
    }

    /**
     * Creates a page and adds it to the database table
     * @param slug The slug of the new page
     * @param index The index of the new page
     * @param title The title of the new page
     * @param content The content of the new page.
     */
    public void createAndSavePage(String slug, Integer index, String title, String content) {
        insertTuple(createPage(slug, index, title, content));
    }

    /**
     * A page is provided with new attributes, it replaces the page with the same ID
     * in the database.
     * @param page The page that will be updated
     */
    public void update(Page page) { //TODO Session to become instance variable, for cleaner code
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        Page pageFromDatabase = (Page) session.load(Page.class, page.getSlug());
        pageFromDatabase.setContent(page.getContent());
        pageFromDatabase.setIndex(page.getIndex());
        pageFromDatabase.setTitle(page.getTitle());
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Update a page's content by it's SLUG
     * @param slug The page's slug
     * @param newContent The new content it will receive.
     */
    public void updateContentBySlug(String slug, String newContent) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();

        Page pageFromDatabase = (Page) session.load(Page.class, slug);
        pageFromDatabase.setContent(new SQLSafeString(newContent).toString());
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Update a page's title by it's SLUG
     * @param slug The page's slug
     * @param newTitle The new title it will receive.
     */
    public void updateTitleBySlug(String slug, String newTitle) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();

        Page pageFromDatabase = (Page) session.load(Page.class, slug);
        pageFromDatabase.setTitle(new SQLSafeString(newTitle).toString());
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Deletes a page by its primary key
     * @param slug The slug of the page to be deleted
     */
    public void deleteByPrimaryKey(String slug) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();
        Page page = findBySlug(slug);
        session.delete(page);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Updates a page's index depending on its slug
     * @param slug The slug of the page
     * @param newIndex The new index
     */
    public void updateIndexBySlug(String slug, Integer newIndex) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();
        Page pageFromDatabase = (Page) session.load(Page.class, slug);
        pageFromDatabase.setIndex(newIndex);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Gets the session factory created in this case specifically for the Page class
     * @return The session factory.
     */
    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().
                addAnnotatedClass(Page.class)
                .configure(); //TODO These two lines need to be dynamic, controlling location of DB and class
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
        return sessionFactory;
    }

    /**
     * Gets a list of all the pages
     * @return A list of all the pages
     */
    public List<Page> getAll() {
        Session session = getSessionFactory().openSession();
        String hqlQuery = "FROM " + new SQLSafeString(Page.TABLENAME);
        @SuppressWarnings("Unchecked")
        List<Page> pages = session.createQuery(hqlQuery).list();
        session.close();
        return pages;
    }

    /**
     * Locates and returns a page by its slug
     * @param slug The slug of the page we're looking for
     * @return The page we find
     */
    public Page findBySlug(String slug) {
        Session session = getSessionFactory().openSession();
        slug = new SQLSafeString(slug).toString();
        Page page = (Page) session.load(Page.class, slug);
        session.close();
        return page;
    }

    /**
     * Deletes all the pages in the page table.
     */
    public void deleteAll() { //TODO Move up with parameter? Perhaps.
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM " + new SQLSafeString(Page.TABLENAME) + " ");
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Insert a new page to be added to the database
     * @param page The page to be added to the database
     */
    public void insertTuple(Page page) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(page);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Get all the pages with a given title
     * @return
     */
    /*public List<Page> getAllPagesByTitle(String title) {
        Session session = getSessionFactory().openSession();
        String sql = "SELECT * FROM " + Page.TABLENAME + " WHERE " + Page.TITLE + " = '" + title + "';";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Page.class);
        List results = query.list();
        session.close();
        return results;
    }*/ //TODO Ask whether this way, direct hard coded SQL onto the database to only get what's needed is better than:

    /**
     * Functional implementation of the above method.
     * @param title
     * @return
     */
    public List<Page> getAllPagesByTitle(String title) {
        return getAll().stream().filter(p -> p.getTitle().equals(title)).collect(Collectors.toList());
    } //TODO Run this by in testing too! Fix documentation, ask L2

    //TODO Convert the outputs into jsons instead of file returns somewhere. Perhaps a class that takes Entity objects.

}
