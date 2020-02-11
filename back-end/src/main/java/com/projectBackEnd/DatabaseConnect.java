package main.java.com.projectBackEnd;

public interface DatabaseConnect {
    //Database object
    public void connectToDatabase(String credentials); // Probably throws something
    public void disconnectFromDatabase(); // Needed each time?
    public String[] queryDatabase(String query);
    public String toEscapeString(String toConvert);
}
