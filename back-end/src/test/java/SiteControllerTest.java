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
 * The purpose of this class is to test the REST endpoints associated with the site entity through the site controller
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
     * Sets the config resource location and the site manager. Also generates the token attribute
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
     * Closes the session factory and deletes the testing user
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
     * Ensure that there are no pre-existing site or page entities in the database before each test via the 'deleteAll()' method
     */
    @BeforeEach
    public void setUp() {
        siteManager.deleteAll();
        //Automatically deletes all pages too due to cascade, but:
        pageManager.deleteAll();
    }
    /**
     * Attempts to retrieve a site that does not exist in the database via the GET request,
     * expects an Http error to be thrown
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
     * Tests that the endpoint is able to update and existing site with legal information, expects success
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
     * Tests that the endpoint is able to add a site with legal information, expects success
     */
    @Test
    public void testAddLegalSite(){
        HttpResponse response= addSite("testSlug", "legalSite");
        assertEquals(HttpStatus.CREATED, response.getStatus());
    }

    /**
     * Attempts to add a site with an empty name, expects an HTTP error to be thrown
     */
    @Test
    public void testAddEmptyNameSite(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/sites", new SiteAddCommand("slug", "")).header("X-API-Key",token));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }
    /**
     * Tests that the endpoint is able to add a site with legal information and also retrieve it, expects success
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
     * Attempts to delete an existing site and then retrieve it, expects an HTTP error to be thrown
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
     * Tests that the endpoint is able to add and update a site with legal information, expects success
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
     * Attempts to update an existing site with an empty name, expects an HTTP error to be thrown
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
     * Quality of life method for updating a site via the REST api
     * @param id the id
     * @param newSlug the new slug value
     * @param newName the new name
     * @return The HTTP response produced by the operation
     */
    protected HttpResponse putSite(int id, String newSlug, String newName) {
        HttpRequest request = HttpRequest.PUT("/sites", new SiteUpdateCommand(id, newSlug, newName)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }
    /**
     * Quality of life method for adding a site via the REST api
     * @param slug the slug
     * @param name the name
     * @return The HTTP response produced by the operation
     */
    protected HttpResponse addSite(String slug, String name) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(slug, name)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }
    /**
     * Quality of life method for retrieving a site via the REST api
     * @param slug the slug
     * @return The site retrieved
     */
    protected Site getSite(String slug) {
        URI loc = location(slug);
        HttpRequest request = HttpRequest.GET(loc);
        return client.toBlocking().retrieve(request, Site.class);
    }

    /**
     * Method for producing the url of a site from it's associated response
     * @param response the http response
     * @return The url produced
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
     * Method for generating a urlencoded url for a site
     * @param siteName the site's slug
     * @return The generated URI
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
     * Quality of life method for retrieving a site's ID by it's slug
     * @param slug the site's slug
     * @return the site's ID
     */
    protected int getSitePKBySlug(String slug){
        return getSite(slug).getPrimaryKey();
    }
}
