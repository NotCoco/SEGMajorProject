package main.java.com.projectBackEnd.Entities.AppInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class AppInfoManager implements AppInfoManagerInterface {

    private static AppInfoManagerInterface infoManager;
    private static ObjectMapper mapper = new ObjectMapper();;
    private static File file = JSONLocation.getJsonFile();

    private AppInfo savedInfo;

    private AppInfoManager() {
        savedInfo = readInfo();
        infoManager = this;
    }

    public static AppInfoManagerInterface getInfoManager() {
        return (infoManager != null) ? infoManager : new AppInfoManager();
    }

    public void updateInfo(AppInfo updatedVersion) {
        try {
            file = JSONLocation.getJsonFile();
            mapper.writeValue(file, updatedVersion);
            savedInfo = readInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AppInfo getInfo() {
        return savedInfo;
    }

    private static AppInfo readInfo() {
        try {
            file = JSONLocation.getJsonFile();
            return mapper.readValue(file, AppInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
