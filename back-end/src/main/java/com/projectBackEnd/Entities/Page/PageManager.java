package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;

import java.io.Serializable;
import java.util.List;

public class PageManager implements PageManagerInterface {
    public void delete(Serializable primaryKey) {}
    public void deleteAll() {}

    public List<Page> getAllPage() { return null; }
    public Page addPage(Page newPage) { return null; }
    public Page getByPrimaryKey(Serializable pk) { return null; }
    public void delete(Page object) {} //TODO Is unnecessary now, can be removed
    public Page update(Page updatedVersion) { return null; }
    public Page addPage(Site siteName, String slug, int Index, String title, String content) { return null; }
    public Page addPage(String siteName, String slug, int Index, String title, String content) { return null; }
    public List<Page> getAllPagesOfSite(String siteName) { return null; } //I'm working on it! Act like it exists for now P:
    public List<Page> getAllPagesOfSite(Site site) { return null; } //I'm working on it! Act like it exists for now P:

}
