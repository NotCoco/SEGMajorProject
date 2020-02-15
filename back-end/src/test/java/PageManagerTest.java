package test.java;

import main.java.com.projectBackEnd.DatabaseInitialiser;
import main.java.com.projectBackEnd.Page;
import main.java.com.projectBackEnd.PageManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class PageManagerTest extends PageManager {
   /*@Override
    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().
                addAnnotatedClass(Page.class)
                .configure("testhibernate.cfg.xml");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
        return sessionFactory;
    }*/
    @Before
    public void setUp() { //For this run it will use the same DatabaseInitialiser object, right? Won't interfere
                            // With existing running ones if I were to run it with a different DB / change the object?
        String[] databaseInfo = {}; //Size 0 since it will use the default from the DBInitialiser class.
        DatabaseInitialiser.main(databaseInfo);
    }
    @After
    public void tearDown() {
       DatabaseInitialiser.dropAllTables();
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
       Page page = createAndSavePage(";DROP TABLE Pages", 2, "';'''", "sdafds");
       assertEquals(getAll().size(), 1);
    }

    @Test
    public void testEmptyContent() {
       Page page = createAndSavePage("biliary_atresia", 0, "", "");
       assertEquals(getAll().size(), 1);
    }

    //@Test(expected = ConstraintViolationException.class)
    //public void testEmptyIndex() throws ConstraintViolationException {
    @Test(expected = PersistenceException.class)
    public void testEmptyIndex() throws PersistenceException {
       Page page = createAndSavePage("biliary", null, "2", "1"); //Should throw something?
       assertEquals(getAll().size(), 0);
    }

    //@Test(expected = ConstraintViolationException.class)
    //public void testDuplicatePrimaryKey() throws ConstraintViolationException {
    @Test(expected = PersistenceException.class)
    public void testDuplicatePrimaryKey() throws PersistenceException {
        Page page = createAndSavePage("biliary_atresia", 0, "Random Title", "Content");
        Page page2 = createAndSavePage("biliary_atresia", 1, "Random Title 2", "Content");
    }

    @Test
    public void testGetAll() {
       for (Page p : getListOfPages()) {
           insertTuple(p);
       }
       assertEquals(getAll().size(), getListOfPages().size());
    }

    @Test
    public void testDeleteAll() {
        for (Page p : getListOfPages()) {
            insertTuple(p);
        }
        deleteAll();
        assertEquals(getAll().size(), 0);
    }

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
        assertEquals(findBySlug("Slug2").getTitle(), "Title2");
    }

    private static ArrayList<Page> getListOfPages() {
        ArrayList<Page> listOfPages = new ArrayList<>();
        listOfPages.add(new Page("Slug1", 0, "Title1", "Content1"));
        listOfPages.add(new Page("Slug2", 8, "Title2", "Content2"));
        listOfPages.add(new Page("Slug3", 7, "Title3", "Content3"));
        listOfPages.add(new Page("Slug4", 5, "Title4", "Content4"));
        listOfPages.add(new Page("Slug5", 4, "Title5", "Content5"));
        return listOfPages;
    }

    @Test
    public void theirTest() {

        Page em1 = new Page("Mary Smith", 25, "t1", "c1");
        Page em2 = new Page("John Aces", 32, "t1", "c1");
        Page em3 = new Page("Ian Young", 29, "t1", "c1");

        System.out.println(" =======CREATE =======");
        insertTuple(em1);
        insertTuple(em2);
        insertTuple(em3);
        System.out.println(" =======READ =======");
        List<Page> ems1 = getAll();
        for(Page e: ems1) {
            System.out.println(e.toString());
        }
        System.out.println(" =======UPDATE =======");
        em1.setIndex(44);
        em1.setContent("Mary Rose");
        update(em1);
        System.out.println(" =======READ =======");
        List<Page> ems2 = getAll();
        for(Page e: ems2) {
            System.out.println(e.toString());
        }
        System.out.println(" =======DELETE ======= ");
        delete(em2);
        System.out.println(" =======READ =======");
        List<Page> ems3 = getAll();
        for(Page e: ems3) {
            System.out.println(e.toString());
        }
        System.out.println(" =======DELETE ALL ======= ");
        deleteAll();
        System.out.println(" =======READ =======");
        List<Page> ems4 = getAll();
        for(Page e: ems4) {
            System.out.println(e.toString());
        }
    }
}