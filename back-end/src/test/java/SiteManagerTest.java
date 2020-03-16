package test.java;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SiteManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static SiteManagerInterface siteManager = null;

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("test/resources/testhibernate.cfg.xml");
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
        assertEquals("Biliary Atresia", site.getName());
    }

    @Test
    public void testCreateAndSaveSite() {
        siteManager.addSite("Biliary Atresia");
        assertEquals(1, siteManager.getAllSites().size());
    }

//    @Test
//    public void testCreateWithIllegalValues() {
//        String name = null;
//        siteManager.addSite(new Site(null, null));
//        siteManager.addSite(name);
//        assertEquals(0, siteManager.getAllSites().size());
//    }

    @Test
    public void testEmptyName() {
        siteManager.addSite("");
        //assertEquals("Unnamed", siteManager.getAllSites(),get(0).getName());
    }

    @Test
    public void testFillingAndGettingSites() {
        fillDatabase();
        assertEquals(getListOfSites().size(), siteManager.getAllSites().size());
    }

    @Test
    public void testDuplicateName() {
        siteManager.addSite("Biliary Atresia");
        siteManager.addSite("Biliary Atresia");
        assertEquals(1, siteManager.getAllSites().size());
    }

    @Test
    public void testTwoEqualSites() {
        Site site1 = new Site("Site1");
        Site site2 = new Site("Site1");
        assertTrue(site1.equals(site2));
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        Site firstSite = siteManager.getAllSites().get(0);
        Site foundSite = firstSite;
        int sitePK = firstSite.getPrimaryKey();
        Site foundSiteDB = siteManager.getByPrimaryKey(sitePK);

        assertThat(foundSite, samePropertyValuesAs(foundSiteDB));
        assertTrue(foundSite.equals((foundSiteDB)));
    }

    @Test
    public void testGetIllegalPrimaryKey() {
        assertNull(siteManager.getByPrimaryKey(-1));
    }

    @Test
    public void testGetByName() {
        fillDatabase();
        Site site = siteManager.getBySiteName("Disease2");
        assertEquals("Disease2", site.getName());
    }

    @Test
    public void testGetByNameNotInDB() {
        fillDatabase();
        Site site = siteManager.getBySiteName("Biliary Atresia");
        assertNull(site);
    }

    @Test
    public void testUpdateSite() {
        fillDatabase();
        int id = siteManager.getAllSites().get(0).getPrimaryKey();
        Site replacementSite = new Site(id, "New Disease Name");
        siteManager.update(replacementSite);

        Site siteInDB = siteManager.getAllSites().get(0);
        assertEquals(replacementSite.getName(), siteInDB.getName());
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithIllegalValues() {
        fillDatabase();
        Site site = new Site(siteManager.getAllSites().get(0).getPrimaryKey(), null);
        siteManager.update(site);
    }

    @Test
    public void testDeleteAll() {
        // Delete all from filled database
        fillDatabase();
        siteManager.deleteAll();
        assertEquals(0, siteManager.getAllSites().size());
        // Delete all from empty database
        siteManager.deleteAll();
        assertEquals(0, siteManager.getAllSites().size());
    }

    @Test
    public void testDelete() {
        fillDatabase();
        siteManager.delete(siteManager.getAllSites().get(0)); //Testing object deletion
        assertEquals(getListOfSites().size()-1, siteManager.getAllSites().size());
        siteManager.delete(siteManager.getAllSites().get(0));
        assertEquals(getListOfSites().size()-2, siteManager.getAllSites().size());
    }

    @Test
    public void testDeleteByPK() {
        fillDatabase();
        siteManager.delete(siteManager.getAllSites().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals( getListOfSites().size()-1,siteManager.getAllSites().size());
    }

    @Test
    public void testWithDeleteIllegalPK() {
        int numberOfSites = siteManager.getAllSites().size();
        try {
            siteManager.delete(-1);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Check that nothing has been removed
            assertEquals(numberOfSites, siteManager.getAllSites().size());
        }
    }

    @Test
    public void testDeleteNotInDBObject() {
        Site site = new Site("Not in db");
        int numberOfSites = siteManager.getAllSites().size();
        try {
            siteManager.delete(site);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Check that nothing has been removed
            assertEquals(numberOfSites, siteManager.getAllSites().size());
        }
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
