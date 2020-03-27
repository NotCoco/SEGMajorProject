package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;

import java.io.Serializable;
import java.util.List;

public interface PageManagerInterface {
    public List<Page> getAllPages();
    public List<Page> getAllPagesOfSite(String siteSlug);
    public void deleteAll();
    public Page addPage(Page newPage);
    public Page getByPrimaryKey(Integer pk);
    public Page getPageBySiteAndSlug(String siteSlug, String slug);
    public void delete(Serializable primaryKey);
    public Page update(Page updatedVersion);

}
