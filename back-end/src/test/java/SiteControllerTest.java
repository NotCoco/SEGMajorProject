package test.java;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;

import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManager;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManagerInterface;

import javax.inject.Inject;

import main.java.com.projectBackEnd.Entities.Site.Hibernate.Site;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManagerInterface;
import main.java.com.projectBackEnd.Entities.Site.Micronaut.SiteAddCommand;
import main.java.com.projectBackEnd.Entities.Site.Micronaut.SiteUpdateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.HibernateUtility;
/**
* class to unit test interactions between rest calls and system with respect to site functionality
*/
@MicronautTest
public class SiteControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    static SiteManagerInterface siteManager;
    static PageManagerInterface pageManager;
    private static String token;
	/**
	*	run before class, aquire siteManager, pageManager objects, set testing db and create and login a user whose credentials are used for testing
	*/
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        siteManager = SiteManager.getSiteManager();
        pageManager = PageManager.getPageManager();
        try{
        	UserManager.getUserManager().addUser("test@test.com" , "123","name");
        	token = UserManager.getUserManager().verifyUser("test@test.com" , "123");
        }
        catch(Exception e){
        	fail();
        }  
    }
	/**
	* delete the user and close factory
	*/
    @AfterAll
    public static void closeDatabase() {
        try{
        	UserManager.getUserManager().deleteUser("test@test.com" , "123");
        }
        catch(Exception e){
        	fail();
        }    
        HibernateUtility.shutdown();
    }
	/**
	*	delete all site and page objects from db
	*/
    @BeforeEach
    public void setUp() {
        siteManager.deleteAll();
        //Automatically deletes all pages too due to cascade, but:
        pageManager.deleteAll();
    }
	/**
	* check if geting a page that does not request returns 404 error
	*/
    @Test
    public void testNonExistingSiteReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/sites/IpsumLoremSite"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }
	/**
	* tests if updating a correct site to correct fields returns correct response
	*/
    @Test
    public void testPutLegalSite(){
        HttpResponse response= addSite("testSlug", "legalSite");
        String url = getEUrl(response);
        int id =  getSitePKBySlug(url);
        response = putSite(id,"newSlug", "NewName");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
	/**
	*	test if adding a correct site returns correct http response
	*/
    @Test
    public void testAddLegalSite(){
        HttpResponse response= addSite("testSlug", "legalSite");
        assertEquals(HttpStatus.CREATED, response.getStatus());
    }

	/**
	*	test if adding a site witth an empty name returns a http exception
	*/
    @Test
    public void testAddEmptyNameSite(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/sites", new SiteAddCommand("slug", "")).header("X-API-Key",token));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }
	/**
	*	test if add and get site behave correctly on correct input
	*/
    @Test
    public void testAddAndGetSite(){
        HttpResponse response = addSite("testSlug", "testSite");
        String id =  getEUrl(response);

        Site testSite = getSite(id);

        assertEquals("testSite", testSite.getName());
    }
	/**
	*	test if adding site while using incorrect session tokens returns http unauthorized exception
	*/
	@Test
	public void testAddUnauthorized(){
		String correctToken = token;
		token = "";

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			addSite("testSlug", "testSite");
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown.getStatus());
		token = "SOmeVeryCo2rr45ECt231TokEN1";

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			addSite("testSlug", "testSite");
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown1.getStatus());


		token = correctToken;
	}	
	/**
	*	test if deleting an existing site behaves correctly and than getting it returns an http not found exception
	*/
    @Test
    public void testDeleteAndGetSite(){
        HttpResponse response = addSite("testSlug", "testSite");
        String url =  getEUrl(response);
        int id = getSitePKBySlug(url);
        // Asserting that we've added a site
        assertEquals(HttpStatus.CREATED, response.getStatus());

        HttpRequest request = HttpRequest.DELETE("/sites/"+url).header("X-API-Key",token);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/sites/"+url));
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }
	/**
	*	test if deleting while using incorrect session token returns http unauthorized exception
	*/
	@Test
	public void testDeleteUnauthorized(){
        HttpResponse response = addSite("testSlug", "testSite");
        String url =  getEUrl(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
  		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE("/sites/"+url).header("X-API-Key",""));
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown.getStatus());

  		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE("/sites/"+url).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown1.getStatus());
	}
	/**
	*	test if adding and updating site while using correct field values behaves correctly
	*/
    @Test
    public void testAddAndUpdateSite(){
        HttpResponse response = addSite("testSlug", "testSite");
        String url =  getEUrl(response);

        int id = getSitePKBySlug(url);

        putSite(id, "newSlug", "newName");
        Site m = getSite("newSlug");
        assertEquals("newName", m.getName());
    }
	/**
	*	test if updating site to empty name raises a http error
	*/
    @Test
    public void testUpdateToEmptyNameSite(){
        HttpResponse response = addSite("testSlug","testSite");
        String url =  getEUrl(response);
        int id = getSitePKBySlug(url);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.PUT("/sites", new SiteUpdateCommand(id, "slug", "")).header("X-API-Key",token));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }
	/**
	*	test if updating while using incorrect session token returns http unauthorized exception
	*/
	@Test
	public void testUpdateUnauthorized(){
        HttpResponse response = addSite("testSlug","testSite");
        String url =  getEUrl(response);
        int id = getSitePKBySlug(url);
        assertEquals(HttpStatus.CREATED, response.getStatus());


        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.PUT("/sites", new SiteUpdateCommand(id, "slug", "")).header("X-API-Key",""));
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown.getStatus());
        HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.PUT("/sites", new SiteUpdateCommand(id, "slug", "")).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown1.getStatus());
	}
	/**
	* creates a put request for updating a site
	* @returns http response to the request
	*/
    protected HttpResponse putSite(int id, String newSlug, String newName) {
        HttpRequest request = HttpRequest.PUT("/sites", new SiteUpdateCommand(id, newSlug, newName)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }
	/**
	*	creates a post request for adding a site
	* 	@returns a http response to the request
	*/
    protected HttpResponse addSite(String slug, String name) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(slug, name)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }
	/**
	* get site object by the slug
	*/
    protected Site getSite(String slug) {
        URI loc = location(slug);
        HttpRequest request = HttpRequest.GET(loc);
        return client.toBlocking().retrieve(request, Site.class);
    }

	/**
	* get url from http response and
	*/
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



	/**
	* gets uri of site by site name
	* @returns URI of site
	*/
    protected URI location(String siteName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug);
    }
	/**
	* get sites primary key by slug
	* @returns sites primary key
	*/
    protected int getSitePKBySlug(String slug){
        return getSite(slug).getPrimaryKey();
    }
}
