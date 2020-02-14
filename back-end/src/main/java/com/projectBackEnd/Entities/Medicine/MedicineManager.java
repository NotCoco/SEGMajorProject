package main.java.com.projectBackEnd;
import java.util.List;

import main.java.com.projectBackEnd.Util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class MedicineManager implements MedicineManagerInterface {

    @Override
    public Medicine createMedicine(String name, String type) {
        return new Medicine(name,type);
    }

    @Override
    public void createAndSaveMedicine(String name, String type) {
        insertTuple(createMedicine(name, type));
    }

    @Override
    public Medicine findByID(Integer id) {
        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        Medicine medicine = session.load(Medicine.class, id);
        session.close();
        return medicine;
    }

    @Override
    public void updateMedicine(Medicine medicine) {
        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        session.beginTransaction();

        Medicine medicineFromDatabase = session.load(Medicine.class, medicine.getId());
        medicineFromDatabase.setName(medicine.getName());
        medicineFromDatabase.setType(medicine.getType());

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void updateNameByID(Integer id, String newName) {
        updateByID(id, newName, "type");
    }

    @Override
    public void updateTypeByID(Integer id, String newType) {
        updateByID(id, newType, "type");
    }


    public void updateByID(Integer id, String value, String attribute) {

        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        session.beginTransaction();

        Medicine medicineFromDatabase = session.load(Medicine.class, id);

        if (attribute.equals("name")) medicineFromDatabase.setName(new SQLSafeString(value).toString());
        else if (attribute.equals("type")) medicineFromDatabase.setType(new SQLSafeString(value).toString());
        else; //error

        session.getTransaction().commit();
        session.close();
    }


    @Override
    public void deleteByID(Integer id) {
        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        session.beginTransaction();
        Medicine medicine = findByID(id);
        session.delete(medicine);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Medicine> getAll() {
        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        String hqlQuery = "FROM " + new SQLSafeString(Page.TABLENAME);
        @SuppressWarnings("Unchecked")
        List<Medicine> medicines = session.createQuery(hqlQuery).list();
        session.close();
        return medicines;
    }

    @Override
    public void deleteAll() {
        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM " + new SQLSafeString(Page.TABLENAME) + " ");
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void insertTuple(Medicine medicine) {
        Session session = HibernateUtil.buildSessionFactory(Medicine.class).openSession();
        session.beginTransaction();
        session.save(medicine);
        session.getTransaction().commit();
        session.close();
    }
}