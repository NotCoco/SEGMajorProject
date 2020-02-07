package main.java.com.projectBackEnd;

public interface Database {
    public void connectToDatabase(); // Probably throws something
    public void disconnectFromDatabase(); // Needed each time?
    public String[] queryDatabase(String query);
    public String createDiseaseQuery(String tableName);

    public String updateDisease(String tableName, String attributeToChange, String newValue, String toUpdateID);
    public String updateDiseaseTupleXCoordQuery(String tableName, String toUpdateID, String newXCoord);
    public String updateDiseaseTupleYCoordQuery(String tableName, String toUpdateID, String newXCoord);
    public String updateDiseaseTupleContentQuery(String tableName, String toUpdateID, String newXCoord);

    public String deleteDiseaseTupleQuery(String tableName, String toDeleteID);
    public String addDiseaseTupleQuery(String tableName, String content, int XCoord, int YCoord, String type, int page);
    public String deleteDiseaseQuery(String tableName);

    public String allDiseaseNamesQuery();
    public String allDiseaseInfoQuery(String diseaseID);

    public String safeString(String toConvert); //MYSQLi Escape etc.
}
