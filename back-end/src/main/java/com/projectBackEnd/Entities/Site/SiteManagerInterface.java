package main.java.com.projectBackEnd.Entities.Site;

import main.java.com.projectBackEnd.Entities.Page.Page;

import java.io.Serializable;
import java.util.List;

public interface SiteManagerInterface {
    //TODO Technically these are shared two methods that could come from a super Interface.
    public void delete(Serializable primaryKey);
    public void deleteAll();

    public List<Site> getAllSites();
    public Site addSite(Site newSite);
    public Site getByPrimaryKey(Integer pk);
    public Site getBySiteName(String name);
    public void delete(Site object); //TODO Is unnecessary now, can be removed
    public Site update(Site updatedVersion);
    public Site addSite(String name);
    public List<Page> getAllPagesOfSite(); //I'm working on it! Act like it exists for now P:

}
