package main.java.com.projectBackEnd.Entities.Medicine;


import java.io.Serializable;
import java.util.List;

//TODO (Jeanne) : Commenting
public interface MedicineManagerInterface {

    public void deleteAll();
    public List<Medicine> getAllMedicines();
    public Medicine addMedicine(String name, String type);
    //public Medicine addMedicine(Medicine newMedicine);
    //public Medicine getByPrimaryKey(Integer pk);
    public void delete(Serializable pk);
    public void delete(Medicine med);
    public Medicine update(Medicine med);
    public Medicine getByPrimaryKey(Integer id);



}
