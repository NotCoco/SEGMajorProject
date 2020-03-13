package main.java.com.projectBackEnd.Entities.Page;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PageManager extends EntityManager implements PageManagerInterface {
    private static PageManagerInterface pageManager;
    private PageManager() {
        super();
        setSubclass(Page.class);
        HibernateUtility.addAnnotation(Page.class);
        pageManager = this;
    }
    public static PageManagerInterface getPageManager() {
        if (pageManager != null) return pageManager;
        else return new PageManager();
    }


    public List<Page> getAllPages() {
        return (List<Page>) super.getAll();
    }
    public Page addPage(Page newPage) {
        return (Page) super.insertTuple(newPage);
    }
    public Page getByPrimaryKey(Integer pk) {
        return (Page) super.getByPrimaryKey(pk);
    } //TODO Useless likely, aswell.

    public void delete(Page object) {
        super.delete(object);
    } //TODO Is unnecessary now, can be removed
    public Page update(Page updatedVersion) { super.update(updatedVersion); return updatedVersion; }
    public Page addPage(Site site, String slug, int index, String title, String content) { return (Page) insertTuple(new Page(site, slug, index, title, content)); }
    public Page addPage(String siteName, String slug, int index, String title, String content) { return (Page) insertTuple(new Page(siteName, slug, index, title, content)); }
    public List<Page> getAllPagesOfSite(Site site) { return getAllPagesOfSite(site.getName()); }
    public List<Page> getAllPagesOfSite(String siteName) {
        return pageManager.getAllPages().stream().filter(p -> p.getSite().getName().equals(siteName)).sorted(Comparator.comparingInt(Page::getIndex)).collect(Collectors.toList());
    }

}
