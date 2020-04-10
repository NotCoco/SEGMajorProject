package test.java;

import main.java.com.projectBackEnd.Services.AppInfo.AppInfo;
import main.java.com.projectBackEnd.Services.AppInfo.AppInfoManager;
import main.java.com.projectBackEnd.Services.AppInfo.AppInfoManagerInterface;
import main.java.com.projectBackEnd.Services.AppInfo.JSONLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import java.io.File;
import static org.junit.Assert.*;

/**
 * This class tests that a JSON will be created if the file location is not existing already.
 * It will take place on servers etc. It must be in a separate class as the file is a class static variable
 * To test this class, it must be executed separately from other classes as if other AppInfo classes run first,
 * the static will be preset.
 */
class AppInfoJSONCreationTest {

    private static AppInfoManagerInterface infoManager;
    private static String path = "HelloIWillExist.json";

    /**
     * Set the JSON's location and initialise a manager for testing
     */
    @BeforeEach
    void setUp() {
        JSONLocation.setJsonFile(path);
        //Setting this will create the file itself before the AppInfoManager is initialised
        infoManager = AppInfoManager.getInfoManager();
    }

    /**
     * Delete the file we created
     */
    @AfterAll
    static void deleteFile() {
        File madeFile = new File(path);
        if (madeFile.exists()) madeFile.delete();
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

}