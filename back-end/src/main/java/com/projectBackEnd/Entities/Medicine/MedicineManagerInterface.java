package main.java.com.projectBackEnd.Entities.Medicine;


import java.io.Serializable;
import java.util.List;

//TODO (Jeanne) : Commenting
public interface MedicineManagerInterface {

    public void deleteAll();
    public List<Medicine> getAllMedicines();
    public Medicine addMedicine(String name, String type);
    public void delete(Serializable pk);
    public void delete(Medicine med);
    public Medicine update(Medicine med);
    public Medicine getByPrimaryKey(int id);


}
