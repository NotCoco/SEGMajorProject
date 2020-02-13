package main.java.com.projectBackEnd;

import org.hibernate.SessionFactory;
import java.util.List;

public interface MedicineManagerInterface {

    void addMedicine(String name, String type);
    void updateMedicine(Medicine medicine);
    Medicine findByID(int id);
    void deleteByID(int id);
    List<Medicine> getAll();


}
