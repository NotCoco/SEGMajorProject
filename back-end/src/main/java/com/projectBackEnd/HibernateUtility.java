package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class HibernateUtility
{
    private static SessionFactory sessionFactory;
    private static String resourceName="hibernate.cfg.xml";
    private static ArrayList<Class> annotations;

    public static SessionFactory buildSessionFactory()
    {
        try {
            Configuration cfg = new Configuration();
            for(Class a : annotations){
                cfg.addAnnotatedClass(a);
            }
            sessionFactory = cfg.configure("/main/resources/"+resourceName).buildSessionFactory();
            return sessionFactory;

        } catch (Throwable ex) {
            System.err.println("SF creation failure." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void setResource(String newName){
        resourceName = newName;
    }

    public static void replaceAnnotationList(ArrayList<Class> newAnnotations){
        annotations = newAnnotations;
        buildSessionFactory();
    }

    public static void addAnnotation(Class c){
        annotations.add(c);
        buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    //TODO Allow dynamic controlling location of DB and class

}