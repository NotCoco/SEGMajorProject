package main.java.com.projectBackEnd.Entities.Page.Hibernate;

import main.java.com.projectBackEnd.Entities.Site.Hibernate.Site;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * PageManager defines methods to interact with the Page table in the database.
 * This class extends the EntityManager.
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class PageManager extends EntityManager implements PageManagerInterface {

    private static PageManagerInterface pageManager;

    /**
     * Private constructor (Singleton design pattern)
     */
    private PageManager() {

        super();
        setSubclass(Page.class);
        HibernateUtility.addAnnotation(Site.class);
        HibernateUtility.addAnnotation(Page.class);
        pageManager = this;

    }


    /**
     * Get page manager interface
     * @return pageManager ; if none has been defined, create a new PageManager()
     */
    public static PageManagerInterface getPageManager() {
        if (pageManager != null) return pageManager;
        else return new PageManager();
    }


    /**
     * Insert a Page object into the database
     * @param newPage   Page to add to the database
     * @return added object
     */
    public Page addPage(Page newPage) {
        return (Page) super.insertTuple(newPage);
    }


    /**
     * Update attributes of the given Page object
     * @return Page object with updated attributes
     */
    public Page update(Page updatedVersion) { super.update(updatedVersion); return updatedVersion; }


    /**
     * Retrieve Page object corresponding to the given primary key from the database
     * @param pk    Primary key of the Page to find
     * @return found Page
     */
    public Page getByPrimaryKey(Integer pk) {
        return (Page) super.getByPrimaryKey(pk);
    }


    /**
     * Get all Pages belonging to the given site from the database
     * @param siteSlug  Slug of the parent site
     * @return List of pages belonging to parent site
     */
    public List<Page> getAllPagesOfSite(String siteSlug) {
        return getAllPages().stream().filter(p -> p.getSite().getSlug().equals(siteSlug))
                .sorted(Comparator.comparingInt(Page::getIndex)).collect(Collectors.toList());
    }


    /**
     * Retrieve Page associated to input site and slug in the database
     * @param siteSlug  Slug of the parent Site
     * @param slug      Slug of the Page
     * @return found Page
     */
    public Page getPageBySiteAndSlug(String siteSlug, String slug) {
        List<Page> matches = getAllPages().stream().filter(p -> p.getSite().getSlug().equals(siteSlug) && p.getSlug().equals(slug))
                .sorted(Comparator.comparingInt(Page::getIndex)).collect(Collectors.toList());
        return matches.size()>= 1 ? matches.get(0) : null;
    }


    /**
     * Get all the objects from the Page table stored in the database
     * @return List of all pages in Page table
     */
    public List<Page> getAllPages() {
        return (List<Page>) super.getAll();
    }


}