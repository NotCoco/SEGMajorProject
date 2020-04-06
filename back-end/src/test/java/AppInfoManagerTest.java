package test.java;

import main.java.com.projectBackEnd.Entities.AppInfo.AppInfo;
import main.java.com.projectBackEnd.Entities.AppInfo.AppInfoManager;
import main.java.com.projectBackEnd.Entities.AppInfo.AppInfoManagerInterface;
import main.java.com.projectBackEnd.Entities.AppInfo.JSONLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

/**
 * Testing the two methods in the AppInfoManagerInterface
 */
public class AppInfoManagerTest {

    private static AppInfoManagerInterface infoManager;

    /**
     * Default Constructor
     */
    public AppInfoManagerTest() {

    }

    /**
     * Set the JSON's location and initialise a manager for testing
     */
    @BeforeEach
    public void setUp() {
        JSONLocation.setJsonFile("src/test/resources/AppInfoTest.json");
        //Setting this will create the file itself before the AppInfoManager is initialised
        infoManager = AppInfoManager.getInfoManager();
    }
//======================================================================================================================

    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    public void testUpdateAndGetInformation() {
        infoManager.updateInfo(new AppInfo("Interesting Hospital", "Cool Department"));
        assertEquals(infoManager.getInfo().getHospitalName(), "Interesting Hospital");
    }


    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    public void testUpdateAndGetInformationAgain() {
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department"));
        assertEquals(infoManager.getInfo().getHospitalName(), "Interesting New Hospital");
    }

    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    public void testUpdateAndGetInformationOnceMore() {
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department in a different dep"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "Cool Department in a different dep");
    }

    /**
     * Test that changing the json file once the AppInfoManager has been initialised has no effect.
     */
    @Test
    public void testEmptyJSONFile() {
        JSONLocation.setJsonFile("");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department in a different dep"));
            assertEquals(infoManager.getInfo().getDepartmentName(), "Cool Department in a different dep");
    }

    /**
     * Test that changing the json file once the AppInfoManager has been initialised has no effect.
     * (If runtime changing is enabled, this will create a new file with the spam name)
     */
    @Test
    public void testUnfoundJSONFile() {
        JSONLocation.setJsonFile("fr43fdasdf");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "resettospaghetto"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "resettospaghetto");
    }
}
