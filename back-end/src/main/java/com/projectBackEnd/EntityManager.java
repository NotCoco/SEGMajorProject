package main.java.com.projectBackEnd;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

public class EntityManager { //TODO Try with statics to see which is cleaner


    public static <T> List<T> getAll(Class subclass) { //Hibernate get all, no HQL
        //https://stackoverflow.com/questions/43037814/how-to-get-all-data-in-the-table-with-hibernate/43067399
        List<T> results = null;
        try (SessionFactory sf = HibernateUtility.getSessionFactory(subclass)) {
            results = getAllCriteria(subclass, sf.openSession());
        }
        return results;
    }
    private static <T> List<T> getAllCriteria(Class subclass, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(subclass);
        criteria.from(subclass);
        return session.createQuery(criteria).getResultList();
    }
    public static void deleteAll(Class subclass) {
        SessionFactory sf = HibernateUtility.getSessionFactory(subclass);
        Session session = sf.openSession();
        try {
            deleteAllTransaction(subclass, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            sf.close();
        }
    }
    private static void deleteAllTransaction(Class subclass, Session session) {
        session.beginTransaction();
        for (Object tuple : getAll(subclass)) { //Deleting one by one is recommended to deal with cascading.
            session.delete(tuple);
        }
        session.getTransaction().commit();
    }

    /**
     * Insert a new page to be added to the database
     * @param newObject The page to be added to the database
     */
    //public <U> void insertTyple(U newObject) Basically the same :\ U extends T doesn't work.
    public static Object insertTuple(Object newObject) {
        //assert TableEntity.class.isAssignableFrom(newObject.getClass());
        SessionFactory sf = HibernateUtility.getSessionFactory(newObject.getClass());
        Session session = sf.openSession();
        try {
            insertTupleTransaction(newObject, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            sf.close();
        }
        return newObject;
    }

    private static void insertTupleTransaction(Object newObject, Session session) {
        session.beginTransaction();
        session.save(newObject);
        session.getTransaction().commit();
    }

    public static Object getByPrimaryKey(Class subclass, Serializable pk) {
        SessionFactory sf = HibernateUtility.getSessionFactory(subclass);
        Session session = sf.openSession();
        Object found = null;
        try {
            found = findByPrimaryKeyTransaction(subclass, pk, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback(); //VIOLATES
        } finally {
            session.close();
            sf.close();
        }
        return found;
    }
    private static Object findByPrimaryKeyTransaction(Class subclass, Serializable pk, Session session) {
        session.beginTransaction(); //TODO Demeter Violation with Implicit Transaction object
        Object found = session.get(subclass, pk);
        session.getTransaction().commit(); //Violation
        return found;
    }


    public static void delete(TableEntity object) {
        SessionFactory sf = HibernateUtility.getSessionFactory(object.getClass()); //Violates Demeter
        Session session = sf.openSession();
        try {
            deletePageTransaction(object, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            sf.close();
        }
    }

    /**
     * Old entity / Old entity's primary key comes back in with NEW content.
     * @param
     * @return
     */
    public static TableEntity update(TableEntity updatedCopy) { //TODO Session to become instance variable, for cleaner code
        SessionFactory sf = HibernateUtility.getSessionFactory(updatedCopy.getClass()); //Violates Demeter
        Session session = sf.openSession();
        TableEntity fromDatabase = null;
        try {
            fromDatabase = updateTransaction(updatedCopy, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            sf.close();
        }
        return fromDatabase;
    }

    private static TableEntity updateTransaction(TableEntity updatedCopy, Session session) throws HibernateException {
        session.beginTransaction(); //TODO Demeter Violation with Implicit Transaction object
        Page pageFromDatabase = (Page) session.load(updatedCopy.getClass(), updatedCopy.getPrimaryKey());
        pageFromDatabase.imitate(updatedCopy);
        session.getTransaction().commit(); //Violation
        return pageFromDatabase;
    }

    private static void deletePageTransaction(TableEntity object, Session session) throws HibernateException {
        session.beginTransaction();
        TableEntity entityToDelete = (TableEntity) getByPrimaryKey(object.getClass(), object.getPrimaryKey());
        session.delete(entityToDelete);
        session.getTransaction().commit();
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
