package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Medicine.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.MedicineManager;

import main.java.com.projectBackEnd.Entities.Medicine.MedicineManagerInterface;

import javax.persistence.PersistenceException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;


public class MedicineManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static MedicineManagerInterface medicineManager = null;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        medicineManager = MedicineManager.getMedicineManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterAll
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @BeforeEach
    public void setUp() {
        medicineManager.deleteAll();
    }

//======================================================================================================================
    //Testing Medicine Creation Constructors

    /**
     * Test the constructor constraints for naming with valid names
     */
    @Test
    public void testCreateValidMedicine() {
        Medicine validMedicine = new Medicine("Medicine for BA", "Injection");
        assertEquals("Medicine for BA", validMedicine.getName());
        assertEquals("Injection", validMedicine.getType());
    }

    /**
     * Test the constructor constraints for empty names
     */
    @Test
    public void testCreateEmptyNameMedicine() {
        Medicine emptyNameMedicine = new Medicine("", "Topical");
        assertEquals("Unnamed", emptyNameMedicine.getName());
    }

    /**
     * Test the constructor constraints for empty types
     */
    public void testCreateEmptyTypeMedicine() {
        Medicine emptyTypeMedicine = new Medicine("Medicine for BA", "");
        assertEquals("Undefined", emptyTypeMedicine.getType());
    }

    /**
     * Test the constructor constraints for null types
     */
    public void testCreateNullTypeMedicine() {
        Medicine nullTypeMedicine = new Medicine("Medicine for BA", null);
        assertEquals("Undefined", nullTypeMedicine.getType());
    }

    /**
     * Test the constructor constraints for null names
     */
    @Test
    public void testCreateNullNameMedicine() {
        Medicine nullNameMedicine = new Medicine(null, "Topical");
        assertEquals("Unnamed", nullNameMedicine.getName());
    }
    //Testing MedicineManagerInterface: addMedicine
    @Test
    public void testCreateAndSaveMedicine() {
        medicineManager.addMedicine("Medicine for BA ", "Topical");
        assertEquals(1, medicineManager.getAllMedicines().size());
        assertEquals("Topical", medicineManager.getAllMedicines().get(0).getType());
    }

    @Test
    public void testCreateWithIllegalValues() {
        Medicine m = medicineManager.addMedicine(null,null);
        System.out.println("+++++++++++" + m);
        medicineManager.addMedicine(new Medicine(null, null, null));
        assertEquals(0, medicineManager.getAllMedicines().size());
    }

    @Test
    public void testCreateWithEmptyValues() {
        medicineManager.addMedicine(" ","     ");
        assertEquals( "Unnamed", medicineManager.getAllMedicines().get(0).getName());
        assertEquals( "Undefined", medicineManager.getAllMedicines().get(0).getType());
    }

    @Test
    public void testCreateWithWhitespaceInNameAndType() {
        String name = "Me di ci ne";
        String type = "Ty     pe";
        medicineManager.addMedicine(name, type);
        assertEquals( name, medicineManager.getAllMedicines().get(0).getName());
        assertEquals( type, medicineManager.getAllMedicines().get(0).getType());
    }

    @Test
    public void testFillingAndGetting() {
        fillDatabase();
        assertEquals(getListOfMedicines().size(), medicineManager.getAllMedicines().size());
    }

    @Test
    public void testFillingAndGettingValues() {
        fillDatabase();
        for (int i =0; i < medicineManager.getAllMedicines().size() ; i++) {
            assertEquals(getListOfMedicines().get(i).getName(), medicineManager.getAllMedicines().get(i).getName());
            assertEquals(getListOfMedicines().get(i).getType(), medicineManager.getAllMedicines().get(i).getType());
        }
    }


    @Test
    public void testUpdateMedicine() {
        fillDatabase();
        int id = medicineManager.getAllMedicines().get(0).getPrimaryKey();
        Medicine replacementMed = new Medicine(id, "Ibuprofen", "New Disease Name");
        medicineManager.update(replacementMed);

        Medicine medInDB = medicineManager.getAllMedicines().get(0);
        assertEquals(replacementMed.getName(), medInDB.getName());
        assertEquals(replacementMed.getType(), medInDB.getType());
    }

    @Test
    public void testUpdateMedicineWithEmptyNameAndEmptyType() {
        fillDatabase();
        int id = medicineManager.getAllMedicines().get(0).getPrimaryKey();
        Medicine replacementMed = new Medicine(id, "", "");
        medicineManager.update(replacementMed);
        assertEquals("Unnamed", replacementMed.getName());
        assertEquals("Undefined", replacementMed.getType());
    }

    @Test
    public void testUpdateWithIllegalValues() {
        assertThrows(PersistenceException.class, () -> {
            fillDatabase();
            Medicine med = new Medicine(medicineManager.getAllMedicines().get(0).getPrimaryKey(), null, null);
            medicineManager.update(med);
        });
    }


    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        Medicine firstMed = medicineManager.getAllMedicines().get(0);
        Medicine foundMedicine = firstMed;
        int medPK = foundMedicine.getPrimaryKey();
        Medicine foundMedicineFromDB = medicineManager.getByPrimaryKey(medPK);

        assertThat(foundMedicine, samePropertyValuesAs(foundMedicineFromDB));

    }

    @Test
    public void testTwoEqualMedicines() {
        Medicine med1 = new Medicine("Med1", "type");
        Medicine med2 = new Medicine("Med1", "type");
        assertThat(med1, samePropertyValuesAs(med2));

    }

    @Test
    public void testGetIllegalPrimaryKey() {
        assertNull(medicineManager.getByPrimaryKey(-1));
    }


    @Test
    public void testDeleteAll() {
        // Delete all from empty database
        medicineManager.deleteAll();
        assertEquals(0, medicineManager.getAllMedicines().size());
        // Delete all from filled database
        fillDatabase();
        medicineManager.deleteAll();
        assertEquals(0, medicineManager.getAllMedicines().size());
    }

    @Test
    public void testDelete() {
        fillDatabase();
        medicineManager.delete(medicineManager.getAllMedicines().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals( getListOfMedicines().size()-1, medicineManager.getAllMedicines().size());
        medicineManager.delete(medicineManager.getAllMedicines().get(1).getPrimaryKey());
        assertEquals(getListOfMedicines().size()-2, medicineManager.getAllMedicines().size());
    }

    @Test
    public void testDeleteByPK() {
        fillDatabase();
        medicineManager.delete(medicineManager.getAllMedicines().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals(getListOfMedicines().size()-1, medicineManager.getAllMedicines().size());
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
            medicineManager.delete(med.getPrimaryKey());
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(medicineManager.getAllMedicines().size(), medicines);
            // Check that nothing has been removed
        }
    }

    /**
     * List to fill in database with example Medicine objects
     * @return example objects
     */
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
        listOfMedicines.add(new Medicine("Med7", "Inhaler"));
        listOfMedicines.add(new Medicine("Med_8/#", "Liquid"));
        listOfMedicines.add(new Medicine(-1, "Med7", "Topical"));

        return listOfMedicines;
    }

    private void fillDatabase() {
        ArrayList<Medicine> listOfMeds = getListOfMedicines();
        for (int i = 0; i<listOfMeds.size(); ++i) medicineManager.addMedicine(listOfMeds.get(i));
    }
}