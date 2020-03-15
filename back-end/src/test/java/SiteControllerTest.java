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

import main.java.com.projectBackEnd.Entities.Page.*;
import main.java.com.projectBackEnd.Entities.Site.*;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
public class SiteControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    static SiteManagerInterface siteManager;
    static PageManagerInterface pageManager;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        siteManager = SiteManager.getSiteManager();
        pageManager = PageManager.getPageManager();
    }

    @AfterAll
    public static void closeDatabase() {
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
        siteManager.deleteAll();
        //Automatically deletes all pages too due to cascade, but:
        pageManager.deleteAll();
    }

    @Test
    public void testPutLegalSite(){
        HttpResponse response= addSite("legalSite");
        String url = getEUrl(response);
        int id =  getSitePKByName(url);
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
        int id = getSitePKByName(url);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/sites", new Site(id, "")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testAddNullNameSite(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/sites", new SiteAddCommand("")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testDeleteAndGetSite(){
        HttpResponse response = addSite("testSite");
        String url =  getEUrl(response);
        int id = getSitePKByName(url);
        // Asserting that we've added a site
        assertEquals(HttpStatus.CREATED, response.getStatus());

        HttpRequest request = HttpRequest.DELETE("/sites/"+url);
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

        int id = getSitePKByName(url);

        putSite(id, "newName");
        Site m = getSite("newName");
        assertEquals("newName", m.getName());
    }



    protected HttpResponse putSite(int id, String newName) {
        String oldSiteName = siteManager.getByPrimaryKey(id).getName();
        URI sLoc = location(oldSiteName);
        HttpRequest request = HttpRequest.PUT(sLoc, new Site(id, newName));
        return client.toBlocking().exchange(request);
    }

    protected HttpResponse addSite(String name) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(name));
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }

    protected Site getSite(String name) {
        URI loc = location(name);
        HttpRequest request = HttpRequest.GET(loc);
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



    protected URI location(String siteName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug);
    }

    protected int getSitePKByName(String name){
        return getSite(name).getPrimaryKey();
    }
}
