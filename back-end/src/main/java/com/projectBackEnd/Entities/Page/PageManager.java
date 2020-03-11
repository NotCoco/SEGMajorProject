package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.io.Serializable;
import java.util.List;

/**
 * PageManager class that deals with interacting with the database itself with respect to Pages.
 * Inspiration:
 * //https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */

public class PageManager extends EntityManager implements PageManagerInterface {
    private static PageManagerInterface pageManager;
    private PageManager() {
        super();
        setSubclass(Page.class);
        HibernateUtility.addAnnotation(Page.class);
        pageManager = this;
    }
    public static PageManagerInterface getPageManager() {
        if (pageManager != null) return pageManager;
        else return new PageManager();
    }
    /**
     * Creates a page and adds it to the database table
     * @param slug The slug of the new page
     * @param index The index of the new page
     * @param title The title of the new page
     * @param content The content of the new page.
     * @return the created page
     */
    public Page addPage(String slug, Integer index, String title, String content) {
        Page newPage = new Page(slug, index, title, content);
        insertTuple(newPage);
        return newPage;
    }
    public Page addPage(Page newPage) {
        insertTuple(newPage);
        return newPage;
    }

    @Override
    public Page getByPrimaryKey(Serializable slug) {
        return (Page) super.getByPrimaryKey(slug);
    }

    public List<Page> getAllPages() {
        return (List<Page>) super.getAll();
    }
    @Override
    public void delete(Page pageToDelete) {
        super.delete(pageToDelete);
    }
    @Override
    public Page update(Page updatedCopy) {
        super.update(updatedCopy);
        return updatedCopy;
    }
}

/**
 * Locates and returns a page by its slug
 * @param slug The slug of the page we're looking for
 * @return The page we find
 */
    /*public Page findBySlug(String slug) {
        Session session = buildSessionFactory().openSession();
        slug = new SQLSafeString(slug).toString();
        Page page = (Page) session.load(Page.class, slug);
        session.close();
        return page;
    } //Might need to do getAll and find from that
    */
/**
 * Update a page's content by it's SLUG
 * @param slug The page's slug
 * @param newContent The new content it will receive.
 */
    /*
    public void updateContentBySlug(String slug, String newContent) {
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();

        Page pageFromDatabase = (Page) session.load(Page.class, slug);
        pageFromDatabase.setContent(new SQLSafeString(newContent).toString());
        session.getTransaction().commit();
        session.close();
    }*/

/**
 * Update a page's title by it's SLUG
 * @param slug The page's slug
 * @param newTitle The new title it will receive.
 */
    /*
    public void updateTitleBySlug(String slug, String newTitle) {
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();

        Page pageFromDatabase = (Page) session.load(Page.class, slug);
        pageFromDatabase.setTitle(new SQLSafeString(newTitle).toString());
        session.getTransaction().commit();
        session.close();
    }*/

/**
 * Deletes a page by its primary key
 * @param slug The slug of the page to be deleted
 */ /*
    public void deleteByPrimaryKey(String slug) {
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();
        Page page = findBySlug(slug);
        session.delete(page);
        session.getTransaction().commit();
        session.close();
    }*/ //TODO: Delete these, frontend are unlikely to send specific primary keys.

/**
 * Updates a page's index depending on its slug
 * @param slug The slug of the page
 * @param newIndex The new index
 */
    /* public void updateIndexBySlug(String slug, Integer newIndex) {
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        slug = new SQLSafeString(slug).toString();
        Page pageFromDatabase = (Page) session.load(Page.class, slug);
        pageFromDatabase.setIndex(newIndex);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Gets a list of all the pages
     * @return A list of all the pages
     */
    /*public List<Page> getAll() {
        Session session = buildSessionFactory().openSession();
        String hqlQuery = "FROM " + new SQLSafeString(Page.TABLENAME);
        @SuppressWarnings("Unchecked")
        List<Page> pages = session.createQuery(hqlQuery).list();
        session.close();
        return pages;
    }*/

    /**
     * Locates and returns a page by its slug
     * @param slug The slug of the page we're looking for
     * @return The page we find
     */
    /*
    public Page findBySlug(String slug) {
        Session session = buildSessionFactory().openSession();
        slug = new SQLSafeString(slug).toString();
        Page page = (Page) session.load(Page.class, slug);
        session.close();
        return page;
    }
*/
    /**
     * Deletes all the pages in the page table.
     */
    /*
    public void deleteAll() { //TODO Move up with parameter? Perhaps.
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM " + new SQLSafeString(Page.TABLENAME) + " ");
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
*/
    /**
     * Insert a new page to be added to the database
     * @param page The page to be added to the database
     */
    /*
    public void insertTuple(Page page) {
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        session.save(page);
        session.getTransaction().commit();
        session.close();
    }*/

    /**
     * Get all the pages with a given title
     * @return
     */

     //TODO Frontend will probably not be using this method, instead only using update(...) with jsons as
// they'll be sending .json objects not strings.
/**
 * Get all the pages with a given title
 * @return
 */
    /*public List<Page> getAllPagesByTitle(String title) {
        Session session = buildSessionFactory().openSession();
        String sql = "SELECT * FROM " + Page.TABLENAME + " WHERE " + Page.TITLE + " = '" + title + "';";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Page.class);
        List results = query.list();
        session.close();
        return results;
    }*/ //TODO Ask whether this way, direct hard coded SQL onto the database to only get what's needed is better than:

    /*
    public List<Page> getAllPagesByTitle(String title) {
        return getAll().stream().filter(p -> p.getTitle().equals(title)).collect(Collectors.toList());
    } //TODO Run this by in testing too! Fix documentation, ask L2

    //TODO Convert the outputs into jsons instead of file returns somewhere. Perhaps a class that takes Entity objects.
    //TODO Remove this unnecessary method as the frontend do not require it, currently staying for exemplar purpose.
    */
/**
 * Gets a list of all the pages
 * @return A list of all the pages
 */
        /*
public List<Page> getAll() { //<-- HQL get all
    Session session = buildSessionFactory().openSession();
    String hqlQuery = "FROM " + (Page.TABLENAME);
    @SuppressWarnings("Unchecked")
    List<Page> pages = session.createQuery(hqlQuery).list();
    session.close();
    return pages;
}*/
/**
 * Queries inside hibernate entirely.
 * @param slug
 * @return
 *//*
    public Page getBySlug(String slug){ //Queries hibernate internally
        Session session = buildSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Page> query = cb.createQuery(Page.class);
        Root<Page> root = query.from(Page.class);
        query.select(root).where(
                cb.equal(root.get(Page.SLUG.toLowerCase()), slug));

        return session.createQuery(query).getResultList().get(0);
    }*/
/**
 * Deletes all the pages in the page table.
 */
    /*public void deleteAll() { //TODO Move up with parameter? Perhaps.
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM " + (Page.TABLENAME) + " ");
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    public void deleteAllCascade() { //TODO Move up with parameter? Perhaps.
        for (Page p : (List<Page>) getAll()) {
            delete(p);
        }
    }*/
