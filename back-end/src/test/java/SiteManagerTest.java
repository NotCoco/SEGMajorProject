package test.java;

import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

public class SiteManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static SiteManagerInterface siteManager = null;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        siteManager = SiteManager.getSiteManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterAll
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @BeforeEach
    public void setUp() {
        siteManager.deleteAll();
    }

    @Test
    public void testCreateSite() {
        Site site = new Site("slug1", "Biliary Atresia");
        assertEquals("Biliary Atresia", site.getName());
    }

    @Test
    public void testCreateAndSaveSite() {
        siteManager.addSite("slug1", "Biliary Atresia");
        assertEquals(1, siteManager.getAllSites().size());
    }

    @Test
    public void testCreateWithIllegalValue() {
        String name = null;
        siteManager.addSite("slug2", name);
        assertEquals(0, siteManager.getAllSites().size());
    }

    @Test
    public void testEmptyName() {
        siteManager.addSite("slug1", "");
        //assertEquals("Unnamed", siteManager.getAllSites(),get(0).getName());
    }

    @Test
    public void testFillingAndGettingSites() {
        fillDatabase();
        assertEquals(getListOfSites().size(), siteManager.getAllSites().size());
    }

    @Test
    public void testDuplicateName() {
        siteManager.addSite("slug1", "Biliary Atresia");
        siteManager.addSite("slug2", "Biliary Atresia");
        assertEquals(2, siteManager.getAllSites().size());
    }

    @Test
    public void testDuplicateSlug() {
        siteManager.addSite("slug1", "Biliary Atresia");
        siteManager.addSite("slug1", "Biliary Atresia");
        assertEquals(1, siteManager.getAllSites().size());
    }

    @Test
    public void testTwoEqualSites() {
        Site site1 = new Site("s1", "Site1");
        Site site2 = new Site("s1", "Site1");
        assertThat(site1, samePropertyValuesAs(site2));
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        Site firstSite = siteManager.getAllSites().get(0);
        Site foundSite = firstSite;
        int sitePK = firstSite.getPrimaryKey();
        Site foundSiteDB = siteManager.getByPrimaryKey(sitePK);

        assertThat(foundSite, samePropertyValuesAs(foundSiteDB));
    }

    @Test
    public void testGetIllegalPrimaryKey() {
        assertNull(siteManager.getByPrimaryKey(-2));
    }

    @Test
    public void testGetByName() {
        fillDatabase();
        Site site = siteManager.getBySiteSlug("Slug2");
        assertEquals("Disease2", site.getName());
    }

    @Test
    public void testGetByNameNotInDB() {
        fillDatabase();
        Site site = siteManager.getBySiteSlug("Biliary Atresia213432");
        assertNull(site);
    }

    @Test
    public void testUpdateSite() {
        fillDatabase();
        Site oldSite = siteManager.getAllSites().get(0);
        int id = oldSite.getPrimaryKey();
        String oldSlug = oldSite.getSlug();
        Site replacementSite = new Site(id, "newSlug", "New Disease Name");
        siteManager.update(replacementSite);

        Site siteInDB = siteManager.getAllSites().get(0);
        assertEquals(replacementSite.getName(), siteInDB.getName());
        assertNull(siteManager.getBySiteSlug(oldSlug));
    }

    @Test
    public void testUpdateWithIllegalValues() {
        assertThrows(PersistenceException.class, () -> {
            fillDatabase();
            Site site = new Site(siteManager.getAllSites().get(0).getPrimaryKey(),"slug1", null);
            siteManager.update(site);
        });
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
        Site site = new Site("nop", "Not in db");
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

        listOfSites.add(new Site("Slug1", "Disease1"));
        listOfSites.add(new Site("Slug2", "Disease2"));
        listOfSites.add(new Site("Slug3", "Disease3"));
        listOfSites.add(new Site("Slug4", "Disease4"));
        listOfSites.add(new Site("Slug5", "Disease5"));
        listOfSites.add(new Site("Slug6", "Disease6"));
        listOfSites.add(new Site("Slug7", "Disease7"));
        listOfSites.add(new Site("Slug8", "Disease8"));

        return listOfSites;
    }

    /**
     * Add sites to database
     */
    private void fillDatabase() {
        ArrayList<Site> listOfSites = getListOfSites();
        for (int i = 0; i<listOfSites.size(); ++i) siteManager.addSite(listOfSites.get(i));
    }

}
