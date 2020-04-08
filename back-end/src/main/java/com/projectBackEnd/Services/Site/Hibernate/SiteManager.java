package main.java.com.projectBackEnd.Services.Site.Hibernate;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
/**
 * SiteManager defines methods to interact with the Site table in the database.
 * This class extends the EntityManager - supplying it with the rest of its interface methods.
 *
 * Inspiration : https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class SiteManager extends EntityManager implements SiteManagerInterface {

    private static SiteManagerInterface siteManager;

    /**
     * Private constructor (Singleton design pattern)
     */
    private SiteManager() {
        super();
        setSubclass(Site.class);
        HibernateUtility.addAnnotation(Site.class);
        siteManager = this;
    }
	
    /**
     * Get the site manager interface
     * @return siteManager ; if none has been defined, create a new SiteManager()
     */
    public static SiteManagerInterface getSiteManager() {
        if (siteManager==null) return new SiteManager();
        else return siteManager;
    }


    /**
     * Insert a Site object into the database
     * @param newSite Site to add to the database
     * @return added object
     */
    public Site addSite(Site newSite) {
        if (getSiteBySlug(newSite.getSlug()) != null || !Site.checkValidity(newSite)) throw new PersistenceException();
        super.insertTuple(newSite);

        return newSite;
    }

    /**
     * Update the Site's attributes
     * @param updatedVersion Site with updated attributed
     * @return updated Site
     */
    public Site update(Site updatedVersion) {

        Site foundSiteMatch = getSiteBySlug(updatedVersion.getSlug());
        if ((foundSiteMatch != null && !foundSiteMatch.getPrimaryKey().equals(updatedVersion.getPrimaryKey())) || !Site.checkValidity(updatedVersion))
            throw new PersistenceException();

        super.update(updatedVersion);
        return updatedVersion;

    }


    /**
     * Retrieve Site object corresponding to the given primary key from the database
     * @param pk    Primary key of the Site to find in the database
     * @return  found Site object
     */
    public Site getByPrimaryKey(Integer pk) {
        return (Site) super.getByPrimaryKey(pk);
    }
	
    /**
     * Retrieve Site object corresponding to the given site slug from the database
     * @param siteSlug  Slug of the site to find in the database
     * @return found Site object
     */	
    public Site getSiteBySlug(String siteSlug) {
        List<Site> matches = getAllSites().stream().filter(s -> s.getSlug().equals(siteSlug)).collect(Collectors.toList());
        if (matches.size() >= 1) return matches.get(0);
        else return null;
    }

    /**
     * Get a list of all Sites from the Site table in the database
     * @return List of all Sites in the database
     */
    public List<Site> getAllSites() {
        return (List<Site>) super.getAll();
    }



}
