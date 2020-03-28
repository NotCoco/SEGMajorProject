package main.java.com.projectBackEnd.Entities.Site.Hibernate;

import java.io.Serializable;
import java.util.List;

public interface SiteManagerInterface {
    public List<Site> getAllSites();

    public void deleteAll();
    public Site addSite(Site newSite);

    public void delete(Serializable primaryKey);

    public Site getByPrimaryKey(Integer pk);
    public Site getSiteBySlug(String slug);
    public Site update(Site updatedVersion);

    }
