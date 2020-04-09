package main.java.com.projectBackEnd.Services.AppInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * AppInfoManager to deal with updating and getting the information saved in the JSON for hospital details.
 */
public class AppInfoManager implements AppInfoManagerInterface {

    private static AppInfoManagerInterface infoManager;
    private static ObjectMapper mapper = new ObjectMapper();
    private static File file = JSONLocation.getJsonFile();
    //Set this before initialising the AppInfoManager for it to work.

    private AppInfo savedInfo;

    /**
     * Private constructor singleton information manager, setting the saved info to what is read from the JSON.
     */
    private AppInfoManager() {
        savedInfo = readInfo();
        infoManager = this;
    }

    /**
     * Get the singleton app info manager, as its interface.
     * @return The app info manager interface implementation singleton
     */
    public static AppInfoManagerInterface getInfoManager() {
        return (infoManager != null) ? infoManager : new AppInfoManager();
    }

    /**
     * Update the object stored in the json and fields of the manager object.
     * @param updatedVersion The new storage object for the JSON
     */
    public void updateInfo(AppInfo updatedVersion) {
        try {
            //file = JSONLocation.getJsonFile();
            //Uncomment this line for runtime changing of the JSONLocation class static.
            mapper.writeValue(file, updatedVersion);
            savedInfo = updatedVersion;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Info is used far more frequently, so gets an AppInfo directly without reading file.
     * @return The AppInfo object information is saved about
     */
    public AppInfo getInfo() {
        return savedInfo;
    }

    /**
     * Read information from the file, returning an AppInfo object.
     * @return The AppInfo object as specified in the JSON.
     */
    private static AppInfo readInfo() {
        try {
            return mapper.readValue(file, AppInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}