package main.java.com.projectBackEnd;

public class SQLDatabase implements Database { // Consider static
    //Database object = ...; Something we can continue querying on.

    @Override
    public void connectToDatabase() { // Throws... ?
        //Connect us to the database
    }

    @Override
    public void disconnectFromDatabase() { // Throws ... ?
        //Disconnects us from the database, probably each time ?
    }

    @Override
    public String[] queryDatabase(String query) {
        //Query a database, empty or boolean etc.
        return null;
    }

    @Override
    public String createDiseaseQuery(String tableName) {
        String query = "CREATE TABLE ..." + safeString(tableName); // With auto assigning ID?
        return query;
    }

    @Override
    public String updateDiseaseInformationQuery(String tableName, int toUpdateID, String newText) {
        String query = "UPDATE ...";
        return query;
    }

    @Override
    public String deleteDiseaseInformationQuery(String tableName, int toDeleteID) {
        String query = "DELETE ...";
        return query;
    }

    @Override
    public String createDiseaseInformationQuery(String tableName, int toCreate) {
        String query = "INSERT INTO ..."; // Should have auto assigning ID? Deal with this !
        return query;
    }

    @Override
    public String deleteDiseaseQuery(String tableName) {
        String query = "DROP TABLE...";
        return query;
    }

    @Override
    public String safeString(String toConvert) {
        String safeString = toConvert.replaceAll(" ", "_").toUpperCase(); // The SQL ASCII thing
        return safeString;
    }
}

