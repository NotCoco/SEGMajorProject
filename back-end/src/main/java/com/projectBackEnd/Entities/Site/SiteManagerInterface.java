package main.java.com.projectBackEnd.Entities.Site;

import main.java.com.projectBackEnd.Entities.Page.Page;

import java.io.Serializable;
import java.util.List;

public interface SiteManagerInterface {
    public List<Site> getAllSites();

    public void deleteAll();
    public Site addSite(Site newSite);

    public void delete(Serializable primaryKey);

    public Site getByPrimaryKey(Integer pk);
    public Site getBySiteSlug(String slug);
    public Site update(Site updatedVersion);

    }
