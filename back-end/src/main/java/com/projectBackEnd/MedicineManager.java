package main.java.com.projectBackEnd;
import java.util.List;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;


// TODO (Jeanne) : commenting
public class MedicineManager {


    public static Medicine createAndSaveMedicine(String name, String type) {
        Medicine newMedicine = new Medicine(name, type);
        EntityManager.insertTuple(newMedicine);
        return newMedicine;
    }

    public static Medicine update(Medicine med) { //TODO Session to become instance variable, for cleaner code
        return (Medicine) EntityManager.update(med);
    }

    public static void delete(Medicine med) {
        EntityManager.delete(med);
    }

    public static Medicine findByID(Integer id) {
        return (Medicine) EntityManager.getByPrimaryKey(Page.class, id);
    }

    public static void deleteAll() {
        EntityManager.deleteAll(Medicine.class);
    }

    public static List<Medicine> getAll() {
        return EntityManager.getAll(Medicine.class);
    }

    public static void insertTuple(Medicine med) {
        EntityManager.insertTuple(med);
    }



//
//    public MedicineManager() {
//        Configuration config = new Configuration();
//        config.configure();
//        config.addAnnotatedClass(Medicine.class);
//        factory = config.buildSessionFactory();
//    }
//
//    public void addMedicine(String name, String type) {
//        Session session = factory.openSession();
//        Transaction transaction = null;
//        try {
//            transaction = session.beginTransaction();
//            Medicine med = new Medicine(name, type);
//            session.save(med);
//            transaction.commit();
//        } catch(HibernateException ex) {
//            if(transaction != null) transaction.rollback();
//            ex.printStackTrace();
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
//    public Medicine findByID(int id) {
//        Session session = factory.openSession();
//        Medicine medicine = session.load(Medicine.class, id);
//        session.close();
//        return medicine;
//    }
//
//    @Override
//    public void updateMedicine(Medicine medicine) {
//        Session session = factory.openSession();
//        Transaction transaction = null;
//        try {
//            transaction = session.beginTransaction();
//            Medicine medicineFromDatabase = session.load(Medicine.class, medicine.getId());
//            medicineFromDatabase.setName(medicine.getName());
//            medicineFromDatabase.setType(medicine.getType());
//            transaction.commit();
//        } catch(HibernateException ex) {
//            if(transaction != null) transaction.rollback();
//            ex.printStackTrace();
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
//    public void deleteByID(int id) {
//        Session session = factory.openSession();
//        Transaction transaction = null;
//        try {
//            transaction = getDeleteTransaction(id, session);
//        } catch(HibernateException ex) {
//            if (transaction != null) transaction.rollback();
//            ex.printStackTrace();
//        } finally {
//            session.close();
//        }
//    }
//
//    private Transaction getDeleteTransaction(int id, Session session) throws HibernateException {
//        Transaction transaction = session.beginTransaction();
//        Medicine medicine = findByID(id);
//        session.delete(medicine);
//        transaction.commit();
//        return transaction;
//    }
//
//
//    @Override
//    public List<Medicine> getAll() {
//        Session session = factory.openSession();
//        String hqlQuery = "FROM " + new SQLSafeString(Page.TABLENAME);
//        @SuppressWarnings("Unchecked")
//        List<Medicine> medicines = session.createQuery(hqlQuery).list();
//        session.close();
//        return medicines;
//    }
    
//  Delete all method?

}
