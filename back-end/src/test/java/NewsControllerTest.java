package test.java;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import main.java.com.projectBackEnd.*;

import javax.inject.Inject;


import main.java.com.projectBackEnd.Entities.News.*;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;


import main.java.com.projectBackEnd.Entities.User.UserManager;
//import main.java.com.projectBackEnd.HibernateUtility;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class NewsControllerTest {


    @Inject
    @Client("/")
    HttpClient client;

    static NewsManagerInterface newsManager;
    private static String token;
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        newsManager = NewsManager.getNewsManager();
        try{
        	UserManager.getUserManager().addUser("test@test.com" , "123");
        	token = UserManager.getUserManager().verifyUser("test@test.com" , "123");
        }
        catch(Exception e){
        	fail();
        }  
    }

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

    @BeforeEach
    public void setUp() {
        newsManager.deleteAll();
    }

    @AfterEach
    public void cleanUp(){ newsManager.deleteAll(); }

    @Test
    public void testAddNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug");
        assertEquals(HttpStatus.CREATED, response.getStatus());

        assertEquals("slug", getEUrl(response));
        News testNews = newsManager.getNewsBySlug("slug");
        assertNotNull(testNews);
        assertEquals("Corona virus pandemics", testNews.getTitle());
    }

    @Test
    public void testUpdateLegalNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug");
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();
        response = putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, "NewContent", "NewSlug");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    @Test
    public void testDeleteNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug");
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        HttpRequest request = HttpRequest.DELETE("/news/"+slug);
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    @Test
    public void testAddAndUpdateNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug");
        assertEquals(HttpStatus.CREATED, response.getStatus());

        String slug =  getEUrl(response);
        int id = newsManager.getNewsBySlug(slug).getPrimaryKey();
        response = putNews(id, new Date(34189213L) , true, "New description", "New title", true, "COVID-19 originated from Wuhan, China", "slug");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        News news = newsManager.getNewsBySlug(slug);
        assertEquals("New description", news.getDescription());
        assertEquals("New title", news.getTitle());
    }

    @Test
    public void testAddAndGetNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Test description", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug");
        assertEquals(HttpStatus.CREATED, response.getStatus());
        List<News> newsList = getAllNews();
        assertEquals("Test description",newsList.get(0).getDescription());
    }

    @Test
    public void testDeleteAndGetNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "TestSlug");
        assertEquals(HttpStatus.CREATED, response.getStatus());
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();

        HttpRequest request = HttpRequest.DELETE("/news/"+"TestSlug");
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/news/"+"TestSlug"));
        });
    }


    protected HttpResponse putNews(Integer primaryKey, Date date, boolean pinned, String description, String title,
                                   boolean urgent, String content, String slug) {
        HttpRequest request = HttpRequest.PUT("/news", new NewsUpdateCommand(primaryKey, date, pinned, description,
                title, urgent, content,slug)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }

    protected HttpResponse addNews(Date date, boolean pinned, String description, String title, boolean urgent,
                                   String content, String slug){
        HttpRequest request = HttpRequest.POST("/news", new NewsAddCommand(date, pinned, description, title,
                urgent, content, slug)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }

    protected List<News> getAllNews(){
        HttpRequest request = HttpRequest.GET("/news");
        return client.toBlocking().retrieve(request, Argument.of(List.class, News.class));
    }

    private String getEUrl(HttpResponse response) {
        String val = response.header(HttpHeaders.LOCATION);
        if (val != null) {
            int index = val.indexOf("/news/");
            if (index != -1) {
                return (val.substring(index + "/news/".length()));
            }
            return null;
        }
        return null;
    }

}
