package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtility {

    public static String location = "";

    /**
     * Gets the session factory created in this case specifically for the Page class
     * @return The session factory.
     */
    public static SessionFactory getSessionFactory(Class c) {
        Configuration configuration = new Configuration();
        if (location.length() > 0) {
            configuration.addAnnotatedClass(c).configure(location);
        } else {
            configuration.addAnnotatedClass(c).configure(); //TODO These two lines need to be dynamic, controlling location of DB and class
        }
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
        return sessionFactory;
    }

    public static void setLocation(String locationIn) {
        location = locationIn;
    }
}
