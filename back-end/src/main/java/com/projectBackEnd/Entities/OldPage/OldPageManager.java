package main.java.com.projectBackEnd.Entities.OldPage;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.io.Serializable;
import java.util.List;

/**
 * OldPageManager class that deals with interacting with the database itself with respect to OldPages.
 * Inspiration:
 * //https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */

public class OldPageManager extends EntityManager implements OldPageManagerInterface {
    private static OldPageManagerInterface pageManager;
    private OldPageManager() {
        super();
        setSubclass(OldPage.class);
        HibernateUtility.addAnnotation(OldPage.class);
        pageManager = this;
    }
    public static OldPageManagerInterface getOldPageManager() {
        if (pageManager != null) return pageManager;
        else return new OldPageManager();
    }
    /**
     * Creates a page and adds it to the database table
     * @param slug The slug of the new page
     * @param index The index of the new page
     * @param title The title of the new page
     * @param content The content of the new page.
     * @return the created page
     */
    public OldPage addOldPage(String slug, Integer index, String title, String content) {
        OldPage newOldPage = new OldPage(slug, index, title, content);
        insertTuple(newOldPage);
        return newOldPage;
    }
    public OldPage addOldPage(OldPage newOldPage) {
        insertTuple(newOldPage);
        return newOldPage;
    }

    @Override
    public OldPage getByPrimaryKey(Serializable slug) {
        return (OldPage) super.getByPrimaryKey(slug);
    }

    public List<OldPage> getAllPages() {
        return (List<OldPage>) super.getAll();
    }
    @Override
    public void delete(OldPage pageToDelete) {
        super.delete(pageToDelete);
    }
    @Override
    public OldPage update(OldPage updatedCopy) {
        super.update(updatedCopy);
        return updatedCopy;
    }
}

/**
 * Locates and returns a page by its slug
 * @param slug The slug of the page we're looking for
 * @return The page we find
 */
    /*public OldPage findBySlug(String slug) {
        Session session = buildSessionFactory().openSession();
        slug = new SQLSafeString(slug).toString();
        OldPage page = (OldPage) session.load(OldPage.class, slug);
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

        OldPage pageFromDatabase = (OldPage) session.load(OldPage.class, slug);
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

        OldPage pageFromDatabase = (OldPage) session.load(OldPage.class, slug);
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
        OldPage page = findBySlug(slug);
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
        OldPage pageFromDatabase = (OldPage) session.load(OldPage.class, slug);
        pageFromDatabase.setIndex(newIndex);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Gets a list of all the pages
     * @return A list of all the pages
     */
    /*public List<OldPage> getAll() {
        Session session = buildSessionFactory().openSession();
        String hqlQuery = "FROM " + new SQLSafeString(OldPage.TABLENAME);
        @SuppressWarnings("Unchecked")
        List<OldPage> pages = session.createQuery(hqlQuery).list();
        session.close();
        return pages;
    }*/

    /**
     * Locates and returns a page by its slug
     * @param slug The slug of the page we're looking for
     * @return The page we find
     */
    /*
    public OldPage findBySlug(String slug) {
        Session session = buildSessionFactory().openSession();
        slug = new SQLSafeString(slug).toString();
        OldPage page = (OldPage) session.load(OldPage.class, slug);
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
        Query query = session.createQuery("DELETE FROM " + new SQLSafeString(OldPage.TABLENAME) + " ");
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
    public void insertTuple(OldPage page) {
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
    /*public List<OldPage> getAllPagesByTitle(String title) {
        Session session = buildSessionFactory().openSession();
        String sql = "SELECT * FROM " + OldPage.TABLENAME + " WHERE " + OldPage.TITLE + " = '" + title + "';";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(OldPage.class);
        List results = query.list();
        session.close();
        return results;
    }*/ //TODO Ask whether this way, direct hard coded SQL onto the database to only get what's needed is better than:

    /*
    public List<OldPage> getAllPagesByTitle(String title) {
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
public List<OldPage> getAll() { //<-- HQL get all
    Session session = buildSessionFactory().openSession();
    String hqlQuery = "FROM " + (OldPage.TABLENAME);
    @SuppressWarnings("Unchecked")
    List<OldPage> pages = session.createQuery(hqlQuery).list();
    session.close();
    return pages;
}*/
/**
 * Queries inside hibernate entirely.
 * @param slug
 * @return
 *//*
    public OldPage getBySlug(String slug){ //Queries hibernate internally
        Session session = buildSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<OldPage> query = cb.createQuery(OldPage.class);
        Root<OldPage> root = query.from(OldPage.class);
        query.select(root).where(
                cb.equal(root.get(OldPage.SLUG.toLowerCase()), slug));

        return session.createQuery(query).getResultList().get(0);
    }*/
/**
 * Deletes all the pages in the page table.
 */
    /*public void deleteAll() { //TODO Move up with parameter? Perhaps.
        Session session = buildSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM " + (OldPage.TABLENAME) + " ");
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    public void deleteAllCascade() { //TODO Move up with parameter? Perhaps.
        for (OldPage p : (List<OldPage>) getAll()) {
            delete(p);
        }
    }*/
