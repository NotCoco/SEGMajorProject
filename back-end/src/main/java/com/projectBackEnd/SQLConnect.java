package main.java.com.projectBackEnd;

public class SQLConnect implements DatabaseConnect {

    //Database object
    public void connectToDatabase(String credentials) {} // Probably throws something
    public void disconnectFromDatabase() {} // Needed each time?
    public String[] queryDatabase(String query) {
        return null; //Database object.runQuery(...)
    }
    public String toEscapeString(String toConvert) { return toConvert;  }
}
