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
* class to unit test interactions between rest calls and system with respect to page functionality
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
    public void testNonExistingPageReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("nothing"));
        });
        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }
	/**
	* test if patching page index behaves correctly
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
	* test if patching with invalid token returns unauthroized http response
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
	*test if adding a page with correct fields behaves correctly
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
	*	test if adding page with null fields returns nullpointer exception
	*/
    @Test
    public void testAddingPageWithNulls() {
        assertThrows(NullPointerException.class, () -> {
            HttpResponse response = addPage(null, null, null, null, null,token);
        });
    }
	/**
	*	test if adding a correct page to a null site returns http error
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
	*	test if adding page with null index returns error
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
        }); //Shouldn't be allowed!
    }
	/**
	*	test if adding page with wrong session token returns http unauthorized
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
	*	test if creating page with same key returns error
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
	* test if deleting a non existent page returns an exception
	*/
    @Test
    public void testDeleteNonExistentPage() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE("nothing").header("X-API-Key",token));
        });
    }
	/**
	*	test if deleting  existend page, deletes it
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
	*	test if trying to delete a page without a correct session token returns unauthorized
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
	*	test if updating page to a duplicate key returns an error
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
	*	test if udpating a page to a new tittle behaves correctly
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
	*	test if udpating page's site to valid behaves correctly
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
	* test if updating pages slug to valid behaves correctly
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
	* test if updateing page's field to invalid throws an error
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
	*	test if updating page without correct session token returns http unauthorized
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
	*	creates a put(update) rest request
	*	@returns Http response to the request
	*/
    protected HttpResponse putPage(int id, String siteName, String slug, int index, String title, String content,String token) {
        URI pLoc = location(siteName);
        HttpRequest request = HttpRequest.PUT(pLoc+"/pages", new PageUpdateCommand(id, siteName, slug, index, title, content)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }
	/**
	*	creates a post(add) rest request
	*	@returns Http response to the request
	*/
    protected HttpResponse addPage(String siteSlug, String slug, Integer index, String title, String content,String token) {
        URI sLoc = location(siteSlug);
        HttpRequest request = HttpRequest.POST((sLoc +"/pages"), new PageAddCommand(siteSlug, slug, index, title, content)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }
	/**
	*	gets a page by site slug and page name
	* @returns correct page
	*/
    protected Page getPage(String siteSlug, String pageName) {
        URI loc = pageLocation(siteSlug, pageName);

        HttpRequest request = HttpRequest.GET(loc);
        return client.toBlocking().retrieve(request, Page.class);
    }
	/**
	*	gets uri String by site slug and page name
	*	@returns URI page
	*/
    protected URI pageLocation(String siteSlug, String pageName) {
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
	* creates a post request (adds site)
	* @returns http response to the request
	*/
    protected HttpResponse addSite(String slug, String name,String token) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(slug,name)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
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


}

