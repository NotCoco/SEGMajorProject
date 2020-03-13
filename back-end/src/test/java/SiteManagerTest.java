package test.java;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class SiteManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static SiteManagerInterface siteManager = null;

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        siteManager = SiteManager.getSiteManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        siteManager.deleteAll();
    }

    @Test
    public void testCreateSite() {
        Site site = new Site("Biliary Atresia");
        assertEquals(site.getName(), "Biliary Atresia");
    }

    @Test
    public void testCreateAndSaveSite() {
        siteManager.addSite("Biliary Atresia");
        assertEquals(siteManager.getAllSites().size(), 1);
    }

    @Test
    public void testCreateIllegalSite() {
        siteManager.addSite(new Site(null, null));
        assertEquals(siteManager.getAllSites().size(), 0);
    }

    @Test
    public void testFillingAndGettingSites() {
        fillDatabase();
        assertEquals(siteManager.getAllSites().size(), 8);
    }

    @Test
    public void testDuplicateName() {
        siteManager.addSite("Biliary Atresia");
        siteManager.addSite("Biliary Atresia");
        assertEquals(1, siteManager.getAllSites().size());
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithIllegalValues() {
        fillDatabase();
        Site site = new Site(siteManager.getAllSites().get(0).getPrimaryKey(), null);
        siteManager.update(site);
    }

    
    /**
     * @return array list of example site objects for database filling
     */
    private static ArrayList<Site> getListOfSites() {

        ArrayList<Site> listOfSites = new ArrayList<>();

        listOfSites.add(new Site("Disease1"));
        listOfSites.add(new Site("Disease2"));
        listOfSites.add(new Site("Disease3"));
        listOfSites.add(new Site("Disease4"));
        listOfSites.add(new Site("Disease5"));
        listOfSites.add(new Site("Disease6"));
        listOfSites.add(new Site("Disease7"));
        listOfSites.add(new Site("Disease8"));

        return listOfSites;
    }

    /**
     * Add sites to database
     */
    private void fillDatabase() {
        for (Site site : getListOfSites()) siteManager.addSite(site);
    }

}
