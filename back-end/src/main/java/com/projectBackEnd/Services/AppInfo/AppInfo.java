package main.java.com.projectBackEnd.Services.AppInfo;

/**
 * AppInfo object for storage of hospital names and department names.
 */
public class AppInfo {

    private String hospitalName;
    private String departmentName;

    /**
     * Default constructor
     */
    public AppInfo(){}

    /**
     * Create data storage objects
     * @param hospitalName      The name of the hospital
     * @param departmentName    The name of the department using the software
     */
    public AppInfo(String hospitalName, String departmentName) {
        this.hospitalName = hospitalName;
        this.departmentName = departmentName;
    }

    /**
     * Get the hospital name as stored in the object
     * @return The hospital name
     */
    public String getHospitalName() {
        return hospitalName;
    }

    /**
     * Set the hospital name as stored in the object
     * @param hospitalName New hospital name
     */
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    /**
     * Get the department name as stored in the object
     * @return The department name
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Set the department name to be stored in the object
     * @param departmentName New department name
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}