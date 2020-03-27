package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;

import java.io.Serializable;
import java.util.List;

public interface PageManagerInterface {
    public void delete(Serializable primaryKey);
    public void deleteAll();

    public List<Page> getAllPages();
    public Page addPage(Page newPage);
    public Page getByPrimaryKey(Integer pk);
    public Page update(Page updatedVersion);
    public Page addPage(String siteName, String slug, Integer Index, String title, String content);
    public Page addPage(Site siteName, String slug, Integer Index, String title, String content);
    public List<Page> getAllPagesOfSite(String siteName);
    public List<Page> getAllPagesOfSite(Site site);
    public Page getPageBySiteAndSlug(String siteName, String slug);
    public Page getPageBySiteAndSlug(Site site, String slug);


}
