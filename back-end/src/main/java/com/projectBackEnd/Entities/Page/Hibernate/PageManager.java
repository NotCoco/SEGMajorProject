package main.java.com.projectBackEnd.Entities.Page.Hibernate;

import main.java.com.projectBackEnd.Entities.Site.Hibernate.Site;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import javax.persistence.PersistenceException;
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
        if (getPageBySiteAndSlug(newPage.getSite().getSlug(), newPage.getSlug()) != null) throw new PersistenceException();
        return (Page) super.insertTuple(newPage);
    }

    /**
     * Find Page object associated to input primary key in the database
     * @param pk
     * @return found Page
     */
    public Page getByPrimaryKey(Integer pk) {
        return (Page) super.getByPrimaryKey(pk);
    }

    /**
     * Get all objects from Page table stored in the database
     * @return List of all pages in Page table
     */
    public List<Page> getAllPages() {
        return (List<Page>) super.getAll();
    }

    /**
     * Get all pages belonging to input site in database
     * @param siteSlug
     * @return List of pages
     */
    public List<Page> getAllPagesOfSite(String siteSlug) {
        return getAllPages().stream().filter(p -> p.getSite().getSlug().equals(siteSlug)).sorted(Comparator.comparingInt(Page::getIndex)).collect(Collectors.toList());
    }

    /**
     * Find Page associated to input site and slug in the database
     * @param siteSlug
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
    public Page update(Page updatedVersion) {
        Page pageMatch = getPageBySiteAndSlug(updatedVersion.getSite().getSlug(), updatedVersion.getSlug());
        if (pageMatch != null && !pageMatch.getPrimaryKey().equals(updatedVersion.getPrimaryKey())) throw new PersistenceException();
        super.update(updatedVersion); return updatedVersion;
    }


}
