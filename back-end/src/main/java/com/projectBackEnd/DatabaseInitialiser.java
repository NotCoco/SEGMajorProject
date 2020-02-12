package main.java.com.projectBackEnd;
import java.sql.*;
import java.util.HashSet;

/**
 * An initialiser class for a database. Running this with the correct database credentials
 * will initialise the classes
 */
public class DatabaseInitialiser { //https://www.tutorialspoint.com/jdbc/jdbc-create-tables.htm
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/testunittest"; //TODO OPEN TO CHANGE, make user friendly
    private static final String USER = "root";
    private static final String PASS = "";

    /**
     * Main method will connect and run SQL CreateQueries from all the different types of table
     * the schema has. New storage databases that want to use this method will need
     * to add their name and class to the getAllCreateQueries method below.
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            for (String createQuery : getAllCreateQueries()) {
                statement.execute(createQuery);
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null || connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Creates a hash set of strings of Create Queries for DB initialisation.
     * @return Set of queries strings from each Table entity.
     */
    private static HashSet<String> getAllCreateQueries() {
        HashSet<String> allCreateQueries = new HashSet<>();
        allCreateQueries.add(
            new SQLSafeString(Page.getCreateQuery()).toString()
        );
        /*allCreateQueries.add(
            new SQLSafeString(Medicine.getCreateQuery()).toString()
        );*/
        /*allCreateQueries.add(
            new SQLSafeString(Login.getCreateQuery()).toString()
        );*/
        return allCreateQueries;
    }
}


