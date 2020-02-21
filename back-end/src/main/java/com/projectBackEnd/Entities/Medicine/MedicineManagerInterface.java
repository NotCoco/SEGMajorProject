package main.java.com.projectBackEnd;


import main.java.com.projectBackEnd.TableEntity;

import java.io.Serializable;
import java.util.List;

//TODO (Jeanne) : Commenting
public interface MedicineManagerInterface {

    public void deleteAll();
    public List<Medicine> getAllMedicines();
    public Medicine createAndSaveMedicine(String name, String type);
    //public Medicine addMedicine(Medicine newMedicine);
    //public Medicine getByPrimaryKey(Integer pk);
    public void delete(Medicine med);
    public Medicine update(Medicine med);
    public Medicine getByPrimaryKey(Integer id);



}
