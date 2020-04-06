package test.java;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;

import javax.inject.Inject;

import main.java.com.projectBackEnd.Entities.AppInfo.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.HibernateUtility;

/**
 * Testing REST API endpoints for the controller and its interactions with HTTP Requests
 */
@MicronautTest
class AppInfoControllerTest {

    @Inject
    @Client("/")
    private HttpClient client;

    private static String token;

    /**
     * Set up the database to have a test administrator user with a token for restricted requests
     */
    @BeforeAll
    static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        JSONLocation.setJsonFile("src/test/resources/AppInfoTest.json");

        try {
            UserManager.getUserManager().addUser("test@test.com", "123", "name");
            token = UserManager.getUserManager().verifyUser("test@test.com", "123");
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Deletes the test user and shutsdown the database.
     */
    @AfterAll
    static void closeDatabase() {
        try {
            UserManager.getUserManager().deleteUser("test@test.com", "123");
        } catch (Exception e) {
            fail();
        }
        HibernateUtility.shutdown();
    }

    /**
     * Tests updating information and that the getter returns the correct new information
     */
    @Test
    void testUpdatingInfo() {
        updateInformation(new AppInfo("Interesting New Hospital", "Cool Department"));
        assertEquals(getInfo().getHospitalName(), "Interesting New Hospital");
    }

    /**
     * Test updating information again, to return the new information
     */
    @Test
    void testUpdatingAndGettingInfo() {
        updateInformation(new AppInfo("Cool", "Cool Department"));
        assertEquals(getInfo().getHospitalName(), "Cool");
    }

    /**
     * Test updating information once more, to return new information
     */
    @Test
    void testUpdatingAndGettingInfoAgain() {
        updateInformation(new AppInfo("Fancy update", "Cool Department"));
        assertEquals(getInfo().getHospitalName(), "Fancy update");
    }


    /**
     * Creates a PUT request with the updated information supplying API Key and new information
     * @param updatedInfo The new information to be updated
     * @return The HTTP response governing success
     */
    private HttpResponse updateInformation(AppInfo updatedInfo){
        HttpRequest request = HttpRequest.PUT("/appinfo", updatedInfo).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }

    /**
     * Runs a GET request to get the information currently stored
     * @return The information AppInfo object stored.
     */
    private AppInfo getInfo(){
        HttpRequest request = HttpRequest.GET("/appinfo/");
        return client.toBlocking().retrieve(request, AppInfo.class);
    }

}