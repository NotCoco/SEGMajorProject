package main.java.com.projectBackEnd.Entities.Medicine;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by MedicineManager for database queries.
 */

public interface MedicineManagerInterface {

    public Medicine addMedicine(String name, String type);
    public Medicine addMedicine(Medicine med);
    public Medicine update(Medicine med);
    public Medicine getByPrimaryKey(Serializable id);
    public List<Medicine> getAllMedicines();
    public List<Medicine> getAllMedicinesByType(String type);
    public List<Medicine> getAllMedicinesByName(String name);
    public void deleteAll();
    public void delete(Medicine med);
    public void delete(Serializable pk);

}
