package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.OldPage.OldPage;

import main.java.com.projectBackEnd.Entities.OldPage.OldPageManager;
import main.java.com.projectBackEnd.Entities.OldPage.OldPageManagerInterface;
import org.junit.*;


import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class OldPageManagerTest {
   public static ConnectionLeakUtil connectionLeakUtil = null;
   public static OldPageManagerInterface pageManager = null;
    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("test/resources/testhibernate.cfg.xml");
        pageManager = OldPageManager.getOldPageManager();
       connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        pageManager.deleteAll();
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        OldPage pageWithSlug2 = (OldPage) (pageManager.getByPrimaryKey("Slug2"));
        assertTrue(pageWithSlug2.equals(getListOfPages().get(1)));
    }

    @Test
    public void testCreateOldPage() {
       OldPage page = new OldPage("biliary_atresia", 0, "Biliary Atresia", "" +
               "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
               "");
       assertEquals(page.getTitle(), "Biliary Atresia"); //TODO Hamcrest these.
    }
    @Test
    public void testGetNonexistentPrimaryKey() {
        assertNull(pageManager.getByPrimaryKey(""));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNonexistentPrimaryKey() {
        pageManager.delete("");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNotInDBObject() {
        OldPage pageNotInTable = new OldPage("notaddedtotable", 0, "notaddedtoTable", "");
        pageManager.delete(pageNotInTable);
    }
    @Test(expected = PersistenceException.class)
    public void testUpdateWithBadData() {
        OldPage badPage = new OldPage("Slug1", null, null, null);
        fillDatabase();
        pageManager.update(badPage);
    }
    @Test
    public void testDeleteByPrimaryKey() {
        fillDatabase();
        pageManager.delete(getListOfPages().get(1).getPrimaryKey());
        assertNull(pageManager.getByPrimaryKey(getListOfPages().get(1).getPrimaryKey()));
    }
    @Test
    public void testCreateAndSavePage() {
        pageManager.addOldPage("biliary_atresia", 0, "Biliary Atresia", "" +
               "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
               "");
       assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testSavedPage() {
        OldPage page = new OldPage("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        pageManager.addOldPage(page);
        fillDatabase();
        OldPage pageFromDatabase = pageManager.getByPrimaryKey("biliary_atresia");
        assertTrue(pageFromDatabase.equals(page));
    }

    @Test
    public void testSafeNames() {
        pageManager.addOldPage(";DROP TABLE Pages", 2, "';'''", "sdafds");
       assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testEmptyContent() {
        pageManager.addOldPage("biliary_atresia", 0, "", "");
       assertEquals(pageManager.getAllPages().size(), 1);
    }

    //@Test(expected = PersistenceException.class)
    @Test
    public void testEmptyIndex() {
        pageManager.addOldPage("biliary", null, "2", "1"); //Should throw something?
        pageManager.addOldPage("biliary2", 2, "2", "1");
       assertEquals(pageManager.getAllPages().size(), 1);
    } //TODO: Doesn't throw an error, just doesn't create?

    //@Test(expected = ConstraintViolationException.class)
    //public void testDuplicatePrimaryKey() throws ConstraintViolationException {
    @Test(expected = PersistenceException.class)
    public void testDuplicatePrimaryKey() throws PersistenceException {
        pageManager.addOldPage("biliary_atresia", 0, "Random Title", "Content");
        pageManager.addOldPage("biliary_atresia", 1, "Random Title 2", "Content");
    }

    @Test
    public void testGetAll() {
       fillDatabase();
       assertEquals(pageManager.getAllPages().size(), getListOfPages().size());
    }

    @Test
    public void testIdenticalPages() {
        OldPage page = new OldPage("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        OldPage page2 = new OldPage("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        assertTrue(page.equals(page2));
    }

    @Test
    public void testDeleteAll() {
        fillDatabase();
        pageManager.deleteAll();
        assertEquals(pageManager.getAllPages().size(), 0);
    }

    @Test
    public void testDelete() {
        fillDatabase();
        pageManager.delete(getListOfPages().get(1));
        assertEquals(pageManager.getAllPages().size(), getListOfPages().size()-1);
    }

    @Test
    public void testWhichDeleted() {
        fillDatabase();
        pageManager.delete(getListOfPages().get(1));
        assertNull(pageManager.getByPrimaryKey(getListOfPages().get(1).getPrimaryKey()));
    }

    @Test
    public void testUpdatePage() {
        OldPage replacementPage = new OldPage("Slug3", 10, "Title3", "New content!");

        fillDatabase();
        pageManager.update(replacementPage);
        OldPage foundPage = pageManager.getByPrimaryKey("Slug3");

        assertEquals(foundPage.getContent(), replacementPage.getContent());
    }

    @Test
    public void testFindBySlug() {
        fillDatabase();
        assertTrue(pageManager.getByPrimaryKey("Slug2").equals(getListOfPages().get(1)));
    }

    @Test
    public void testUnfoundPrimaryKey() {
        fillDatabase();
        assertNull(pageManager.getByPrimaryKey("fakekey"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNullPrimaryKey() throws IllegalStateException {
        fillDatabase();
        assertNull(pageManager.getByPrimaryKey(null));
    }
    private static ArrayList<OldPage> getListOfPages() {
        ArrayList<OldPage> listOfPages = new ArrayList<>();
        listOfPages.add(new OldPage("Slug1", 0, "Title1", "Content1"));
        listOfPages.add(new OldPage("Slug2", 8, "Title2", "Content2"));
        listOfPages.add(new OldPage("Slug3", 7, "Title3", "Content3"));
        listOfPages.add(new OldPage("Slug4", 5, "Title4", "Content4"));
        listOfPages.add(new OldPage("Slug5", 4, "Title5", "Content5"));
        listOfPages.add(new OldPage("Slug8", 4, "Title5", "Content5"));
        listOfPages.add(new OldPage("Slug9", 4, "Title5", "Content5"));
        listOfPages.add(new OldPage("Slug12", 4, "Title5", "Content5"));
        listOfPages.add(new OldPage("Slug17", 4, "Title5", "Content5"));
        return listOfPages;
    }
    private void fillDatabase() {
        for (OldPage p : getListOfPages()) {
            pageManager.addOldPage(p);
        }
    }

}