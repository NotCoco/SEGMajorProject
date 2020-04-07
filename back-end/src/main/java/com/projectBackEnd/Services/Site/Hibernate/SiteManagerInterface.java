package main.java.com.projectBackEnd.Services.Site.Hibernate;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by SiteManager for database queries with the 'Site' table
 */
public interface SiteManagerInterface {

    Site addSite(Site newSite);

    Site update(Site updatedVersion);

    Site getByPrimaryKey(Integer pk);

    Site getSiteBySlug(String slug);

    List<Site> getAllSites();

    void delete(Serializable primaryKey);

    void deleteAll();

}
