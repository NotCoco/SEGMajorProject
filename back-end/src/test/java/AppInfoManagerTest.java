package test.java;

import main.java.com.projectBackEnd.Services.AppInfo.AppInfo;
import main.java.com.projectBackEnd.Services.AppInfo.AppInfoManager;
import main.java.com.projectBackEnd.Services.AppInfo.AppInfoManagerInterface;
import main.java.com.projectBackEnd.Services.AppInfo.JSONLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Testing the two methods in the AppInfoManagerInterface
 */
class AppInfoManagerTest {

    private static AppInfoManagerInterface infoManager;
    private static String mainTestPath = "src/test/resources/AppInfoTest.json";
    /**
     * Set the JSON's location and initialise a manager for testing
     */
    @BeforeAll
    void setUp() {
        JSONLocation.setJsonFile(mainTestPath);
        //Setting this will create the file itself before the AppInfoManager is initialised
        infoManager = AppInfoManager.getInfoManager();
    }

    /**
     * Reset the test path
     */
    @AfterEach
    void reset() {
        JSONLocation.setJsonFile(mainTestPath);
    }
//======================================================================================================================

    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    void testUpdateAndGetInformation() {
        infoManager.updateInfo(new AppInfo("Interesting Hospital", "Cool Department"));
        assertEquals("Interesting Hospital", infoManager.getInfo().getHospitalName());
    }


    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    void testUpdateAndGetInformationAgain() {
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department"));
        assertEquals("Interesting New Hospital", infoManager.getInfo().getHospitalName());
    }

    /**
     * Test setting hospital's information, multiple times to show rewrites
     */
    @Test
    void testUpdateAndGetInformationOnceMore() {
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department in a different dep"));
        assertEquals("Cool Department in a different dep", infoManager.getInfo().getDepartmentName());
    }

    /**
     * Test that changing the json file once the AppInfoManager has been initialised has no effect.
     * The JSON Location is invalidated.
     */
    @Test
    void testEmptyJSONFile() {
        JSONLocation.setJsonFile("");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Unique Update Message That Won't Happen"));
        assertNotEquals("Unique Update Message That Won't Happen", infoManager.getInfo().getDepartmentName());
    }

    /**
     * Test that changing the json file once the AppInfoManager has been initialised has no effect.
     * (If runtime changing is enabled, this will create a new file with the spam name)
     */
    @Test
    void testUnfoundJSONFile() {
        String newPath = System.getProperty("user.dir")+"/fr43fdasdf";
        JSONLocation.setJsonFile(newPath);
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "resettospaghetto"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "resettospaghetto");
        File created = new File(newPath);
        assertTrue(created.exists());
        created.delete();
    }
}
