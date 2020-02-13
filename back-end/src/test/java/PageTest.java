package test.java;

import main.java.com.projectBackEnd.Util.HibernateUtil;

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

// Just use Session session = HibernateUtil.buildSessionFactory(Page.class) to create session

    @Test
    public void testCorrectTableCreated() {
        // assertThat(Database . getTableName, equalTo("randomTableName) );
    }
}