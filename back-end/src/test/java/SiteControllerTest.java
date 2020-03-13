package test.java;


import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Site.*;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class SiteControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    static SiteManagerInterface siteManager;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("test/resources/testhibernate.cfg.xml");
        siteManager = SiteManager.getSiteManager();
    }

    @AfterAll
    public static void closeDatabase() {
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
        siteManager.deleteAll();
    }

    @Test
    public void testPutLegalMedicine(){
        HttpResponse response= addSite("legalSite");
        String url = getEUrl(response);
        int id =  getPKByName(url);
        response = putSite(id, "NewName");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    @Test
    public void testAddLegalSite(){
        HttpResponse response= addSite("legalSite");
        assertEquals(HttpStatus.CREATED, response.getStatus());
    }

    @Test
    public void testUpdateNullNameSite(){
        HttpResponse response = addSite("testSite");
        String url =  getEUrl(response);
        int id = getPKByName(url);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/sites", new Site(id, "")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testAddNullNameSite(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/medicine", new SiteAddCommand("")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testDeleteAndGetSite(){
        HttpResponse response = addSite("testSite");
        String url =  getEUrl(response);
        int id = getPKByName(url);
        // Asserting that we've added a site
        assertEquals(HttpStatus.CREATED, response.getStatus());

        HttpRequest request = HttpRequest.DELETE("/sites/"+id);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/sites/"+url));
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testAddAndGetSite(){
        HttpResponse response = addSite("testSite");
        String id =  getEUrl(response);

        Site testSite = getSite(id);

        assertEquals("testSite", testSite.getName());
    }

    @Test
    public void testNonExistingSiteReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/sites/IpsumLoremSite"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testAddAndUpdateSite(){
        HttpResponse response = addSite("testSite");
        String url =  getEUrl(response);

        int id = getPKByName(url);

        putSite(id, "newName");
        Site m = getSite("newName");
        assertEquals("newName", m.getName());
    }

    protected HttpResponse putSite(int id, String newName) {
        HttpRequest request = HttpRequest.PUT("/sites", new Site(id, newName));
        return client.toBlocking().exchange(request);
    }

    protected HttpResponse addSite(String name) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(name));
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }

    protected Site getSite(String name) {
        HttpRequest request = HttpRequest.GET("/sites/" + name);
        return client.toBlocking().retrieve(request, Site.class);
    }

    private String getEUrl(HttpResponse response) {
        String val = response.header(HttpHeaders.LOCATION);
        if (val != null) {
            int index = val.indexOf("/sites/");
            if (index != -1) {
                return (val.substring(index + "/sites/".length()));
            }
            return null;
        }
        return null;
    }

    protected int getPKByName(String name){
        return getSite(name).getPrimaryKey();
    }
}
