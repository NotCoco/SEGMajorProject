package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.HashSet;

/**
 * A Hibernate Utility to monitor session factories have correct table classes and ensure no connectinos leak. This class
 * is the connection point to the configuration xml for a database.
 * http://www.jcombat.com/hibernate/introduction-to-hibernateutil-and-the-sessionfactory-interface
 */
public class HibernateUtility {

    private static SessionFactory sessionFactory;
    private static String resourceName="hibernate.cfg.xml";
    private static HashSet<Class> annotatedClasses;

    /**
     * Builds a session factory using the annotated class list from which sessions can be
     * created. Also closes any previous ones if necessary so only one is in use.
     * @return The session factory made, default open.
     */
    private synchronized static SessionFactory getOpenSessionFactory() {
        if (sessionFactory != null) {
            if (sessionFactory.isOpen()) sessionFactory.close();
        }
        try {
            return createFactory(new Configuration());
        } catch (Throwable ex) {
            System.err.println("SF creation failure." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Creates a factory using a configuration and the list of AnnotatedClasses
     * @param cfg The configuration to be used
     * @return A newly built, open session factory.
     */
    private synchronized static SessionFactory createFactory(Configuration cfg) {
        for(Class entityClass : annotatedClasses) {
            cfg.addAnnotatedClass(entityClass);
        }
        sessionFactory = cfg.configure(resourceName).buildSessionFactory();
        return sessionFactory.isOpen() ? sessionFactory : createFactory(cfg);
    }
    /**
     * Set the location of the hibernate config file which contains database information
     * @param location The location and name of the config file
     */
    public static void setResource(String location){
        resourceName = location;
    }

    /**
     * Adds another entity class to the factory and rebuilds the factory for a new table.
     * @param tableEntity The class that will be added to the factory for table access
     */
    public static void addAnnotation(Class tableEntity) {
        if (annotatedClasses == null) annotatedClasses = new HashSet<>();
        if (annotatedClasses.contains(tableEntity)) return;
        annotatedClasses.add(tableEntity);
        getOpenSessionFactory();
    }

    /**
     * Returns an open session factory that can be used for sessions
     * @return The open session factory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed() && sessionFactory.isOpen()) return sessionFactory;
        else return getOpenSessionFactory();
    }

    /**
     * Closes the session factory ending SQL connection to it
     */
    public static void shutdown() {
        getSessionFactory().close();
    }

}
