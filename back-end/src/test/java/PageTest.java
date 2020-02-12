package test.java;

import main.java.com.projectBackEnd.Page;
import main.java.com.projectBackEnd.PageManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    @Test
    public void testCorrectTableCreated() {
        // assertThat(Database . getTableName, equalTo("randomTableName) );
    }
}