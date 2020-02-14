package main.java.com.projectBackEnd;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;



public class BackEndMain {

	
	public static void main(String []args){

		System.out.println("Hello World");
		UserManager pm = new UserManager();
		pm.addUser("123","321");
	}




}
