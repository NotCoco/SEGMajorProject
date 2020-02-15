package main.java.com.projectBackEnd;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * An initialiser class for a database. Running this with the correct database credentials
 * will initialise the classes
 */
public class DatabaseInitialiser { //https://www.tutorialspoint.com/jdbc/jdbc-create-tables.htm
    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost/testunittest"; //TODO OPEN TO CHANGE, make user friendly
    private static String USER = "root";
    private static String PASS = "";

    /**
     * Main method will connect and run SQL CreateQueries from all the different types of table
     * the schema has. New storage databases that want to use this method will need
     * to add their name and class to the getAllCreateQueries method below.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 0) resetDatabaseDetails(args);
        runCollectionOfQueries(getAllCreateQueries());
    }

    private static void runCollectionOfQueries(Collection<String> queries) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            for (String createQuery : queries) {
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
                e.printStackTrace();
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
            (Page.getCreateQuery())
        );
        /*allCreateQueries.add(
            new SQLSafeString(Medicine.getCreateQuery()).toString()
        );*/
        /*allCreateQueries.add(
            new SQLSafeString(Login.getCreateQuery()).toString()
        );*/ //TODO Fill this in with all the other entity create queries.
        return allCreateQueries;
    }

    private static HashSet<String> getAllDropQueries() {
        HashSet<String> allDropQueries = new HashSet<>();
        allDropQueries.add(
                ("DROP TABLE " + Page.TABLENAME)
        );

        return allDropQueries;
    }

    public static void dropAllTables() {
        runCollectionOfQueries(getAllDropQueries());
    }

    private static void resetDatabaseDetails(String[] args) {
        if (args.length > 0) {
            System.out.println("Main method takes arguments such as 'localhost/testdatabase', then user/pass.");
            DB_URL = "jdbc:mysql://" + args[0];
        }
        if (args.length == 3) {
            USER = args[1];
            PASS = args[2];
        }
    }
}


