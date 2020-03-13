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
import java.util.List;

import static org.junit.Assert.*;


public class MedicineManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static MedicineManagerInterface medicineManager = null;

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        medicineManager = MedicineManager.getMedicineManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        medicineManager.deleteAll();
    }


    @Test
    public void testCreateMedicine() {
        Medicine med = new Medicine("Medicine for BA", "Injection");
        assertEquals("Medicine for BA", med.getName());
        assertEquals("Injection", med.getType());
    }

    @Test
    public void testCreateAndSaveMedicine() {
        medicineManager.addMedicine("Medicine for BA ", "Topical");
        assertEquals(1, medicineManager.getAllMedicines().size());
    }

    @Test
    public void testCreateWithIllegalValues() {
        medicineManager.addMedicine(null,null);
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
    public void testGetAllByType() {
        fillDatabase();
        List<Medicine> typeLiquidMedicines = medicineManager.getAllMedicinesByType("Liquid");
        int size = typeLiquidMedicines.size();
        assertEquals(3, size);
        assertEquals("Med1", typeLiquidMedicines.get(0).getName());
        assertEquals("Med4", typeLiquidMedicines.get(1).getName());
        assertEquals("Med_8/#", typeLiquidMedicines.get(2).getName());
    }

    @Test
    public void testGetAllByName() {
        fillDatabase();
        List<Medicine> nameMed4Medicines = medicineManager.getAllMedicinesByName("Med4");
        int size = nameMed4Medicines.size();
        assertEquals(2, size);
        assertEquals("Tablet", nameMed4Medicines.get(0).getType());
        assertEquals("Liquid", nameMed4Medicines.get(1).getType());
    }

    @Test
    public void getByTypeShouldReturnEmpty(){
        fillDatabase();
        List<Medicine> typeNoMedicines = medicineManager.getAllMedicinesByType("No");
        int size = typeNoMedicines.size();
        assertEquals(0, size);
    }

    @Test
    public void getByNameShouldReturnEmpty(){
        fillDatabase();
        List<Medicine> nameNoMedicine = medicineManager.getAllMedicinesByName("No");
        int size = nameNoMedicine.size();
        assertEquals(0, size);
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

    @Test(expected = PersistenceException.class)
    public void testUpdateWithIllegalValues() {
        fillDatabase();
        Medicine med = new Medicine(medicineManager.getAllMedicines().get(0).getPrimaryKey(), null, null);
        medicineManager.update(med);
    }


    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        Medicine firstMed = medicineManager.getAllMedicines().get(0);
        Medicine foundMedicine = firstMed;
        int medPK = foundMedicine.getPrimaryKey();
        Medicine foundMedicineFromDB = medicineManager.getByPrimaryKey(medPK);

        assertThat(foundMedicine, samePropertyValuesAs(foundMedicineFromDB));
        assertTrue(foundMedicine.equals((foundMedicineFromDB)));
    }

    @Test
    public void testTwoEqualMedicines() {
        Medicine med1 = new Medicine("Med1", "type");
        Medicine med2 = new Medicine("Med1", "type");
        assertTrue(med1.equals(med2));
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
        medicineManager.delete(medicineManager.getAllMedicines().get(1)); //Testing object deletion
        assertEquals( getListOfMedicines().size()-1, medicineManager.getAllMedicines().size());
        medicineManager.delete(medicineManager.getAllMedicines().get(1));
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
            medicineManager.delete(med);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(medicineManager.getAllMedicines().size(), medicines);
            // Check that nothing has been removed
        }
    }

    /**
     * Fill in database with example Medicine objects
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
        for (Medicine med : getListOfMedicines()) medicineManager.addMedicine(med);
    }

}