package main.java.com.projectBackEnd.Entities.AppInfo;
public class AppInfo {

    private String hospitalName;
    private String departmentName;

    public AppInfo(){}

    public AppInfo(String hospitalName, String departmentName) {
        this.hospitalName = hospitalName;
        this.departmentName = departmentName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}