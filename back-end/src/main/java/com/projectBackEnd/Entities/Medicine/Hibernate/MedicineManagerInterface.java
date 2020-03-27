package main.java.com.projectBackEnd.Entities.Medicine.Hibernate;

import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.Medicine;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by MedicineManager for database queries.
 */

public interface MedicineManagerInterface {
    public List<Medicine> getAllMedicines();
    public void deleteAll();
    public Medicine addMedicine(Medicine med);
    public Medicine getByPrimaryKey(Serializable id);
    public void delete(Serializable pk);
    public Medicine update(Medicine med);
}
