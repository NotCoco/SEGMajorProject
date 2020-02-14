package main.java.com.projectBackEnd;

import java.io.File;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
 
public class HibernateUtil 
{
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private static SessionFactory buildSessionFactory() 
    {
        try {
            return new Configuration().configure().buildSessionFactory();
 
        } catch (Throwable ex) {
            System.err.println("SF creation failure." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory buildSessionFactory(Class annotatedClass) 
    {
        try {
            return new Configuration().addAnnotatedClass(annotatedClass).configure().buildSessionFactory();
 
        } catch (Throwable ex) {
            System.err.println("SF creation failure." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void shutdown() {
        getSessionFactory().close();
    }

    //TODO Allow dynamic controlling location of DB and class

}
