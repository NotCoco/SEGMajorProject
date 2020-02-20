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

public class EntityManager <T extends TableEntity> { //TODO Try with statics to see which is cleaner
    private Class<T> subclass;
    public void setSubclass(Class<T> subclass) {
        this.subclass = subclass;
    }

    public List<T> getAll() {
        //https://stackoverflow.com/questions/43037814/how-to-get-all-data-in-the-table-with-hibernate/43067399
        List<T> results = null;
        try (Session session = HibernateUtility.getSessionFactory(subclass).openSession()) {
            results = getAllSession(session);
        }
        return results;
        //Doesn't close its own factory, will leak until factory is properly implemented.
    }
    private List<T> getAllSession(Session session) throws HibernateException  {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T>  criteria = builder.createQuery(subclass);
        criteria.from(subclass);
        return session.createQuery(criteria).getResultList();
    }
    public void deleteAll() {
        SessionFactory sf = HibernateUtility.getSessionFactory(subclass);
        Session session = sf.openSession();
        try {
            deleteAllTransaction(session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            //sf.close(); //No longer closes the factory
        }
    }
    private void deleteAllTransaction(Session session) throws  HibernateException {
        session.beginTransaction();
        for (Object tuple : getAll()) { //Deleting one by one is recommended to deal with cascading.
            session.delete(tuple);
        }
        session.getTransaction().commit();
    }

    public T insertTuple(T newObject) {
        //if (!extendsTableEntity(newObject.getClass())) return newObject;
        SessionFactory sf = HibernateUtility.getSessionFactory(newObject.getClass()); //
        Session session = sf.openSession();
        try {
            insertTupleTransaction(newObject, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            //sf.close(); No longer closes its own factory
        }
        return newObject;
    }
    //TODO (Wasif, delete all mass commented out code)
    private void insertTupleTransaction(T newObject, Session session) throws HibernateException {
        session.beginTransaction();
        session.save(newObject);
        session.getTransaction().commit();
    }

    public T getByPrimaryKey(Serializable pk) {
        SessionFactory sf = HibernateUtility.getSessionFactory(subclass);
        Session session = sf.openSession();
        T found = null;
        try {
            found = findByPrimaryKeyTransaction(pk, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback(); //VIOLATES
        } finally {
            session.close();
            sf.close();
        }
        return found;
    }
    private T findByPrimaryKeyTransaction(Serializable pk, Session session) throws HibernateException {
        session.beginTransaction(); //TODO Demeter Violation with Implicit Transaction object
        T found = (T) session.get(subclass, pk);
        session.getTransaction().commit(); //Violation
        return found;
    }

    public void delete(T object) {
        SessionFactory sf = HibernateUtility.getSessionFactory(object.getClass());
        Session session = sf.openSession();
        try {
            deleteTransaction(object, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            //sf.close();
        }
    }
    private void deleteTransaction(T object, Session session) throws HibernateException {
        session.beginTransaction();
        T entityToDelete = getByPrimaryKey(object.getPrimaryKey());
        session.delete(entityToDelete);
        session.getTransaction().commit();
    }

    //TODO Might need to return back down if frontend send strings etc. I presume they will json and send the (page) back
    //Methods are commented out already in the PageManager if they send a String primary key.
    public T update(T updatedCopy) {
        SessionFactory sf = HibernateUtility.getSessionFactory(updatedCopy.getClass()); //Gets sf
        Session session = sf.openSession();
        T fromDatabase = null;
        try {
            fromDatabase = updateTransaction(updatedCopy, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
            //sf.close();
        }
        return fromDatabase;
    }

    private T updateTransaction(T updatedCopy, Session session) throws HibernateException {
        session.beginTransaction(); //TODO Demeter Violation with Implicit Transaction object
        T fromDatabase = (T) session.load(updatedCopy.getClass(), updatedCopy.getPrimaryKey());
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
