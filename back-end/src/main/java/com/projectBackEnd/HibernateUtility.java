package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.HashSet;

/**
 * http://www.jcombat.com/hibernate/introduction-to-hibernateutil-and-the-sessionfactory-interface
 */
public class HibernateUtility
{
    private static SessionFactory sessionFactory;
    private static String resourceName="hibernate.cfg.xml";
    private static HashSet<Class> annotations;

    public synchronized static SessionFactory buildSessionFactory()
    {
        if (sessionFactory != null) {
            if (sessionFactory.isOpen()) sessionFactory.close();
        }
        try {
            Configuration cfg = new Configuration();
            for(Class a : annotations){
                cfg.addAnnotatedClass(a);
            }
            sessionFactory = cfg.configure(resourceName).buildSessionFactory();
            return sessionFactory;

        } catch (Throwable ex) {
            System.err.println("SF creation failure." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void setResource(String newName){
        resourceName = newName;
    }

    public static void addAnnotation(Class c){
        if (annotations == null) {
            annotations = new HashSet<>();
        }
        if (annotations.contains(c)) return;
        annotations.add(c);
        buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory != null) return sessionFactory;
        else {
            return buildSessionFactory();
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    //TODO Allow dynamic controlling location of DB and class

}
