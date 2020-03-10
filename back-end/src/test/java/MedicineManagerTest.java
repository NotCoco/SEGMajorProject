package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Medicine.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.MedicineManager;

import org.junit.*;

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

    @Test
    public void testFillingAndGetting() {
        fillDatabase();
        assertEquals(getAllMedicines().size(), 10);
    }

    @Test
    public void testDeleteAll() {
        fillDatabase();
        deleteAll();
        assertEquals(getAllMedicines().size(), 0);
    }

    @Test
    public void testDelete() {
        fillDatabase();
        delete(getAllMedicines().get(1)); //Testing object deletion
        assertEquals(getAll().size(), getListOfMedicines().size()-1);
        delete(getAllMedicines().get(1));
        assertEquals(getAll().size(), getListOfMedicines().size()-2);
    }

    @Test
    public void testDeleteByPK() {
        fillDatabase();
        delete(getAllMedicines().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals(getAll().size(), getListOfMedicines().size()-1);
    }

//    @Test
//    public void testDelete(){
//        fillDatabase();
//        delete(getListOfMedicines().get(1));
//        assertEquals(getAllMedicines().size(), getListOfMedicines().size()-1);
//        delete(getListOfMedicines().get(2));
//        assertEquals(getAllMedicines().size(), getListOfMedicines().size()-2);
//    }

//    @Test
//    public void testDelete() {
//        fillDatabase();
//        delete(getListOfMedicines().get(5));
//        assertEquals(getAll().size(), getListOfMedicines().size()-1);
//    }


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