package main.java.com.projectBackEnd;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class EntityManager <T extends TableEntity> { //TODO Try with statics to see which is cleaner
    private Class<T> subclass;
    private static String location = "";


    public static void setLocation(String location) {
        EntityManager.location = location;
    }

    public void setSubclass(Class<T> subclass) {
        this.subclass = subclass;
    }

    /**
     * Gets the session factory created in this case specifically for the Page class
     * @return The session factory.
     */
    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        if (location.length() < 0) {
                    configuration
                    .addAnnotatedClass(subclass)
                    .configure();
        } else {
                    configuration
                    .addAnnotatedClass(subclass)
                    .configure(location);
        }
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        return configuration
                .buildSessionFactory(builder.build());

    }

    public List<T> getAll() { //Hibernate get all, no HQL
        //https://stackoverflow.com/questions/43037814/how-to-get-all-data-in-the-table-with-hibernate/43067399
        //Use <T> as per link
        Session session = getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(subclass);
        criteria.from(subclass);
        return session.createQuery(criteria).getResultList();
    }
    public void deleteAll() {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        for (Object tuple : getAll()) { //Deleting one by one is recommended to deal with cascading.
        session.delete(tuple); }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Insert a new page to be added to the database
     * @param newObject The page to be added to the database
     */
    //public <U> void insertTyple(U newObject) Basically the same :\ U extends T doesn't work.
    public void insertTuple(Object newObject) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(newObject);
        session.getTransaction().commit();
        session.close();
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
