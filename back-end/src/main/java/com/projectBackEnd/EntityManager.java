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
        if (!extendsTableEntity(subclass)) return results;
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
        if (!extendsTableEntity(subclass)) return;
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

    //public <U> void insertTyple(U newObject) Basically the same :\ U extends T doesn't work.
    public static TableEntity insertTuple(TableEntity newObject) {
        //if (!extendsTableEntity(newObject.getClass())) return newObject;
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
    //TODO (Wasif, delete all mass commented out code)
    private static void insertTupleTransaction(TableEntity newObject, Session session) {
        session.beginTransaction();
        session.save(newObject);
        session.getTransaction().commit();
    }

    public static TableEntity getByPrimaryKey(Class subclass, Serializable pk) {
        if (!extendsTableEntity(subclass)) return null;
        SessionFactory sf = HibernateUtility.getSessionFactory(subclass);
        Session session = sf.openSession();
        TableEntity found = null;
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
    private static TableEntity findByPrimaryKeyTransaction(Class subclass, Serializable pk, Session session) {
        session.beginTransaction(); //TODO Demeter Violation with Implicit Transaction object
        TableEntity found = (TableEntity) session.get(subclass, pk);
        session.getTransaction().commit(); //Violation
        return found;
    }


    public static void delete(TableEntity object) {
        SessionFactory sf = HibernateUtility.getSessionFactory(object.getClass()); //Violates Demeter
        Session session = sf.openSession();
        try {
            deleteTransaction(object, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            sf.close();
        }
    }
    private static void deleteTransaction(TableEntity object, Session session) throws HibernateException {
        session.beginTransaction();
        TableEntity entityToDelete = getByPrimaryKey(object.getClass(), object.getPrimaryKey());
        session.delete(entityToDelete);
        session.getTransaction().commit();
    }

    //TODO Might need to return back down if frontend send strings etc. I presume they will json and send the (page) back
    //Methods are commented out already in the PageManager if they send a String primary key.
    public static TableEntity update(TableEntity updatedCopy) {
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
    public static boolean extendsTableEntity(Class c) { //TODO: Thoughts on this?
        return TableEntity.class.isAssignableFrom(c);
    }

    private static TableEntity updateTransaction(TableEntity updatedCopy, Session session) throws HibernateException {
        session.beginTransaction(); //TODO Demeter Violation with Implicit Transaction object
        TableEntity fromDatabase = (TableEntity) session.load(updatedCopy.getClass(), updatedCopy.getPrimaryKey());
        //TODO: If not found?
        if (fromDatabase != null) fromDatabase.imitate(updatedCopy);
        else insertTuple(updatedCopy);
        session.getTransaction().commit(); //Violation
        return fromDatabase;
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
