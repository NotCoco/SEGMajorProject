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
}
