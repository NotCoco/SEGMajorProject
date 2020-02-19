package test.java;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.testing.jdbc.leak.IdleConnectionCounter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//TODO REMOVE TESTING CLASS: ONLY FOR DB LEAK CHECKING
/**
 * https://github.com/hibernate/hibernate-orm/tree/master/hibernate-testing/src/main/java/org/hibernate/testing/jdbc/leak
 */
public class MySQLIdleConnectionCounter implements IdleConnectionCounter {

    public static final IdleConnectionCounter INSTANCE =
            new MySQLIdleConnectionCounter();

    @Override
    public boolean appliesTo(Class<? extends Dialect> dialect) {
        return MySQL5Dialect.class.isAssignableFrom( dialect );
    }

    @Override
    public int count(Connection connection) {
        try ( Statement statement = connection.createStatement() ) {
            try ( ResultSet resultSet = statement.executeQuery(
                    "SHOW PROCESSLIST" ) ) {
                int count = 0;
                while ( resultSet.next() ) {
                    String state = resultSet.getString( "command" );
                    if ( "sleep".equalsIgnoreCase( state ) ) {
                        count++;
                    }
                }
                return count;
            }
        }
        catch ( SQLException e ) {
            throw new IllegalStateException( e );
        }
    }
}