package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Page.Page;
import main.java.com.projectBackEnd.Entities.Page.PageManager;

import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class PageManagerTest extends PageManager {
    public static ConnectionLeakUtil connectionLeakUtil = null;
    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        deleteAll();
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        Page pageWithSlug2 = (Page) (getByPrimaryKey("Slug2"));
        assertTrue(pageWithSlug2.equals(getListOfPages().get(1)));
    }

    @Test
    public void testCreatePage() {
       Page page = new Page("biliary_atresia", 0, "Biliary Atresia", "" +
               "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
               "");
       assertEquals(page.getTitle(), "Biliary Atresia"); //TODO Hamcrest these.
    }

    @Test
    public void testCreateAndSavePage() {
       addPage("biliary_atresia", 0, "Biliary Atresia", "" +
               "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
               "");
       assertEquals(getAll().size(), 1);
    }

    @Test
    public void testSavedPage() {
        Page page = new Page("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        insertTuple(page);
        fillDatabase();
        Page pageFromDatabase = getByPrimaryKey("biliary_atresia");
        assertTrue(pageFromDatabase.equals(page));
    }

    @Test
    public void testSafeNames() {
       addPage(";DROP TABLE Pages", 2, "';'''", "sdafds");
       assertEquals(getAll().size(), 1);
    }

    @Test
    public void testEmptyContent() {
       addPage("biliary_atresia", 0, "", "");
       assertEquals(getAll().size(), 1);
    }

    //@Test(expected = PersistenceException.class)
    @Test
    public void testEmptyIndex() {
       addPage("biliary", null, "2", "1"); //Should throw something?
       addPage("biliary2", 2, "2", "1");
       assertEquals(getAll().size(), 1);
    } //TODO: Doesn't throw an error, just doesn't create?

    //@Test(expected = ConstraintViolationException.class)
    //public void testDuplicatePrimaryKey() throws ConstraintViolationException {
    @Test(expected = PersistenceException.class)
    public void testDuplicatePrimaryKey() throws PersistenceException {
        addPage("biliary_atresia", 0, "Random Title", "Content");
        addPage("biliary_atresia", 1, "Random Title 2", "Content");
    }

    @Test
    public void testGetAll() {
       fillDatabase();
       assertEquals(getAll().size(), getListOfPages().size());
    }

    @Test
    public void testIdenticalPages() {
        Page page = new Page("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        Page page2 = new Page("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        assertTrue(page.equals(page2));
    }

    @Test
    public void testDeleteAll() {
        fillDatabase();
        deleteAll();
        assertEquals(getAll().size(), 0);
    }

    @Test
    public void testDelete() {
        fillDatabase();
        delete(getListOfPages().get(1));
        assertEquals(getAll().size(), getListOfPages().size()-1);
    }

    @Test
    public void testWhichDeleted() {
        fillDatabase();
        delete(getListOfPages().get(1));
        assertNull(getByPrimaryKey(getListOfPages().get(1).getSlug()));
    }

    @Test
    public void testUpdatePage() {
        Page replacementPage = new Page("Slug3", 10, "Title3", "New content!");

        fillDatabase();
        update(replacementPage);
        Page foundPage = getByPrimaryKey("Slug3");

        assertEquals(foundPage.getContent(), replacementPage.getContent());
    }

    @Test
    public void testFindBySlug() {
        fillDatabase();
        assertTrue(getByPrimaryKey("Slug2").equals(getListOfPages().get(1)));
    }

    @Test
    public void testUnfoundPrimaryKey() {
        fillDatabase();
        assertNull(getByPrimaryKey("fakekey"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNullPrimaryKey() throws IllegalStateException {
        fillDatabase();
        assertNull(getByPrimaryKey(null));
    }
    private static ArrayList<Page> getListOfPages() {
        ArrayList<Page> listOfPages = new ArrayList<>();
        listOfPages.add(new Page("Slug1", 0, "Title1", "Content1"));
        listOfPages.add(new Page("Slug2", 8, "Title2", "Content2"));
        listOfPages.add(new Page("Slug3", 7, "Title3", "Content3"));
        listOfPages.add(new Page("Slug4", 5, "Title4", "Content4"));
        listOfPages.add(new Page("Slug5", 4, "Title5", "Content5"));
        listOfPages.add(new Page("Slug8", 4, "Title5", "Content5"));
        listOfPages.add(new Page("Slug9", 4, "Title5", "Content5"));
        listOfPages.add(new Page("Slug12", 4, "Title5", "Content5"));
        listOfPages.add(new Page("Slug17", 4, "Title5", "Content5"));
        return listOfPages;
    }
    private void fillDatabase() {
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
    }

}