package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Medicine.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.MedicineManager;

import main.java.com.projectBackEnd.Entities.Medicine.MedicineManagerInterface;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class MedicineManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static MedicineManagerInterface medicineManager = null;

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        medicineManager = MedicineManager.getMedicineManager();
        //connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        //connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        medicineManager.deleteAll();
    }

    @Test
    public void testFillingAndGetting() {
        fillDatabase();
        assertEquals(medicineManager.getAllMedicines().size(), 10);
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        int medPK = (int) medicineManager.getAllMedicines().get(0).getPrimaryKey();
        Medicine med = medicineManager.getByPrimaryKey(medPK);
//        System.out.println(med.getPrimaryKey());
//        System.out.println(getAllMedicines().get(0).getPrimaryKey());
        assertTrue(med.equals(medicineManager.getAllMedicines().get(0)));
    }


    @Test
    public void testDeleteAll() {
        // Delete all from empty database
        medicineManager.deleteAll();
        assertEquals(medicineManager.getAllMedicines().size(), 0);
        // Delete all from filled database
        fillDatabase();
        medicineManager.deleteAll();
        assertEquals(medicineManager.getAllMedicines().size(), 0);
    }

    @Test
    public void testDelete() {
        fillDatabase();
        medicineManager.delete(medicineManager.getAllMedicines().get(1)); //Testing object deletion
        assertEquals(medicineManager.getAllMedicines().size(), getListOfMedicines().size()-1);
        medicineManager.delete(medicineManager.getAllMedicines().get(1));
        assertEquals(medicineManager.getAllMedicines().size(), getListOfMedicines().size()-2);
    }

    @Test
    public void testDeleteByPK() {
        fillDatabase();
        medicineManager.delete(medicineManager.getAllMedicines().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals(medicineManager.getAllMedicines().size(), getListOfMedicines().size()-1);
    }

    @Test
    public void testDeleteIllegalPK() {
        fillDatabase();
        try {
            medicineManager.delete(102);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(medicineManager.getAllMedicines().size(), getListOfMedicines().size()); // Check that nothing has been removed
        }
    }



    // Liquid, Tablet, Capsule, Injection, Topical, Suppositories, Drops, Inhalers

    private static ArrayList<Medicine> getListOfMedicines() {
        ArrayList<Medicine> listOfMedicines = new ArrayList<>();

        listOfMedicines.add(new Medicine("Med1", "Liquid"));
        listOfMedicines.add(new Medicine("Med2", "Tablet"));
        listOfMedicines.add(new Medicine("Med3", "Injection"));
        listOfMedicines.add(new Medicine("Med4", "Tablet"));
        listOfMedicines.add(new Medicine("Med4", "Liquid"));
        listOfMedicines.add(new Medicine("Med6", "Drops"));
        listOfMedicines.add(new Medicine("Med7", "Inhaler"));
        listOfMedicines.add(new Medicine("Med8", "Capsule"));
        listOfMedicines.add(new Medicine("Med9", "Liquid"));
        listOfMedicines.add(new Medicine("Med7", "Topical"));

        return listOfMedicines;
    }

    private void fillDatabase() {
        for (Medicine med : getListOfMedicines()) {
            medicineManager.addMedicine(med);
        }
    }

}