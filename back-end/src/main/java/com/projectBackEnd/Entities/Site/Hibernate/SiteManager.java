package main.java.com.projectBackEnd.Entities.Site.Hibernate;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import java.util.List;
import java.util.stream.Collectors;

public class SiteManager extends EntityManager implements SiteManagerInterface {

    private static SiteManagerInterface siteManager;

    private SiteManager() {
        super();
        setSubclass(Site.class);
        HibernateUtility.addAnnotation(Site.class);
        siteManager = this;
    }

    public static SiteManagerInterface getSiteManager() {
        if (siteManager==null) return new SiteManager();
        else return siteManager;
    }

    public List<Site> getAllSites() {
        return (List<Site>) super.getAll();
    }

    public Site addSite(Site newSite) {
        super.insertTuple(newSite);
        return newSite;
    }

    public Site getByPrimaryKey(Integer pk) {
        return (Site) super.getByPrimaryKey(pk);
    }
    public Site getBySiteSlug(String siteSlug) {
        List<Site> matches = getAllSites().stream().filter(s -> s.getSlug().equals(siteSlug)).collect(Collectors.toList());
        if (matches.size() >= 1) return matches.get(0);
        else return null;
    }

    public Site update(Site updatedVersion) {
        return (Site) super.update(updatedVersion);
    }

}