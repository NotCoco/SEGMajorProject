package test.java;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;

import javax.inject.Inject;

import main.java.com.projectBackEnd.Entities.AppInfo.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.HibernateUtility;


@MicronautTest
public class AppInfoControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    static AppInfoManagerInterface infoManager;
    private static String token;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        JSONLocation.setJsonFile("src/test/resources/AppInfoTest.json");
        infoManager = AppInfoManager.getInfoManager();
        try {
            UserManager.getUserManager().addUser("test@test.com", "123", "name");
            token = UserManager.getUserManager().verifyUser("test@test.com", "123");
        } catch (Exception e) {
            fail();
        }
    }

    @AfterAll
    public static void closeDatabase() {
        try {
            UserManager.getUserManager().deleteUser("test@test.com", "123");
        } catch (Exception e) {
            fail();
        }
        HibernateUtility.shutdown();
    }

    @Test
    public void testUpdatingInfo() {
        updateInformation(new AppInfo("Interesting New Hospital", "Cool Department"));
        assertEquals(getInfo().getHospitalName(), "Interesting New Hospital");
    }

    @Test
    public void testUpdatingAndGettingInfo() {
        updateInformation(new AppInfo("Cool", "Cool Department"));
        assertEquals(getInfo().getHospitalName(), "Cool");
    }

    @Test
    public void testUpdatingAndGettingInfoAgain() {
        updateInformation(new AppInfo("Fancy update", "Cool Department"));
        assertEquals(getInfo().getHospitalName(), "Fancy update");
    }


    protected HttpResponse updateInformation(AppInfo updatedInfo){
        HttpRequest request = HttpRequest.PUT("/appinfo", updatedInfo).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }

    protected AppInfo getInfo(){
        HttpRequest request = HttpRequest.GET("/appinfo/");
        return client.toBlocking().retrieve(request, AppInfo.class);
    }

}