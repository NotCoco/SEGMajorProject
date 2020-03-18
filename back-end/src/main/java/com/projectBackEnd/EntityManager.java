package main.java.com.projectBackEnd;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * EntityManager is the super class of managers, responsible for database queries.
 * It requires a subclass parameter using generic type to define which database table the manager is interacting with.
 * It is responsible for opening and closing the hibernate session.
 * @param <T>
 */
public class EntityManager <T extends TableEntity> {

    private Class<T> subclass;


    /** Set input class as subclass
     * @param subclass
     */
    public void setSubclass(Class<T> subclass) {
        this.subclass = subclass;
    }

    //TODO : The below two methods need to be combined into one single method as chaining @Transactional
    // calls is not allowed


    /**
     * Open the session to get all the objects of type T from the database
     * Sources : https://stackoverflow.com/questions/43037814/how-to-get-all-data-in-the-table-with-hibernate/43067399
     * @return List of all objects of type T found in database
     */
    public List<T> getAll() {

        List<T> results = null;
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            results = getAll(session);
        }
        //HibernateUtility.getSessionFactory().close();
        return results;
        //Doesn't close its own factory, will leak until factory is properly implemented.

    }


    /**
     * Queries the database to find all objects of subclass T that it stores
     * @param session
     * @return List of all objects of type T stored in db
     * @throws HibernateException
     */
    private List<T> getAll(Session session) throws HibernateException  {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T>  criteria = builder.createQuery(subclass);
        criteria.from(subclass);
        return session.createQuery(criteria).getResultList();

    }


    /**
     * Open Session to delete all objects of subclass from database
     */
    public void deleteAll() {

        SessionFactory sf = HibernateUtility.getSessionFactory();
        Session session = sf.openSession();
        try {
            deleteAll(session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
        }

    }


    /**
     * Get all objects from subclass currently stored in database
     * @param session
     * @throws HibernateException
     */
    private void deleteAll(Session session) throws  HibernateException {

        session.beginTransaction();
        for (Object tuple : getAll()) { //Deleting one by one is recommended to deal with cascading.
            session.delete(tuple);
        }
        session.getTransaction().commit();

    }


    /**
     * Open the session and insert a new object into the subclass entity table
     * @param newObject
     * @return object added to database
     */
    public T insertTuple(T newObject) {

        SessionFactory sf = HibernateUtility.getSessionFactory();
        Session session = sf.openSession();
        try {
            insertTuple(newObject, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return newObject;

    }


    /**
     * Save a new object into the database
     * @param newObject
     * @param session
     * @throws HibernateException
     */
    private void insertTuple(T newObject, Session session) throws HibernateException {

        session.beginTransaction();
        session.save(newObject);
        session.getTransaction().commit();

    }


    /**
     * Open the session and look for the object corresponding to pk in database
     * @param pk
     * @return Object found in database (or null)
     */
    public T getByPrimaryKey(Serializable pk) {

        SessionFactory sf = HibernateUtility.getSessionFactory();
        Session session = sf.openSession();
        T found = null;
        try {
            found = getByPrimaryKey(pk, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return found;
    }


    /**
     * Query database and find object corresponding to pk
     * @param pk
     * @param session
     * @return object found at pk
     * @throws HibernateException
     */
    private T getByPrimaryKey(Serializable pk, Session session) throws HibernateException {

        session.beginTransaction();
        T found = session.get(subclass, pk);
        session.getTransaction().commit();
        return found;

    }


    /**
     * Open Session and delete input object from subclass entity table in database
     * @param object
     */
    public void delete(T object) {

        SessionFactory sf = HibernateUtility.getSessionFactory();
        Session session = sf.openSession();
        try {
            delete(object, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
        }

    }


    /**
     * Find object in database and delete it
     * @param object
     * @param session
     * @throws HibernateException
     */
    private void delete(T object, Session session) throws HibernateException {

        session.beginTransaction();
        T entityToDelete = getByPrimaryKey(object.getPrimaryKey());
        session.delete(entityToDelete);
        session.getTransaction().commit();

    }


    /**
     * Open Session and find object associated to input pk
     * @param pk
     */
    public void delete(Serializable pk) {

        SessionFactory sf = HibernateUtility.getSessionFactory();
        Session session = sf.openSession();
        try {
            delete(pk, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
        }

    }


    /**
     * Find object by its given primary key and remove it from database
     * @param pk
     * @param session
     * @throws HibernateException
     */
    private void delete(Serializable pk, Session session) throws HibernateException {

        session.beginTransaction();
        T entityToDelete = getByPrimaryKey(pk);
        session.delete(entityToDelete);
        session.getTransaction().commit();

    }

    //TODO Might need to return back down if frontend send strings etc. I presume they will json and send the (page) back
    //Methods are commented out already in the PageManager if they send a String primary key.


    /**
     * Open Session and update input object from subclass entity table in database
     * @param updatedCopy
     * @return
     */
    public T update(T updatedCopy) {

        SessionFactory sf = HibernateUtility.getSessionFactory(); //Gets sf
        Session session = sf.openSession();
        T fromDatabase = null;
        try {
            fromDatabase = update(updatedCopy, session);
        } catch(HibernateException ex) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return fromDatabase;

    }

    /**
     * Find object to update in database and replace it with updatedCopy or insert it if not found
     * @param updatedCopy
     * @param session
     * @return Updated object
     * @throws HibernateException
     */
    private T update(T updatedCopy, Session session) throws HibernateException {

        session.beginTransaction();
        T fromDatabase = (T) session.load(updatedCopy.getClass(), updatedCopy.getPrimaryKey());
        if (fromDatabase != null) fromDatabase.copy(updatedCopy);
        else insertTuple(updatedCopy);
        session.getTransaction().commit();
        return fromDatabase;

    }


}