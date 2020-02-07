package main.java.com.projectBackEnd;

public interface Database {
    public void connectToDatabase(); // Probably throws something
    public void disconnectFromDatabase(); // Needed each time?
    public String[] queryDatabase(String query);
    public String createDiseaseQuery(String tableName);
    public String updateDiseaseInformationQuery(String tableName, int toUpdateID, String newText);
    public String deleteDiseaseInformationQuery(String tableName, int toDeleteID);
    public String createDiseaseInformationQuery(String tableName, int toCreate);
    public String deleteDiseaseQuery(String tableName);

    public String safeString(String toConvert); //MYSQLi Escape etc.
}
