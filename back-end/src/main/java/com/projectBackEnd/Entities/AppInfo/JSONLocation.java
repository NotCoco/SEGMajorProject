package main.java.com.projectBackEnd.Entities.AppInfo;

import java.io.File;

public class JSONLocation {

    private static File jsonFile = new File("src/main/resources/AppInfoTest.json");

    public static void setJsonFile(String newLocation) {
        jsonFile = new File(newLocation);
    }

    public static File getJsonFile() {
        return jsonFile;
    }
}