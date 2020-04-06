package main.java.com.projectBackEnd.Entities.Page.Hibernate;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by PageManager for database queries with the Page table.
 */

public interface PageManagerInterface {

    Page addPage(Page newPage);

    Page update(Page updatedVersion);

    Page getByPrimaryKey(Integer pk);

    List<Page> getAllPagesOfSite(String siteSlug);

    Page getPageBySiteAndSlug(String siteSlug, String slug);

    List<Page> getAllPages();

    void delete(Serializable primaryKey);

    void deleteAll();

}