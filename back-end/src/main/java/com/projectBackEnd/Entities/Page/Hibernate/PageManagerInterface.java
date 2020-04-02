package main.java.com.projectBackEnd.Entities.Page.Hibernate;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by PageManager for database queries with the Page table.
 */

public interface PageManagerInterface {

    public Page addPage(Page newPage);

    public Page update(Page updatedVersion);

    public Page getByPrimaryKey(Integer pk);

    public List<Page> getAllPagesOfSite(String siteSlug);

    public Page getPageBySiteAndSlug(String siteSlug, String slug);

    public List<Page> getAllPages();

    public void delete(Serializable primaryKey);

    public void deleteAll();

}