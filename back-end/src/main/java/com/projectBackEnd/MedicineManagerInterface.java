package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import java.util.List;

public interface MedicineManagerInterface {

    Medicine createMedicine(String name, String type);
    void createAndSaveMedicine(String name, String type);
    Medicine findByID(Integer id);
    void updateMedicine(Medicine medicine);
    void updateNameByID(Integer id, String name);
    void updateTypeByID(Integer id, String type);
    void deleteByID(Integer id);
    SessionFactory getSessionFactory();
    List<Medicine> getAll();
    void deleteAll();
    void insertTuple(Medicine medicine);

}
