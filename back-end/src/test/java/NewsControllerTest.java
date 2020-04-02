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

import javax.inject.Inject;

import main.java.com.projectBackEnd.Entities.News.Hibernate.News;
import main.java.com.projectBackEnd.Entities.News.Hibernate.NewsManager;
import main.java.com.projectBackEnd.Entities.News.Hibernate.NewsManagerInterface;
import main.java.com.projectBackEnd.Entities.News.Micronaut.NewsAddCommand;
import main.java.com.projectBackEnd.Entities.News.Micronaut.NewsUpdateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;


import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.HibernateUtility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
* class to unit test interactions between rest calls and the software with regards to news functionality
*/
@MicronautTest
public class NewsControllerTest {


    @Inject
    @Client("/")
    HttpClient client;

    static NewsManagerInterface newsManager;
    private static String token;

	/**
	*	run before class, aquire newManager object, set testing db and create and login a user whose credentials are used for testing
	*/
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        newsManager = NewsManager.getNewsManager();
        try{
        	UserManager.getUserManager().addUser("test@test.com" , "123","name");
        	token = UserManager.getUserManager().verifyUser("test@test.com" , "123");
        }
        catch(Exception e){
        	fail();
        }  
    }
	/**
	*	delete the user and shutdown the factory
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
	*	delete all news objects
	*/
    @BeforeEach
    public void setUp() {
        newsManager.deleteAll();
    }
	/**
	*	add news, check if its added and than test if delete returns a proper http response
	*/
   @Test
    public void testDeleteNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        HttpRequest request = HttpRequest.DELETE("/news/"+slug).header("X-API-Key",token);
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
	/**
	*	add news, check if its added and than test if delete returns a proper http response and check if get that news returns error
	*/
    @Test
    public void testDeleteAndGetNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "TestSlug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();

        HttpRequest request = HttpRequest.DELETE("/news/"+"TestSlug").header("X-API-Key",token);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/news/"+"TestSlug"));
        });
    }
	/**
	*	check if delete news that does not exist returns error
	*/
	@Test
	public void testDeleteIncorrect(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/news/"+"TestSlug").header("X-API-Key",token));
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
        HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/news/").header("X-API-Key",token));
        });
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, thrown1.getStatus());

	}

	/**
	*	check if delete with wrong session token returns unauthorized response
	*/
	@Test
	public void testDeleteUnauthorized(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        String slug =  getEUrl(response);
        int id = newsManager.getNewsBySlug(slug).getPrimaryKey();
		
		 HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE("/news/"+slug).header("X-API-Key",""));
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());


		 HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.DELETE("/news/"+slug).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}
	/**
	* check if adding news and then geting all news behaves correctly
	*/
    @Test
    public void testAddAndGetNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Test description", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        List<News> newsList = getAllNews();
        assertEquals("Test description",newsList.get(0).getDescription());
    }
	/**
	*	check if adding news, creates new news
	*/
    @Test
    public void testAddNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        assertEquals("slug", getEUrl(response));
        News testNews = newsManager.getNewsBySlug("slug");
        assertNotNull(testNews);
        assertEquals("Corona virus pandemics", testNews.getTitle());
    }
	/**
	*	check if adding a news with empty or null field throws an error
	*/
	@Test
	public void testAddIncorrect(){
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",true, "COVID-19 originated from Wuhan, China", "",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown1.getStatus());
 		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",true, "", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown2.getStatus());
 		HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "",true, "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown3.getStatus());
		HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
        	addNews(new Date(34189213L) , true, "", "Corona virus pandemics",true, "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown4.getStatus());
		HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",true, "COVID-19 originated from Wuhan, China", null,token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown5.getStatus());
 		HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",true, null, "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown6.getStatus());
 		HttpClientResponseException thrown7 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", null,true, "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown7.getStatus());
		HttpClientResponseException thrown8 = assertThrows(HttpClientResponseException.class, () -> {
        	addNews(new Date(34189213L) , true, null, "Corona virus pandemics",true, "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown8.getStatus());
	}
	/**
	* check if adding news and than updating it, updates the news
	*/
    @Test
    public void testAddAndUpdateNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        String slug =  getEUrl(response);
        int id = newsManager.getNewsBySlug(slug).getPrimaryKey();
        response = putNews(id, new Date(34189213L) , true, "New description", "New title", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        News news = newsManager.getNewsBySlug(slug);
        assertEquals("New description", news.getDescription());
        assertEquals("New title", news.getTitle());
    }
	/**
	* check if adding news while using wrong token returns unauthorized exception
	*/
	@Test
	public void testAddUnauthorized(){
		 HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug","");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
		 HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug","SOmeVeryCo2rr45ECt231TokEN1");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}
	/**
	* checking if update with correct fields behaves correctly
	*/
    @Test
    public void testUpdateLegalNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();
        response = putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, "NewContent", "NewSlug",token);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
	/**
	* check if update with empty or null fields returns an error
	*/
	@Test
	public void testUpdateIncorrect(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, "NewContent", "",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown1.getStatus());
 		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",true, "", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown2.getStatus());
 		HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "",true, "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown3.getStatus());
		HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "", "NewTitle",true, "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown4.getStatus());
		HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, "NewContent", null,token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown5.getStatus());
 		HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, null, "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown6.getStatus());
 		HttpClientResponseException thrown7 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", null,true, "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown7.getStatus());
		HttpClientResponseException thrown8 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, null, "NewTitle",true, "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown8.getStatus());
	}
	/**
	* test if updating with wrong session token returns unauthorized
	*/
	@Test
	public void testUpdateUnauthorized(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics", true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        String slug =  getEUrl(response);
        int id = newsManager.getNewsBySlug(slug).getPrimaryKey();

		 HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, "NewContent", "NewSlug","");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
		 HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true, "NewContent", "NewSlug","SOmeVeryCo2rr45ECt231TokEN1");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}
	/**
	*	updates a news by making rest call
	*/
    protected HttpResponse putNews(Integer primaryKey, Date date, boolean pinned, String description, String title,
                                   boolean urgent, String content, String slug,String token) {
        HttpRequest request = HttpRequest.PUT("/news", new NewsUpdateCommand(primaryKey, date, pinned, description,
                title, urgent, content,slug)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }
	/**
	* adds a news by making rest call
	*/
    protected HttpResponse addNews(Date date, boolean pinned, String description, String title, boolean urgent,
                                   String content, String slug,String token){
        HttpRequest request = HttpRequest.POST("/news", new NewsAddCommand(date, pinned, description, title,
                urgent, content, slug)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }
	/**
	* returns all news by making rest call
	* @return list of all news
	*/
    protected List<News> getAllNews(){
        HttpRequest request = HttpRequest.GET("/news");
        return client.toBlocking().retrieve(request, Argument.of(List.class, News.class));
    }
	/**
	* get url fron httpresponse
	* @return url string
	*/
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
