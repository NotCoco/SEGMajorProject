package test.java;

import main.java.com.projectBackEnd.Entities.AppInfo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;


public class AppInfoManagerTest {

    private static AppInfoManagerInterface infoManager;

    public AppInfoManagerTest() {
        infoManager = AppInfoManager.getInfoManager();
    }

    @BeforeEach
    public void setUp() {
        JSONLocation.setJsonFile("src/test/resources/AppInfoTest.json");
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
     * Unfound JSONs default to the main place.
     */
    @Test
    public void testEmptyJSONFile() {
        JSONLocation.setJsonFile("");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "Cool Department in a different dep"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "Cool Department in a different dep");
    }

    /**
     * Unfound Random JSON location should default to the default too.
     */
    @Test
    public void testUnfoundJSONFile() {
        JSONLocation.setJsonFile("fr43fdasdf");
        infoManager.updateInfo(new AppInfo("Interesting New Hospital", "resettospaghetto"));
        assertEquals(infoManager.getInfo().getDepartmentName(), "resettospaghetto");
    }
}
