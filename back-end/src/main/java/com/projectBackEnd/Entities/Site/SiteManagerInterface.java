package main.java.com.projectBackEnd.Entities.Site;

import main.java.com.projectBackEnd.Entities.Page.Page;

import java.io.Serializable;
import java.util.List;

public interface SiteManagerInterface {

    public void delete(Serializable primaryKey);
    public void deleteAll();

    public List<Site> getAllSites();
    public Site addSite(Site newSite);
    public Site getByPrimaryKey(Integer pk);
    public Site getBySiteSlug(String slug);
    public Site update(Site updatedVersion);
    public Site addSite(String slug, String name);
    }
