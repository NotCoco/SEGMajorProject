package test.java;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import main.java.com.projectBackEnd.Entities.OldPage.OldPage;
import main.java.com.projectBackEnd.Entities.OldPage.OldPageManager;
import main.java.com.projectBackEnd.Entities.OldPage.OldPageManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import io.micronaut.core.type.Argument;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import javax.inject.Inject;

import java.net.URLEncoder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class OldPageControllerTest {

    @Inject
    @Client("/")
    HttpClient client;
    static OldPageManagerInterface pageManager;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("test/resources/testhibernate.cfg.xml");
        pageManager = OldPageManager.getOldPageManager();
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
    public void testOldPageNotFoundReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/slug/3524111"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testAddAndGetOldPage() {
        OldPage pageAdded = new OldPage("tests[lug/forte\"]sting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded); //Post takes /page instead of /page/
        HttpResponse response = client.toBlocking().exchange(request);
        String id = getOldPagePrimaryKeyFromResponse(response);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.GET("/page/"+id);

        OldPage testOldPage = client.toBlocking().retrieve(request, OldPage.class);

        assertTrue(pageAdded.equals(testOldPage)); //Hopefully checks with the .equals method of page

    }

    @Test
    public void testAddAndUpdateOldPage(){
        OldPage pageAdded = new OldPage("testslug/fortesting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        String id =  getOldPagePrimaryKeyFromResponse(response);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        OldPage updatedOldPage = new OldPage("testslug/fortesting/test", 6, "updatedTitle", "newContent");
        request = HttpRequest.PUT("/page", updatedOldPage);
        response = client.toBlocking().exchange(request);
        //Test it has worked in the database too!
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/page/" + id);
        OldPage pageFound = client.toBlocking().retrieve(request, OldPage.class);
        assertTrue(pageFound.equals(updatedOldPage)); //Hopefully checks with the .equals method of page
        assertNotNull(pageManager.getByPrimaryKey("testslug/fortesting/test"));
    }

    @Test
    public void testDeleteAndGetOldPage(){
        OldPage pageAdded = new OldPage("testslug/fortesting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        String id = getOldPagePrimaryKeyFromResponse(response);
        // Asserting that we've added a OldPage
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.DELETE("/page/"+id);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/page/"+id));
        });
    }

    @Test
    public void testAddNullIndexOldPage(){ //TODO No longer throws anything
        //OldPage pageAdded = new OldPage("testslug/fortesting/test", null, "Title", "Content");
        //HttpRequest request = HttpRequest.POST("/page", pageAdded);
        //HttpResponse response = client.toBlocking().exchange(request);
        //String id = getOldPagePrimaryKeyFromResponse(response);
        //assertNull(pageManager.getByPrimaryKey("testslug/fortesting/test"));
        // Asserting that the page has not been added.
        //assertNotEquals(HttpStatus.CREATED, response.getStatus()); //TODO Still incorrectly true.
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/page", new OldPage("slug/test/slug", null, "", "")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testGetAllOldPages() {
        ArrayList<String> ids = new ArrayList<>();
        OldPage pageAdded = new OldPage("testslug/forte%2Fsting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        ids.add(getOldPagePrimaryKeyFromResponse(response));
        pageAdded = new OldPage("testslug/fortesting/tes2t", 3, "Title", "Content");
        request = HttpRequest.POST("/page", pageAdded);
        response = client.toBlocking().exchange(request);
        ids.add(getOldPagePrimaryKeyFromResponse(response));

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.GET("/page/list");
        List<OldPage> pageList = client.toBlocking().retrieve(request, Argument.of(List.class, OldPage.class));
        ArrayList<String> encodedOldPageIDs = new ArrayList<>();
        for (int j = 0; j < pageList.size(); ++j) {
            encodedOldPageIDs.add(URLEncoder.encode(pageList.get(j).getPrimaryKey()));
        }
        for(int i=0; i<ids.size();++i){
            assertEquals(ids.get(i), encodedOldPageIDs.get(i));
        }
    }

    @Test
    public void testAddDuplicatePrimaryKeyOldPages() {
        OldPage pageAdded = new OldPage("testslug/fortesting/test", 3, "Title", "Content");
        HttpRequest request = HttpRequest.POST("/page", pageAdded);
        HttpResponse response = client.toBlocking().exchange(request);
        assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/page", new OldPage("testslug/fortesting/test", 3, "Tit-le", "Conten/t")));
        });
    }

    //OLD getEId implementation as I wasn't sure where to put the URL Encoder :S
    private String getOldPagePrimaryKeyFromResponse(HttpResponse response) {
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
