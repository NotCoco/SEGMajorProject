package main.java.com.projectBackEnd.Entities.Site.Hibernate;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by SiteManager for database queries with the 'Site' table
 */
public interface SiteManagerInterface {

    public Site addSite(Site newSite);

    public Site update(Site updatedVersion);

    public Site getByPrimaryKey(Integer pk);

    public Site getSiteBySlug(String slug);

    public List<Site> getAllSites();

    public void delete(Serializable primaryKey);

    public void deleteAll();

}
