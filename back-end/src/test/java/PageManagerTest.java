package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Page.PageManager;
import main.java.com.projectBackEnd.Page;
import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class PageManagerTest extends PageManager {

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setLocation("testhibernate.cfg.xml");
    }

    public static ConnectionLeakUtil connectionLeakUtil = null;
    @BeforeClass
    public static void initConnectionLeakUtility() {
        if ( true ) {
            connectionLeakUtil = new ConnectionLeakUtil();
        }
    }

    @AfterClass
    public static void assertNoLeaks() {
        if ( true ) {
            connectionLeakUtil.assertNoLeaks();
        }
    }

    @Before
    public void setUp() { //For this run it will use the same DatabaseInitialiser object, right? Won't interfere
                            // With existing running ones if I were to run it with a different DB / change the object?
        //String[] databaseInfo = {}; //Size 0 since it will use the default from the DBInitialiser class.
        //DatabaseInitialiser.main(databaseInfo);
        //TODO : Now unnecessary, delete.
        //deleteAll();
    }
    @After
    public void tearDown() {

       deleteAll();
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        Page pageWithSlug2 = (Page) (EntityManager.getByPrimaryKey(Page.class, "Slug2"));
        assertTrue(pageWithSlug2.equals(getListOfPages().get(1)));
    }

    @Test
    public void testCreatePage() {
       Page page = createPage("biliary_atresia", 0, "Biliary Atresia", "" +
               "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
               "");
       assertEquals(page.getTitle(), "Biliary Atresia"); //TODO Hamcrest these.
    }

    @Test
    public void testCreateAndSavePage() {
       createAndSavePage("biliary_atresia", 0, "Biliary Atresia", "" +
               "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
               "");
       assertEquals(getAll().size(), 1);
    }

    @Test
    public void testSavedPage() {
        Page page = createPage("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        insertTuple(page);
        fillDatabase();
        Page pageFromDatabase = findBySlug("biliary_atresia");
        //assertEquals(pageFromDatabase.getContent(), page.getContent());
        assertTrue(pageFromDatabase.equals(page));
    }

    @Test
    public void testSafeNames() {
       createAndSavePage(";DROP TABLE Pages", 2, "';'''", "sdafds");
       assertEquals(getAll().size(), 1);
    }

    @Test
    public void testEmptyContent() {
       createAndSavePage("biliary_atresia", 0, "", "");
       assertEquals(getAll().size(), 1);
    }

    //@Test(expected = PersistenceException.class)
    @Test
    public void testEmptyIndex() {
       createAndSavePage("biliary", null, "2", "1"); //Should throw something?
       createAndSavePage("biliary2", 2, "2", "1");
       assertEquals(getAll().size(), 1);
    } //TODO: Doesn't throw an error, just doesn't create?

    //@Test(expected = ConstraintViolationException.class)
    //public void testDuplicatePrimaryKey() throws ConstraintViolationException {
    @Test(expected = PersistenceException.class)
    public void testDuplicatePrimaryKey() throws PersistenceException {
        createAndSavePage("biliary_atresia", 0, "Random Title", "Content");
        createAndSavePage("biliary_atresia", 1, "Random Title 2", "Content");
    }

    @Test
    public void testGetAll() {
       fillDatabase();
       assertEquals(getAll().size(), getListOfPages().size());
    }

    @Test
    public void testIdenticalPages() {
        Page page = createPage("biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        Page page2 = createPage("biliary_atresia", 0, "Biliary Atresia", "" +
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
        assertNull(findBySlug(getListOfPages().get(1).getSlug()));
    }

    @Test
    public void testUpdatePage() {
        Page replacementPage = createPage("Slug3", 10, "Title3", "New content!");

        fillDatabase();
        update(replacementPage);
        Page foundPage = findBySlug("Slug3");

        assertEquals(foundPage.getContent(), replacementPage.getContent());
    }

    @Test
    public void testFindBySlug() {
        fillDatabase();
        assertTrue(findBySlug("Slug2").equals(getListOfPages().get(1)));
    }

    @Test
    public void testUnfoundPrimaryKey() {
        fillDatabase();
        assertNull(findBySlug("fakekey"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNullPrimaryKey() throws IllegalStateException {
        fillDatabase();
        assertNull(findBySlug(null));
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
    private static void fillDatabase() {
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
    }

}