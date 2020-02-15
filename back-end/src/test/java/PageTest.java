package test.java;

import main.java.com.projectBackEnd.DatabaseInitialiser;
import main.java.com.projectBackEnd.Page;
import main.java.com.projectBackEnd.PageManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.*;


public class PageTest extends PageManager {
   @Override
    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().
                addAnnotatedClass(Page.class)
                .configure("testhibernate.cfg.xml");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
        return sessionFactory;
    }
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
}