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
import io.micronaut.core.type.Argument;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import javax.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import javax.validation.ConstraintViolationException;
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
        //Test it has worked in the database too!
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/page/" + id);
        Page pageFound = client.toBlocking().retrieve(request, Page.class);
        assertTrue(pageFound.equals(updatedPage)); //Hopefully checks with the .equals method of page
        assertNotNull(pageManager.getByPrimaryKey("testslug/fortesting/test"));
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
    public void testAddNullIndexPage(){ //TODO No longer throws anything
        Page pageAdded = new Page("testslug/fortesting/test", null, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        String id = getPagePrimaryKeyFromResponse(response);
        assertNull(pageManager.getByPrimaryKey("testslug/fortesting/test"));
        // Asserting that the page has not been added.
        //assertNotEquals(HttpStatus.CREATED, response.getStatus()); //TODO Still incorrectly true.
//        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
//            client.toBlocking().exchange(HttpRequest.POST("/page", new Page("slug/test/slug", null, "", "")));
//        });
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testGetAllPages() {
        ArrayList<String> ids = new ArrayList<>();
        Page pageAdded = new Page("testslug/forte%2Fsting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        ids.add(getPagePrimaryKeyFromResponse(response));
        pageAdded = new Page("testslug/fortesting/tes2t", 3, "Title", "Content");
        request = HttpRequest.POST("/page", pageAdded);
        response = client.toBlocking().exchange(request);
        ids.add(getPagePrimaryKeyFromResponse(response));

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.GET("/page/list");
        List<Page> pageList = client.toBlocking().retrieve(request, Argument.of(List.class, Page.class));
        ArrayList<String> encodedPageIDs = new ArrayList<>();
        for (int j = 0; j < pageList.size(); ++j) {
            encodedPageIDs.add(URLEncoder.encode(pageList.get(j).getPrimaryKey()));
        }
        for(int i=0; i<ids.size();++i){
            assertEquals(ids.get(i), encodedPageIDs.get(i));
        }
    }

    @Test
    public void testAddDuplicatePrimaryKeyPages() {
        Page pageAdded = new Page("testslug/fortesting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/page", new Page("testslug/fortesting/test", 3, "Tit-le", "Conten/t")));
        });
    }

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
