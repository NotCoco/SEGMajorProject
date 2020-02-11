package main.java.com.projectBackEnd;

public class SingleTableDatabase {
    public final static String DISEASESTABLENAME = "DISEASES";
    public final static String BLOCKSTABLENAME = "BLOCKS";
    private static final String ATTRIBUTENAMECONTENT = "content";
    private static final String ATTRIBUTENAMEXCOORD = "xCoord";
    private static final String ATTRIBUTENAMEYCOORD = "yCoord";
    private static final String ATTRIBUTENAMETYPE = "type";
    private static final String ATTRIBUTENAMEPAGE = "page";

    private static final String ATTRIBUTENAMEID = "id";
    private static final String ATTRIBUTEDISEASESOURCE = "disease";

    public SingleTableDatabase() {
        connectToDatabase();
        queryDatabase("CREATE TABLE " + safeString(DISEASESTABLENAME) + " (" + ATTRIBUTEDISEASESOURCE + " VARCHAR(255), PRIMARY KEY (" + ATTRIBUTEDISEASESOURCE + "));");
        String createBlocksTable ="CREATE TABLE " + safeString(BLOCKSTABLENAME) + " (";
        createBlocksTable += ATTRIBUTENAMEID + " INTEGER NOT NULL AUTO_INCREMENT,";
        createBlocksTable += ATTRIBUTEDISEASESOURCE + " VARCHAR(255) NOT NULL,";
        createBlocksTable += ATTRIBUTENAMECONTENT + " TEXT NOT NULL,";
        createBlocksTable += ATTRIBUTENAMEXCOORD + " INTEGER NOT NULL,";
        createBlocksTable += ATTRIBUTENAMEYCOORD + " INTEGER NOT NULL,";
        createBlocksTable += ATTRIBUTENAMETYPE + " VARCHAR(3) NOT NULL,";
        createBlocksTable += ATTRIBUTENAMEPAGE + " INTEGER NOT NULL,";
        createBlocksTable += "PRIMARY KEY (" + safeString(ATTRIBUTENAMEID) + "),";
        createBlocksTable += "FOREIGN KEY (" + safeString(ATTRIBUTEDISEASESOURCE) + " REFERENCES " + safeString(DISEASESTABLENAME) + "(" + ATTRIBUTEDISEASESOURCE + ") ON DELETE CASCADE );";
        queryDatabase(createBlocksTable);
        disconnectFromDatabase();

    }
    public void connectToDatabase() {} // Probably throws something
    public void disconnectFromDatabase() {} // Needed each time?
    public String[] queryDatabase(String query) { return null; }

    public String createDiseaseQuery(String diseaseName) {
        return "INSERT INTO " + safeString(DISEASESTABLENAME) + " VALUES ('" + safeString(diseaseName) + "');";
    }

    public String updateDisease(String tableName, String attributeToChange, String newValue, String toUpdateBlockID) {
        return "UPDATE " + safeString(BLOCKSTABLENAME) + " SET " + safeString(attributeToChange) +
                " = '" + safeString(newValue) + "' WHERE " + ATTRIBUTENAMEID + " = '" + safeString(toUpdateBlockID) + "';";
    }
    public String updateDiseaseTupleXCoordQuery(String tableName, String toUpdateID, String newXCoord) {
        return updateDisease(tableName, ATTRIBUTENAMEXCOORD, newXCoord, toUpdateID);
    }
    public String updateDiseaseTupleYCoordQuery(String tableName, String toUpdateID, String newYCoord) {
        return updateDisease(tableName, ATTRIBUTENAMEYCOORD, newYCoord, toUpdateID);
    }
    public String updateDiseaseTupleContentQuery(String tableName, String toUpdateID, String newContent) {
        return updateDisease(tableName, ATTRIBUTENAMECONTENT, newContent, toUpdateID);
    }

    public String deleteDiseaseTupleQuery(String tableName, String toDeleteID) {
        return "DELETE FROM " + safeString(BLOCKSTABLENAME) + " WHERE " + safeString(ATTRIBUTENAMEID) + " = '" + safeString(toDeleteID) + "';";
    }
    public String addDiseaseTupleQuery(String diseaseName, String content, int XCoord, int YCoord, String type, int page) {
        String query = "INSERT INTO "; // Should have auto assigning ID? Deal with this !
        query += safeString(BLOCKSTABLENAME) + "(" +
                safeString(ATTRIBUTEDISEASESOURCE) + ", " +
                safeString(ATTRIBUTENAMECONTENT) + ", " +
                safeString(ATTRIBUTENAMEXCOORD)+ ", " +
                safeString(ATTRIBUTENAMEYCOORD) + ", " +
                safeString(ATTRIBUTENAMETYPE) + ", " +
                safeString(ATTRIBUTENAMEPAGE) + ", " + ") VALUES (" +
                "'" + safeString(diseaseName) + "'," +
                "'" + safeString(content) + "'," + //These need to be safe right
                "'" + safeString(XCoord + "") + "'," +
                "'" + safeString(YCoord + "") + "'," +
                "'" + safeString(type) + "'," +
                "'" + safeString(page + "") + "');";
        return query;
    }
    public String deleteDiseaseQuery(String diseaseName) {
        return "DELETE FROM " + safeString(DISEASESTABLENAME) + " WHERE " + safeString(ATTRIBUTEDISEASESOURCE) + " = '" + safeString(diseaseName) + "';";
    }

    public String allDiseaseNamesQuery() {
        return "SELECT * FROM " + safeString(DISEASESTABLENAME);
    }
    public String allDiseaseInfoQuery(String diseaseName) {
        return "SELECT * FROM " + safeString(BLOCKSTABLENAME) + " WHERE " + safeString(ATTRIBUTEDISEASESOURCE) + " = '" + safeString(diseaseName) + "';";
    }

    public String safeString(String toConvert) { return toConvert; } //MYSQLi Escape etc. TODO
}
