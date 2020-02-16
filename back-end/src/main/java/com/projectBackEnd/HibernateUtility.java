package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtility {

    public static String location = "";
    public static Class entityclass;
    /**
     * Gets the session factory created in this case specifically for the Page class
     * @return The session factory.
     */
    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        if (location.length() > 0) {
            configuration.addAnnotatedClass(entityclass).configure(location);
        } else {
            configuration.addAnnotatedClass(entityclass).configure(); //TODO These two lines need to be dynamic, controlling location of DB and class
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
    public static void setEntityClass(Class newEntityClass) {
        assert TableEntity.class.isAssignableFrom(newEntityClass);
        entityclass = newEntityClass;
    }
}
