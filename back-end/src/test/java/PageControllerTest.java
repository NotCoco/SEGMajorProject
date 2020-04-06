package test.java;


import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;

import main.java.com.projectBackEnd.Entities.Page.Hibernate.Page;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManager;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManagerInterface;
import main.java.com.projectBackEnd.Entities.Page.Micronaut.PageAddCommand;
import main.java.com.projectBackEnd.Entities.Page.Micronaut.PagePatchCommand;
import main.java.com.projectBackEnd.Entities.Page.Micronaut.PageUpdateCommand;

import javax.inject.Inject;

import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManagerInterface;
import main.java.com.projectBackEnd.Entities.Site.Micronaut.SiteAddCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;

import main.java.com.projectBackEnd.HibernateUtility;
/**
 * The purpose of this class is to test the REST endpoints associated with the page entity through the page controller
 */
@MicronautTest
public class PageControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    static SiteManagerInterface siteManager;
    static PageManagerInterface pageManager;
    private static String token;
    /**
     * Sets the config resource location and the page manager. Also generates the token attribute
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
     * Attempts to retrieve a page that does not exist in the database via the GET request
     */
    @Test
    public void testNonExistingPageReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("nothing"));
        });
        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }
    /**
     * Tests that the endpoint is able to patch the index of 5 existing site
     */
    @Test
    public void testPatchingPageIndex() {
        addSite("testSiteA", "name1",token);
        addPage("testSiteA", "nutrition/slu!#g", 9, "Title", "nutri!tion/information",token);
        addPage("testSiteA", "anotherPage", 12, "Title", "nutri!tion/information",token);
        addPage("testSiteA", "coolPage", 20, "Title", "nutri!tion/information",token);
        addPage("testSiteA", "Paaage", 13, "Title", "nutri!tion/information",token);
        //public PagePatchCommand(int id, String slug, int index) {
        List<Page> allPagesWithID = pageManager.getAllPages();
        List<PagePatchCommand> input = new ArrayList<>();

        for(int i = 0; i < allPagesWithID.size(); ++i) {
            Page currentPage = allPagesWithID.get(i);
            input.add(new PagePatchCommand(currentPage.getPrimaryKey(), currentPage.getSlug(), i));
        }
        HttpRequest request = HttpRequest.PATCH("/sites/"+ "testSiteA" +"/page-indices", input).header("X-API-Key",token);
        client.toBlocking().exchange(request);
        allPagesWithID = pageManager.getAllPages();
        for(int i = 0; i < allPagesWithID.size(); ++i) assertEquals(i, allPagesWithID.get(i).getIndex());

    }
	/**
	* Test if patching with invalid token returns unauthorized HTTP response
	*/
	@Test
	public void testPatchingUnauthorized(){
        addSite("testSiteA", "name1",token);
        addPage("testSiteA", "nutrition/slu!#g", 9, "Title", "nutri!tion/information",token);
        addPage("testSiteA", "anotherPage", 12, "Title", "nutri!tion/information",token);
        addPage("testSiteA", "coolPage", 20, "Title", "nutri!tion/information",token);
        addPage("testSiteA", "Paaage", 13, "Title", "nutri!tion/information",token);
        //public PagePatchCommand(int id, String slug, int index) {
        List<Page> allPagesWithID = pageManager.getAllPages();
        List<PagePatchCommand> input = new ArrayList<>();

        for(int i = 0; i < allPagesWithID.size(); ++i) {
            Page currentPage = allPagesWithID.get(i);
            input.add(new PagePatchCommand(currentPage.getPrimaryKey(), currentPage.getSlug(), i));
        }

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.PATCH("/sites/"+ "testSiteA" +"/page-indices", input).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown.getStatus());
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.PATCH("/sites/"+ "testSiteA" +"/page-indices", input).header("X-API-Key",""));
        });
		assertEquals(HttpStatus.UNAUTHORIZED,thrown1.getStatus());
	}

    /**
     * Tests that the endpoint is able to add a legal page
     */
    @Test
    public void testAddingRegularPage() {
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        assertNotNull(pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g"));

        //String id =  getEUrl(response);
        Page testPage = getPage("testSiteA","nutrition/slu!#g");
        assertEquals("Title", testPage.getTitle());
    }
    /**
     * Attempts to create a page with null information
     */
    @Test
    public void testAddingPageWithNulls() {
        assertThrows(NullPointerException.class, () -> {
            HttpResponse response = addPage(null, null, null, null, null,token);
        });
    }
    /**
     * Attempts to create a page with a null site attribute
     */
    @Test
    public void testAddingNullSitePage() {
        addSite("TestSite", "name1",token);
        HttpRequest request = HttpRequest.POST(("/sites/"+ "TestSite" +"/pages"), new PageAddCommand(null, "slug", 3, "", ""));
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(request);
        });

    }

    /**
     * Attempts to create a page with a null index attribute
     */
    @Test
    public void testAddingNullIndexPage() {
        addSite("testSiteA", "name1",token);
        assertThrows(NullPointerException.class, () -> {
            HttpRequest.POST(("/sites/"+ "testSiteA" +"/pages"), new PageAddCommand("testSiteA", "slug", null, "", ""));
        });
        HttpRequest request = HttpRequest.POST(("/sites/"+ "testSiteA" +"/pages"), new Page("testSiteA", "slug", null, "", ""));
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(request);
        });
    }

	/**
	*	Test if adding a page with an invalid session token returns HTTP unauthorized
	*/
	@Test
	public void testAddUnauthorized(){
        addSite("testSiteA", "name1",token);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            addPage( "testSiteA", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information","");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

 		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            addPage( "testSiteA", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information","SOmeVeryCo2rr45ECt231TokEN1");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());

	}

    /**
     * Attempts to create a page with a pre existing page's slug value - shouldn't be allowed
     */
    @Test
    public void testCreateSameKeys() {
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
        	addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        });
    }

    /**
     * Attempts to delete a page that does not exist in the database
     */
    @Test
    public void testDeleteNonExistentPage() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE("nothing").header("X-API-Key",token));
        });
    }

    /**
     * Tests that the endpoint is able to delete a pre-existing legal page
     */
    @Test
    public void testDeletePage() {
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        URI pLoc = pageLocation("testSiteA", "nutrition/slu!#g");
        HttpRequest request = HttpRequest.DELETE(pLoc.toString()).header("X-API-Key",token);
        client.toBlocking().exchange(request);
        assertNull(pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g"));
    }

	/**
	*	Test if trying to delete a page without a valid session token returns unauthorized
	*/
	@Test
	public void testDeleteUnauthorized(){

        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        URI pLoc = pageLocation("testSiteA", "nutrition/slu!#g");

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE(pLoc.toString()).header("X-API-Key",""));
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

        HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE(pLoc.toString()).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });

		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());

	}

    /**
     * Attempts to update a page's slug value to the slug of a pre existing page, expects and HTTP exception to be thrown
     */
    @Test
    public void updateToDuplicateKeysPage() {
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        response = addPage("testSiteA", "sameKey", 1, "Title", "nutri!tion/information",token);
        int idOfMadePage = pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getPrimaryKey();
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            putPage(idOfMadePage, "notvalid", "sameKey", 1, "newTitle", "nutri!tion/information",token);
        });
        assertNotNull(pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g"));
    }

    /**
     * Tests that the endpoint is able to update an existing legal page's title to a legal value
     */
    @Test
    public void testUpdatePageTitle() {
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        assertNotNull(pageManager.getAllPages().get(0));
        int idOfMadePage = pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getPrimaryKey();
        //protected HttpResponse putPage(int id, String siteName, String slug, int index, String title, String content) {
        response = putPage(idOfMadePage, "testSiteA", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information",token);
        assertEquals("newTitle",pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getTitle());
        Page testPage = getPage("testSiteA", "nutrition/slu!#g");
        assertEquals("newTitle", testPage.getTitle());
    }

    /**
     * Tests that the endpoint is able to update the slug value of an existing page to a different legal slug
     */
    @Test
    public void testUpdatePageSiteToValid() {
        addSite("testSiteA", "name!",token);
        addSite("testSiteB", "name!",token);
        addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        //gets id of above page
        int idOfMadePage = pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getPrimaryKey();

        putPage(idOfMadePage, "testSiteA", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information",token);

        Page testPage = getPage("testSiteA", "nutrition/slu!#g");
        assertEquals("newTitle", testPage.getTitle());
    }

    /**
     * Tests that the endpoint is able to update the slug value of an existing page to a different legal slug
     */
    @Test
    public void testUpdatePageSlugToValid() {
        addSite("testSiteA", "name1",token);
        addSite("testSiteB", "name1",token);
        addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        //gets id of above page
        int idOfMadePage = pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getPrimaryKey();

        putPage(idOfMadePage, "testSiteA", "nutrition/sl123u!#g", 1, "newTitle", "nutri!tion/information",token);

        Page testPage = getPage("testSiteA", "nutrition/sl123u!#g");
        assertEquals("newTitle", testPage.getTitle());
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            getPage("testSiteA", "nutrition/slu!#g");
        });
        assertNull(pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g"));
    }

    /**
     * Attempts to update an existing page's site to an invalid value, expects an HTTP exception to be thrown
     */
    @Test
    public void testUpdatePageToInvalid() {
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        int idOfMadePage = pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getPrimaryKey();
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            putPage(idOfMadePage, "notvalid", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information",token);
        });
        assertNotNull(pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g"));
    }

	/**
	*	Test if updating page without a correct session token returns HTTP unauthorized
	*/
	@Test
	public void testUpdateUnauthorized(){
        addSite("testSiteA", "name1",token);
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information",token);
        int idOfMadePage = pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g").getPrimaryKey();

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            putPage(idOfMadePage, "testSiteA", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information","");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

 		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            putPage(idOfMadePage, "testSiteA", "nutrition/slu!#g", 1, "newTitle", "nutri!tion/information","SOmeVeryCo2rr45ECt231TokEN1");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());

	}

    /**
     * Quality of life method for updating a page via the REST API
     * @param id the ID/Primary Key of the page to be updated
     * @param siteName The updated site name
     * @param slug The updated page slug
     * @param index The updated index
     * @param title The updated title
     * @param content The updated content
     * @return The HTTP response produced by the operation
     */
    private HttpResponse putPage(int id, String siteName, String slug, int index, String title, String content,String token) {
        URI pLoc = location(siteName);
        HttpRequest request = HttpRequest.PUT(pLoc+"/pages", new PageUpdateCommand(id, siteName, slug, index, title, content)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }

    /**
     * Quality of life method for adding a page via the REST API
     * @param siteSlug The slug of the site this page belongs to
     * @param slug The new page's slug
     * @param index The new page's index
     * @param title The new page's title
     * @param content The new page's content
     * @return The HTTP response produced by the operation
     */
    private HttpResponse addPage(String siteSlug, String slug, Integer index, String title, String content,String token) {
        URI sLoc = location(siteSlug);
        HttpRequest request = HttpRequest.POST((sLoc +"/pages"), new PageAddCommand(siteSlug, slug, index, title, content)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }

    /**
     * Quality of life method for retrieving a page via the REST API
     * @param siteSlug The site the page belongs to
     * @param pageName The page name
     * @return The page item retrieved
     */
    private Page getPage(String siteSlug, String pageName) {
        URI loc = pageLocation(siteSlug, pageName);

        HttpRequest request = HttpRequest.GET(loc);
        return client.toBlocking().retrieve(request, Page.class);
    }

    /**
     * Method for producing a urlencoded url for a page
     * @param siteSlug The site the page belongs to
     * @param pageName The page's slug
     * @return The URI produced
     */
    private URI pageLocation(String siteSlug, String pageName) {
        String encodedSlug = null;
        String encodedPage = null;
        try {
            encodedSlug = URLEncoder.encode(siteSlug, java.nio.charset.StandardCharsets.UTF_8.toString());
            encodedPage = URLEncoder.encode(pageName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug + "/pages/" + encodedPage);
    }

    //Adding and locating Site methods
    /**
     * Quality of life method for adding a site via the REST API
     * @param slug The page slug
     * @param name The page name
     * @return The HTTP response produced by the operation
     */
    private HttpResponse addSite(String slug, String name,String token) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(slug,name)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }
    /**
     * Method for producing a URI encoded URI for a site
     * @param siteName the site name to be encoded
     * @return Encoded URI of the site name
     */
    private URI location(String siteName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug);
    }


}

