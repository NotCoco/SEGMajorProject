package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;

import java.io.Serializable;
import java.util.List;

public interface PageManagerInterface {
    //TODO Technically these are shared two methods that could come from a super Interface.
    public void delete(Serializable primaryKey);
    public void deleteAll();

    public List<Page> getAllPage();
    public Page addPage(Page newPage);
    public Page getByPrimaryKey(Integer pk); //TODO In the event that this is not used, we violate Interface Segregation
    public void delete(Page object); //TODO Is unnecessary now, can be removed
    public Page update(Page updatedVersion);
    public Page addPage(String siteName, String slug, int Index, String title, String content);
    public Page addPage(Site siteName, String slug, int Index, String title, String content);
    public List<Page> getAllPagesOfSite(String siteName); //I'm working on it! Act like it exists for now P:
    public List<Page> getAllPagesOfSite(Site site); //I'm working on it! Act like it exists for now P:

}
