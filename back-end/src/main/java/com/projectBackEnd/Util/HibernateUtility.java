package main.java.com.projectBackEnd.Util;

import java.io.File;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtility { //TODO: Update to be like the

    public static String location = "";
    /**
     * Gets the session factory created in this case specifically for the Page class
     * @return The session factory.
     */
    public static SessionFactory getSessionFactory(Class entityclass) { //TODO Takes Class entityclass
        Configuration configuration = new Configuration();
        if (location.length() > 0) {
            configuration.addAnnotatedClass(entityclass).configure(location);
        } else {
            configuration.addAnnotatedClass(entityclass).configure(); //TODO These two lines need to be dynamic, controlling location of DB and class
        }
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        return configuration
                .buildSessionFactory(builder.build());
    }
    public static void setLocation(String locationIn) {
        location = locationIn;
    }

}