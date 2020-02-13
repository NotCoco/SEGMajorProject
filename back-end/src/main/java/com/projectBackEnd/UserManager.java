package main.java.com.projectBackEnd;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserManager implements UserManagerInterface{
	private static SessionFactory factory;
	public UserManager(){
		Configuration config = new Configuration();
        	config.configure();
        	config.addAnnotatedClass(User.class);
        	factory = config.buildSessionFactory();
	}
	public void addUser(String username, String password){
		Session session = factory.openSession();
        	Transaction tx = null;
        	try {
            		tx = session.beginTransaction();
            		User u = new User(username,password);
            		session.save(u);
            		tx.commit();
        	} catch(HibernateException ex) {
            		if(tx != null)
                		tx.rollback();
            		ex.printStackTrace();
        	} finally {
            		session.close();
        	}
	}
	public boolean verifyUser(String username, String password){
		return true;
	}


}
