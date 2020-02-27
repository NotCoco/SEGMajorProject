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
        HibernateUtility.addAnnotation(Medicine.class);
    }

    public Medicine createAndSaveMedicine(String name, String type) {
        Medicine newMedicine = new Medicine(name, type);
        super.insertTuple(newMedicine);
        return newMedicine;
    }

    public Medicine update(Medicine med) { //TODO Session to become instance variable, for cleaner code
        return (Medicine) super.update(med);
    }

    public void delete(Medicine med) {
        super.delete(med);
    }

    public Medicine getByPrimaryKey(Integer id) {
        return (Medicine) super.getByPrimaryKey(id);
    }

    public List<Medicine> getAllMedicines() {
        return super.getAll();
    }

//    public List<Medicine> getAllByType() {
//        // code
//    }
//
//    public List<Medicine> getAllByName() {
//        // code
//    }
//
//    public void deleteByID() {
//        // code
//    }
//
//    public Medicine updateNameByID(Medicine med){
//        // code
//    }
//
//    public Medicine updateTypeByID(Medicine med){
//        // code
//    }


    // deleteByID
    // updateTypeByID
    // updateNameByID
    // getAllMedicineByType
    // getAllMedicineByName

}
