package test.java;

import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.Page;
import main.java.com.projectBackEnd.PageManager;
import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class PageManagerTest extends PageManager {

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setLocation("testhibernate.cfg.xml");
    }

    @Before
    public void setUp() { //For this run it will use the same DatabaseInitialiser object, right? Won't interfere
                            // With existing running ones if I were to run it with a different DB / change the object?
        //String[] databaseInfo = {}; //Size 0 since it will use the default from the DBInitialiser class.
        //DatabaseInitialiser.main(databaseInfo);
        //TODO : Now unnecessary, delete.
        deleteAll();
    }
    @After
    public void tearDown() {
       //DatabaseInitialiser.dropAllTables();
       deleteAll();
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
       Page page = createAndSavePage("biliary_atresia", 0, "Biliary Atresia", "" +
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
        Page pageFromDatabase = findBySlug("biliary_atresia");
        assertEquals(pageFromDatabase.getContent(), page.getContent());
    }

    @Test
    public void testSafeNames() {
       createAndSavePage(";DROP TABLE Pages", 2, "';'''", "sdafds");
       assertEquals(getAll().size(), 1);
       System.out.println(getAll().get(0));
    }

    @Test
    public void testEmptyContent() {
       createAndSavePage("biliary_atresia", 0, "", "");
       assertEquals(getAll().size(), 1);
    }

    //@Test(expected = ConstraintViolationException.class)
    //public void testEmptyIndex() throws ConstraintViolationException {
    //@Test(expected = PersistenceException.class)
    @Test
    public void testEmptyIndex() {
       createAndSavePage("biliary", null, "2", "1"); //Should throw something?
       assertEquals(getAll().size(), 0);
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
       for (Page p : getListOfPages()) {
           insertTuple(p);
       }
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
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        deleteAll();
        assertEquals(getAll().size(), 0);
    }
    /*@Test
    public void testDeleteAllCascade() {
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        deleteAllCascade();
        assertEquals(getAll().size(), 0);
    }*/
    @Test
    public void testDelete() {
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        delete(getListOfPages().get(1));
        assertEquals(getAll().size(), getListOfPages().size()-1);
    }

    @Test
    public void testWhichDeleted() {
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        delete(getListOfPages().get(1));
        assertNull(findBySlug(getListOfPages().get(1).getSlug()));
    }

    @Test
    public void testUpdatePage() {
        Page replacementPage = createPage("Slug3", 10, "Title3", "New content!");

        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        update(replacementPage);
        Page foundPage = findBySlug("Slug3");

        assertEquals(foundPage.getContent(), replacementPage.getContent());
    }

    @Test
    public void testFindBySlug() {

        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        assertTrue(findBySlug("Slug2").equals(getListOfPages().get(1)));
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

}