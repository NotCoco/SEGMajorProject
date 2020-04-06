package main.java.com.projectBackEnd.Entities.AppInfo;

import java.io.File;

/**
 * Static class for changing the location of the JSON file - so that the location can be changed
 * before the AppInfoManager is initialised to a test location
 */
public class JSONLocation {


    private static File jsonFile = new File("src/main/resources/AppInfoMain.json");

    /**
     * Set the JSON's location
     * @param newLocation New location string path
     */
    public static void setJsonFile(String newLocation) {
        jsonFile = new File(newLocation);
    }

    /**
     * Get the json file from its location
     * @return The JSON file itself.
     */
    static File getJsonFile() {
        return jsonFile;
    }
}