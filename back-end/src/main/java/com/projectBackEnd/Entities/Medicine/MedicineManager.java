package main.java.com.projectBackEnd.Entities.Medicine;
import java.util.List;
import java.util.stream.Collectors;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

// TODO (Jeanne) : commenting

public class MedicineManager extends EntityManager implements MedicineManagerInterface {

    private static MedicineManagerInterface medicineManager;

    public MedicineManager() {
        super();
        setSubclass(Medicine.class);
        HibernateUtility.addAnnotation(Medicine.class);
    }
    public static MedicineManagerInterface getMedicineManager() {
        return (medicineManager != null) ? medicineManager : new MedicineManager();
    }

    //@Transactional
    public Medicine addMedicine(String name, String type) {
        Medicine newMedicine = new Medicine(name, type);
        super.insertTuple(newMedicine);
        return newMedicine;
    }

    // Transactional annotation is inherited so no need to tag these methods
    public Medicine update(Medicine med) { //TODO Session to become instance variable, for cleaner code
        return (Medicine) super.update(med);
    }

    @Override
    public void delete(Medicine med) {
        super.delete(med);
    }

    @Override
    public Medicine getByPrimaryKey(int id) {
        return (Medicine) super.getByPrimaryKey(id);
    }

    public List<Medicine> getAllMedicines() {
        return (List<Medicine>) super.getAll();
    }

    //@Transactional(readOnly = true)
    public List<Medicine> getAllMedicinesByType(String type) {
        return getAllMedicines().stream().filter(m -> m.getType().equals(type)).collect(Collectors.toList());
    }

    //@Transactional(readOnly = true)
    public List<Medicine> getAllMedicinesByName(String name) {
        return getAllMedicines().stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
    }

}
