package main.java.com.projectBackEnd;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
//https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
public class App {
	
	public static void main(String[] args) {



	}

	public static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration().
				addAnnotatedClass(Employee.class)
				.configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(builder.build());
		return sessionFactory;
	}


	public static List<Employee> read() {
		Session session = getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		List<Employee> employees = session.createQuery("FROM Employee").list();
		session.close();
		System.out.println("Found " + employees.size() + " Employees");
		return employees;
	}

	public static void update(Employee e) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Employee em = (Employee) session.load(Employee.class, e.getId());
		em.setName(e.getName());
		em.setAge(e.getAge());
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully updated " + e.toString());

	}
	public static Integer create(Employee e) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		session.save(e);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + e.toString());
		return e.getId();

	}
	public static void delete(Integer id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Employee e = findByID(id);
		session.delete(e);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully deleted " + e.toString());

	}

	public static Employee findByID(Integer id) {
		Session session = getSessionFactory().openSession();
		Employee e = (Employee) session.load(Employee.class, id);
		session.close();
		return e;
	}

	public static void deleteAll() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("DELETE FROM Employee ");
		query.executeUpdate();
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully deleted all employees.");

	}
}
