package main.java.com.projectBackEnd.Entities.Site;

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

    public void delete(Site site) {
        super.delete(site);
    }

    public List<Site> getAllSites() {
        return (List<Site>) super.getAll();
    }

    public Site addSite(String name) {
        Site newSite = new Site(name);
        super.insertTuple(newSite);
        return newSite;
    }

    public Site addSite(Site newSite) {
        super.insertTuple(newSite);
        return newSite;
    }

    public Site getByPrimaryKey(Integer pk) {
        return (Site) super.getByPrimaryKey(pk);
    }
    public Site getBySiteName(String siteName) {
        List<Site> matches = getAllSites().stream().filter(s -> s.getName().equals(siteName)).collect(Collectors.toList());
        if (matches.size() >= 1) return matches.get(0);
        else return null;
    }

    public Site update(Site updatedVersion) {
        return (Site) super.update(updatedVersion);
    }

}
