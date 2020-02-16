package main.java.com.projectBackEnd;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class EntityManager { //TODO Try with statics to see which is cleaner


    public static <T> List<T> getAll(Class subclass) { //Hibernate get all, no HQL
        //https://stackoverflow.com/questions/43037814/how-to-get-all-data-in-the-table-with-hibernate/43067399
        //Use <T> as per link
        Session session = HibernateUtility.getSessionFactory(subclass).openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(subclass);
        criteria.from(subclass);        
        return session.createQuery(criteria).getResultList();
    }
    public static void deleteAll(Class subclass) {
        Session session = HibernateUtility.getSessionFactory(subclass).openSession();
        session.beginTransaction();
        for (Object tuple : getAll(subclass)) { //Deleting one by one is recommended to deal with cascading.
        session.delete(tuple); }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Insert a new page to be added to the database
     * @param newObject The page to be added to the database
     */
    //public <U> void insertTyple(U newObject) Basically the same :\ U extends T doesn't work.
    public static Object insertTuple(Object newObject) {
        //assert TableEntity.class.isAssignableFrom(newObject.getClass());
        Session session = HibernateUtility.getSessionFactory(newObject.getClass()).openSession();
        session.beginTransaction();
        session.save(newObject);
        session.getTransaction().commit();
        session.close();
        return newObject;
    }
}
/*public static void removeAllInstances(final Class<?> clazz) {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    final List<?> instances = session.createCriteria(clazz).list();
    for (Object obj : instances) {
        session.delete(obj);
    }
    session.getTransaction().commit();
}*/ //https://stackoverflow.com/questions/25097385/query-to-delete-all-rows-in-a-table-hibernate/25097482
