package test.java;

import org.junit.AfterClass;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class BackEndUnitTesting {

    String randomTableName;

    @BeforeClass
    public void completeDatabaseConnection() {
        // try connect to Database. Maybe move this into its own test.

        // Generate random database table string name for testing
        // e.g. TestTable[Hash Random Number]. SQLDatabase.createDisease(...)
    }

    @AfterClass
    public void disconnectFromDatabase() {
        // Disconnect from Database.

        // Drop random database table string name from before.
    }

    @Test
    public void testCorrectTableCreated() {
        // assertThat(Database . getTableName, equalTo("randomTableName) );
    }
}