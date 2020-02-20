package main.java.com.projectBackEnd;
import java.util.List;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;


// TODO (Jeanne) : commenting
// TODO (Jeanne) : implement getByType


public class MedicineManager extends EntityManager implements MedicineManagerInterface {

    public MedicineManager() {
        super();
        setSubclass(Medicine.class);
        HibernateUtility.addAnnotation(Page.class);
    }

    public Medicine createAndSaveMedicine(String name, String type) {
        Medicine newMedicine = new Medicine(name, type);
        EntityManager.insertTuple(newMedicine);
        return newMedicine;
    }

    public Medicine update(Medicine med) { //TODO Session to become instance variable, for cleaner code
        return (Medicine) EntityManager.update(med);
    }

    public void delete(Medicine med) {
        EntityManager.delete(med);
    }

    public Medicine findByID(Integer id) {
        return (Medicine) EntityManager.getByPrimaryKey(Medicine.class, id);
    }

    public void deleteAll() {
        EntityManager.deleteAll(Medicine.class);
    }

    public List<Medicine> getAllMedicines() {
        return EntityManager.getAll(Medicine.class);
    }

    public List<Medicine> getByType() {
        // code
    }

}
