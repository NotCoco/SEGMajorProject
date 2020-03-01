package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Medicine.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.MedicineManager;

import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class MedicineManagerTest extends MedicineManager {

    public static ConnectionLeakUtil connectionLeakUtil = null;

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        deleteAll();
    }

//    @Test
//    public void testGetByPrimaryKey() {
//        fillDatabase();
//        Medicine medicine3 = (Medicine) (getByPrimaryKey("3"));
//        assertTrue(medicine3.equals(getListOfMedicines().get(3)));
//    }
//
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
            insertTuple(med);
        }
    }

}