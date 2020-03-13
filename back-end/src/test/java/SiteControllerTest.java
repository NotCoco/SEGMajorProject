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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        int id = getSitePKByName(url);

        putSite(id, "newName");
        Site m = getSite("newName");
        assertEquals("newName", m.getName());
    }

    @Test
    public void testAddingRegularPage() {
        addSite("testSiteA");
        HttpResponse response = addPage("testSiteA", "nutrition/slu!#g", 1, "Title", "nutri!tion/information");
        assertNotNull(pageManager.getPageBySiteAndSlug("testSiteA", "nutrition/slu!#g"));

        String id =  getEUrl(response);
        Page testPage = getPage(id);
        assertEquals("Title", testPage.getTitle());
    }

    @Test
    public void testAddingPageWithNulls() {
        
    }

    @Test
    public void testUpdatePageDetails() {

    }

    @Test
    public void testUpdatePageWithNullValues() {

    }

    @Test
    public void testPatchingPageIndex() {
        /*    @Patch("/{name}/page-indices")
    public HttpResponse<Page> patchPage(String name, @Body List<PagePatchCommand> patchCommandList){
        for(PagePatchCommand p : patchCommandList){
            String slug = p.getSlug();
            Page page = pageManager.getPageBySiteAndSlug(name, slug);
            page.setIndex(p.getIndex());
            pageManager.update(page);
        }
        return HttpResponse
                .noContent();
    }*/

    }

//    @Test
//    public void testAddPage(){
//        HttpResponse response = addSite("testSite");
//        Page page1 = new Page(100, "testSite", "sameSlug", 1, "TitleA", "ContentA");
//        HttpRequest request = HttpRequest.POST("/sites/testSite/pages", new Page());
//        response = client.toBlocking().exchange(request);
//        assertEquals(HttpStatus.CREATED, response.getStatus());
//
//        request = HttpRequest.GET("/sites/" + name);
//        response = client.toBlocking().retrieve(request, Site.class);
//    }

    protected HttpResponse putSite(int id, String newName) {
        HttpRequest request = HttpRequest.PUT("/sites", new Site(id, newName));
        return client.toBlocking().exchange(request);
    }

    protected HttpResponse addSite(String name) {
        HttpRequest request = HttpRequest.POST("/sites", new SiteAddCommand(name));
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }
    protected HttpResponse addPage(String siteName, String slug, Integer index, String title, String content) {
        HttpRequest request = HttpRequest.POST(("/sites/"+ siteName +"/pages"), new PageAddCommand(siteName, slug, index, title, content));
        HttpResponse response = client.toBlocking().exchange(request);
        return response;

    }

    protected Site getSite(String name) {
        HttpRequest request = HttpRequest.GET("/sites/" + name);
        return client.toBlocking().retrieve(request, Site.class);
    }
    protected Page getPage(String name) {
        HttpRequest request = HttpRequest.GET("/sites/" + name);
        return client.toBlocking().retrieve(request, Page.class);
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

    protected int getSitePKByName(String name){
        return getSite(name).getPrimaryKey();
    }
}
