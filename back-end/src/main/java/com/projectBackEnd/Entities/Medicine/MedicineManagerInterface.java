package main.java.com.projectBackEnd.Entities.Medicine;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by MedicineManager for database queries.
 */

public interface MedicineManagerInterface {

    public void deleteAll();
    public void delete(Medicine med);
    public void delete(Integer pk);
    public Medicine update(Medicine med);
    public List<Medicine> getAllMedicines();
    public Medicine addMedicine(Medicine med);
    public Medicine getByPrimaryKey(Integer id);
    public Medicine addMedicine(String name, String type);

}
