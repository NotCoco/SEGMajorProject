package main.java.com.projectBackEnd;

import main.java.com.projectBackEnd.Util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

//decrepted
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;

import java.util.List;
	/**
	* TODO:
	* remove possiblity of adding 2 users with the same username
	* add reset password
	* change to javax.persistence.criteria.CriteriaQuery from org.hibernate.Criteria;
	* write hashing
	*/
public class UserManager implements UserManagerInterface{

	private void createUser(String username, String password){
		Session session = HibernateUtil.buildSessionFactory(User.class).openSession();
        	Transaction tx = null;
        	try {
            		tx = session.beginTransaction();
            		User u = new User(username, password);
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
	public void addUser(String username, String password){
		createUser(username, hash(password));
	}
	public boolean verifyUser(String username, String password){
		return checkUser(username,hash(password));
	}
	private boolean checkUser(String username, String password){
		Session session = HibernateUtil.buildSessionFactory(User.class).openSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		List l = criteria.list();
		return l.size() > 0;
	}

	public void changePassword(String username,String newPassword){

	}
	private String hash(String toHash){

		return toHash;
	}


}
