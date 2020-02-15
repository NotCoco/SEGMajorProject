package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

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
}
