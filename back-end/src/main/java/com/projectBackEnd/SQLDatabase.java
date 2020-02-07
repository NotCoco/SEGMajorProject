package main.java.com.projectBackEnd;

public class SQLDatabase implements Database { // Consider static
    //Database object = ...; Something we can continue querying on.

    // Defining table headers for the schema where they're hard coded here, only for
    // Creation and update statements etc.
    private static final String ATTRIBUTENAMECONTENT = "content";
    private static final String ATTRIBUTENAMEXCOORD = "xCoord";
    private static final String ATTRIBUTENAMEYCOORD = "yCoord";
    private static final String ATTRIBUTENAMETYPE = "type";
    private static final String ATTRIBUTENAMEPAGE = "page";

    private static final String ATTRIBUTENAMEID = "id";


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
        //Query a database, empty or boolean etc. For select statements
        //Or for queryDatabase(updateDiseaseContentQuery(...))
        return null;
    }

    @Override
    public String createDiseaseQuery(String tableName) {
        String query = "CREATE TABLE " + safeString(tableName) + " ("; // With auto assigning ID?
        query += ATTRIBUTENAMEID + " INTEGER NOT NULL AUTO_INCREMENT," +
            ATTRIBUTENAMECONTENT + " TEXT," +
            ATTRIBUTENAMEXCOORD + " INTEGER NOT NULL," +
            ATTRIBUTENAMEYCOORD + " INTEGER NOT NULL," +
            ATTRIBUTENAMETYPE + " VARCHAR(3) NOT NULL," + // e.g. IMG, VID, TXT, HDR
            ATTRIBUTENAMEPAGE + " INTEGER NOT NULL," +
            "PRIMARY KEY (" + ATTRIBUTENAMEID +
            ")" +
            ");";
        return query;
    }

    @Override
    public String updateDisease(String tableName, String toUpdateID, String newValue, String attributeToChange) {
        return "UPDATE " + safeString(tableName) + " SET " + safeString(attributeToChange) +
                " = '" + safeString(newValue) + "' WHERE " +  ATTRIBUTENAMEID + " = '" + safeString(toUpdateID) + "';";
    }

    // Methods the front end can actually call so that they don't need to
    // Know back end database attribute headers:

    @Override
    public String updateDiseaseTupleXCoordQuery(String tableName, String toUpdateID, String newXCoord) {
        return updateDisease(tableName, ATTRIBUTENAMEXCOORD, newXCoord, toUpdateID);
    }
    @Override
    public String updateDiseaseTupleYCoordQuery(String tableName, String toUpdateID, String newXCoord) {
        return updateDisease(tableName, ATTRIBUTENAMEYCOORD, newXCoord, toUpdateID);
    }
    @Override
    public String updateDiseaseTupleContentQuery(String tableName, String toUpdateID, String newXCoord) {
        return updateDisease(tableName, ATTRIBUTENAMECONTENT, newXCoord, toUpdateID);
    }


    @Override
    public String deleteDiseaseTupleQuery(String tableName, String toDeleteID) {
        return "DELETE FROM " + safeString(tableName) + " WHERE " + ATTRIBUTENAMEID + " = '" + safeString(toDeleteID) + "';";
    }

    @Override
    public String addDiseaseTupleQuery(String tableName, String content, int XCoord, int YCoord, String type, int page) {
        String query = "INSERT INTO "; // Should have auto assigning ID? Deal with this !
        query += safeString(tableName) + "(" + ATTRIBUTENAMECONTENT + ", " +
                ATTRIBUTENAMEXCOORD + ", " +
                ATTRIBUTENAMEYCOORD + ", " +
                ATTRIBUTENAMETYPE + ", " +
                ATTRIBUTENAMEPAGE + ", " + ") VALUES (" +
                "'" + safeString(content) + "'," + //These need to be safe right
                "'" + safeString(XCoord + "") + "'," +
                "'" + safeString(YCoord + "") + "'," +
                "'" + safeString(type) + "'," +
                "'" + safeString(page + "") + "');";
        return query;
    }

    @Override
    public String deleteDiseaseQuery(String tableName) {
        return "DROP TABLE " + safeString(tableName) + ";";
    }

    @Override
    public String safeString(String toConvert) { //TODO
        String safeString = toConvert.replaceAll(" ", "_").toUpperCase(); // The SQL ASCII thing
        return safeString;
    }
}

