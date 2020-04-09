package test.java;

import main.java.com.projectBackEnd.Services.AppInfo.AppInfo;
import main.java.com.projectBackEnd.Services.AppInfo.AppInfoManager;
import main.java.com.projectBackEnd.Services.AppInfo.AppInfoManagerInterface;
import main.java.com.projectBackEnd.Services.AppInfo.JSONLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

/**
 * Testing the two methods in the AppInfoManagerInterface
 */
class AppInfoManagerTest {

    private static AppInfoManagerInterface infoManager;

    /**
     * Set the JSON's location and initialise a manager for testing
     */
    @BeforeEach
    void setUp() {
        JSONLocation.setJsonFile("AppInfoTest.json");
        //Setting this will create the file itself before the AppInfoManager is initialised
        infoManager = AppInfoManager.getInfoManager();
    }
//======================================================================================================================

    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    void testUpdateAndGetInformation() {
        infoManager.updateInfo(new AppInfo("Interesting Hospital", "Cool Department"));
        assertEquals(infoManager.getInfo().getHospitalName(), "Interesting Hospital");
    }


    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    void testUpdateAndGetInformationAgain() {
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department"));
        assertEquals(infoManager.getInfo().getHospitalName(), "Interesting New Hospital");
    }

    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    void testUpdateAndGetInformationOnceMore() {
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department in a different dep"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "Cool Department in a different dep");
    }

    /**
     * Test that changing the json file once the AppInfoManager has been initialised has no effect.
     */
    @Test
    void testEmptyJSONFile() {
        JSONLocation.setJsonFile("");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department in a different dep"));
            assertEquals(infoManager.getInfo().getDepartmentName(), "Cool Department in a different dep");
    }

    /**
     * Test that changing the json file once the AppInfoManager has been initialised has no effect.
     * (If runtime changing is enabled, this will create a new file with the spam name)
     */
    @Test
    void testUnfoundJSONFile() {
        JSONLocation.setJsonFile("fr43fdasdf");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "resettospaghetto"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "resettospaghetto");
    }
}
