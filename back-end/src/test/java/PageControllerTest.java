package test.java;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import main.java.com.projectBackEnd.Entities.Page.Page;
import main.java.com.projectBackEnd.Entities.Page.PageManager;
import main.java.com.projectBackEnd.Entities.Page.PageManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class PageControllerTest {

    @Inject
    @Client("/")
    HttpClient client;
    static PageManagerInterface pageManager;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        pageManager = PageManager.getPageManager();
    }
    @AfterAll
    public static void closeDatabase() {
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
        pageManager.deleteAll();
    }

    @Test
    public void testPageNotFoundReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/slug/3524111"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testAddAndGetPage() {
        Page pageAdded = new Page("tests[lug/forte\"]sting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded); //Post takes /page instead of /page/

        HttpResponse response = client.toBlocking().exchange(request);
        String id = getPagePrimaryKeyFromResponse(response);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.GET("/page/"+id);

        Page testPage = client.toBlocking().retrieve(request, Page.class);

        assertTrue(pageAdded.equals(testPage)); //Hopefully checks with the .equals method of page

    }
    @Test
    public void testAddAndUpdatePage(){
        Page pageAdded = new Page("testslug/fortesting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        String id =  getPagePrimaryKeyFromResponse(response);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        Page updatedPage = new Page("testslug/fortesting/test", 6, "updatedTitle", "newContent");
        request = HttpRequest.PUT("/page", updatedPage);
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/page/" + id);
        Page pageFound = client.toBlocking().retrieve(request, Page.class);
        assertTrue(pageFound.equals(updatedPage)); //Hopefully checks with the .equals method of page
    }

    @Test
    public void testDeleteAndGetPage(){
        Page pageAdded = new Page("testslug/fortesting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        String id = getPagePrimaryKeyFromResponse(response);
        // Asserting that we've added a Page
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.DELETE("/page/"+id);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/page/"+id));
        });
    }

    @Test
    public void testAddNullIndexPage(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/page", new Page("slug/test/slug", null, "", "")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }
    /*
    //TODO Test me!
    private String getEId(HttpResponse response) {
        String responseHeader = response.header(HttpHeaders.LOCATION);
        if (responseHeader != null) {
            int index = responseHeader.indexOf("/page/");
            if (index != -1) {
                String cutResponseHeader = responseHeader.substring(index + "/page/".length());
                try {
                    return URLEncoder.encode(cutResponseHeader, java.nio.charset.StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    //return URLEncoder.encode(cutResponseHeader);
                    //TODO Remove deprecation
                    return null;
                }
            }
            return null;

        }
        return null;
    }*/
    //OLD getEId implementation as I wasn't sure where to put the URL Encoder :S
        private String getPagePrimaryKeyFromResponse(HttpResponse response) {
        String val = response.header(HttpHeaders.LOCATION);
        if (val != null) {
            int index = val.indexOf("/page/");
            if (index != -1) {
                return (val.substring(index + "/page/".length()));
            }
            return null;
        }
        return null;
        }
}
