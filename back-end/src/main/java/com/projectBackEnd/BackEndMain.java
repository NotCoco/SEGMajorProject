package main.java.com.projectBackEnd;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;



public class BackEndMain {

	
	public static void main(String []args){
		UserManager um = new UserManager();
		um.addUser("test2","test2");
		System.out.println("Hello World");
	}




}
