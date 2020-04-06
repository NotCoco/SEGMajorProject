package main.java.com.projectBackEnd.Entities.Medicine.Hibernate;

import java.io.Serializable;
import java.util.List;


/**
 *  Methods used by MedicineManager for database queries.
 */
public interface MedicineManagerInterface {

    public Medicine addMedicine(Medicine med);

    public Medicine update(Medicine med);

    public Medicine getByPrimaryKey(Serializable id);

    public List<Medicine> getAllMedicines();

    public void delete(Serializable pk);

    public void deleteAll();

}
