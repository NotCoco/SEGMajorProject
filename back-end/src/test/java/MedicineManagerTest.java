package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Medicine.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.MedicineManager;

import main.java.com.projectBackEnd.Entities.Medicine.MedicineManagerInterface;
import org.junit.*;

import javax.persistence.PersistenceException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
    public void testCreateMedicine() {
        Medicine med = new Medicine("Medicine for BA", "Injection");
        assertEquals(med.getName(), "Medicine for BA");
        assertEquals(med.getType(), "Injection");
    }

    @Test
    public void testCreateAndSaveMedicine() {
        medicineManager.addMedicine("Medicine for BA ", "Topical");
        assertEquals(medicineManager.getAllMedicines().size(), 1);
    }

    @Test
    public void testCreateIllegalMedicine(){
        medicineManager.addMedicine(null,null);
        assertEquals(medicineManager.getAllMedicines().size(), 0);
    }

    @Test
    public void testFillingAndGetting() {
        fillDatabase();
        assertEquals(medicineManager.getAllMedicines().size(), 10);
    }

    @Test
    public void testUpdateMedicine() {

    }

    @Test(expected = PersistenceException.class)
    public void testUpdateWithIllegalValues() {
        fillDatabase();
        Medicine med = new Medicine((int)medicineManager.getAllMedicines().get(0).getPrimaryKey(), null, null);
        medicineManager.update(med);
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
    public void testGetIllegalPrimaryKey() {
        assertNull(medicineManager.getByPrimaryKey(-1));
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
    public void testWithDeleteIllegalPK() {
        int medicines = medicineManager.getAllMedicines().size();
        try {
            medicineManager.delete(-1);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(medicineManager.getAllMedicines().size(), medicines);
            // Check that nothing has been removed
        }
    }

    @Test
    public void testDeleteNotInDBObject() {
        Medicine med = new Medicine("Not in db", "NA");
        int medicines = medicineManager.getAllMedicines().size();
        try {
            medicineManager.delete(med);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(medicineManager.getAllMedicines().size(), medicines);
            // Check that nothing has been removed
        }
    }



    private static ArrayList<Medicine> getListOfMedicines() {
        ArrayList<Medicine> listOfMedicines = new ArrayList<>();

        // TYPES : Liquid, Tablet, Capsule, Injection, Topical, Suppositories, Drops, Inhalers
        listOfMedicines.add(new Medicine("Med1", "Liquid"));
        listOfMedicines.add(new Medicine("Med2", "Tablet"));
        listOfMedicines.add(new Medicine("Med3", "Injection"));
        listOfMedicines.add(new Medicine("Med4", "Tablet"));
        listOfMedicines.add(new Medicine("Med4", "Liquid"));
        listOfMedicines.add(new Medicine("Med6", "Drops"));
        listOfMedicines.add(new Medicine("Med7", "Inhaler"));
        listOfMedicines.add(new Medicine("Med8", "Capsule"));
        listOfMedicines.add(new Medicine("Med9", "Liquid"));
        listOfMedicines.add(new Medicine(-1, "Med7", "Topical"));

        return listOfMedicines;
    }

    private void fillDatabase() {
        for (Medicine med : getListOfMedicines()) {
            medicineManager.addMedicine(med);
        }
    }

}