package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PageManager defines methods to interact with the Page table in the database.
 * This class extends the EntityManager.
 * //TODO Remove methods only used by testing.
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class PageManager extends EntityManager implements PageManagerInterface {

    private static PageManagerInterface pageManager;

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
     * Insert input Page object into the database
     * @param newPage
     * @return added object
     */
    public Page addPage(Page newPage) {
        return (Page) super.insertTuple(newPage);
    }

    /**
     * Create and insert a new Page object into the database
     * Requires a site object parameter.
     * @param site
     * @param slug
     * @param index
     * @param title
     * @param content
     * @return added object
     */
    public Page addPage(Site site, String slug, Integer index, String title, String content) {
        return (Page) insertTuple(new Page(site, slug, index, title, content));
    }

    /**
     * Create and insert a new Page object into the database
     * Requires a site name parameter.
     * @param siteName
     * @param slug
     * @param index
     * @param title
     * @param content
     * @return
     */
    public Page addPage(String siteName, String slug, Integer index, String title, String content) {
        return (Page) insertTuple(new Page(siteName, slug, index, title, content));
    }

    /**
     * Find Page object associated to input primary key in the database
     * @param pk
     * @return found Page
     */
    public Page getByPrimaryKey(Integer pk) {
        return (Page) super.getByPrimaryKey(pk);
    } //TODO Useless likely, aswell.

    /**
     * Get all objects from Page table stored in the database
     * @return List of all pages in Page table
     */
    public List<Page> getAllPages() {
        return (List<Page>) super.getAll();
    }

    /**
     * Get all pages belonging to input site in database
     * @param site
     * @return List of pages
     */
    public List<Page> getAllPagesOfSite(Site site) { return getAllPagesOfSite(site.getSlug()); }

    /**
     * Get all pages belonging to input site in database
     * @param siteName
     * @return List of pages
     */
    public List<Page> getAllPagesOfSite(String siteSlug) {
        return getAllPages().stream().filter(p -> p.getSite().getSlug().equals(siteSlug)).sorted(Comparator.comparingInt(Page::getIndex)).collect(Collectors.toList());
    }

    /**
     * Find Page associated to input site and slug in the database
     * @param site
     * @param slug
     * @return found Page
     */
    public Page getPageBySiteAndSlug(Site site, String slug) {
        return getPageBySiteAndSlug(site.getSlug(), slug);
    }

    /**
     * Find Page associated to input site and slug in the database
     * @param siteName
     * @param slug
     * @return found Page
     */
    public Page getPageBySiteAndSlug(String siteSlug, String slug) {
        List<Page> matches = getAllPages().stream().filter(p -> p.getSite().getSlug().equals(siteSlug) && p.getSlug().equals(slug)).sorted(Comparator.comparingInt(Page::getIndex)).collect(Collectors.toList());
        return matches.size()>= 1 ? matches.get(0) : null;
    }

    /**
     * Update attributes of the object
     * @return updated object
     */
    public Page update(Page updatedVersion) { super.update(updatedVersion); return updatedVersion; }

    /**
     * Remove input Page from database
     * @param object
     */
    public void delete(Page object) {
        super.delete(object);
    } //TODO Is unnecessary now, can be removed

}
