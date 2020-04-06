package main.java.com.projectBackEnd.Entities.AppInfo;

/**
 *  Methods used by AppInfoManager for database queries.
 */

public interface AppInfoManagerInterface {
    void updateInfo(AppInfo updatedVersion);
    AppInfo getInfo();
}
