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
 * The purpose of this class is to test the REST endpoints associated with the news entity through the news controller
 */
@MicronautTest
public class NewsControllerTest {


    @Inject
    @Client("/")
    HttpClient client;

    static NewsManagerInterface newsManager;
    private static String token;


    /**
     * Sets the config resource location and the news manager. Also generates the token attribute
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
     * Ensure that there are no pre-existing news entities in the database before each test via the 'deleteAll()' method
     */
    @BeforeEach
    public void setUp() {
        newsManager.deleteAll();
    }


    /**
     * Tests that the endpoint is able to delete an existing news item, expects success
     */
   @Test
    public void testDeleteNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        HttpRequest request = HttpRequest.DELETE("/news/"+slug).header("X-API-Key",token);
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }


    /**
     * Deletes an existing news item and attempts to retrieve it via the GET request, expects an HTTP error to
     * be thrown
     */
    @Test
    public void testDeleteAndGetNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "TestSlug",token);
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
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug", token);
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
     * Tests the news request responsible for retrieving all newsas a list, via the GET request,
     * expects success
     */
    @Test
    public void testAddAndGetNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Test description", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        List<News> newsList = getAllNews();
        assertEquals("Test description",newsList.get(0).getDescription());
    }


    /**
     * Tests that the endpoint is able to add a legal news item, expects success
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
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, "COVID-19 originated from Wuhan, China", "",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown1.getStatus());
 		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, "", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown2.getStatus());
 		HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "",true,
                    "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown3.getStatus());
		HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
        	addNews(new Date(34189213L) , true, "", "Corona virus pandemics",true,
                    "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown4.getStatus());
		HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, "COVID-19 originated from Wuhan, China", null,token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown5.getStatus());
 		HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, null, "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown6.getStatus());
 		HttpClientResponseException thrown7 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", null,true,
                    "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown7.getStatus());
		HttpClientResponseException thrown8 = assertThrows(HttpClientResponseException.class, () -> {
        	addNews(new Date(34189213L) , true, null, "Corona virus pandemics",true,
                    "COVID-19 originated from Wuhan, China", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown8.getStatus());
	}


    /**
     * Tests that the endpoint is able to add and update an existing news item with legal information, expects success
     */
    @Test
    public void testAddAndUpdateNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        String slug =  getEUrl(response);
        int id = newsManager.getNewsBySlug(slug).getPrimaryKey();
        response = putNews(id, new Date(34189213L) , true, "New description", "New title",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
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
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, "COVID-19 originated from Wuhan, China", "slug","");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
		 HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, "COVID-19 originated from Wuhan, China", "slug","SOmeVeryCo2rr45ECt231TokEN1");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}


    /**
     * Tests that the endpoint is able to update a news item with legal information, expects success
     */
    @Test
    public void testUpdateLegalNews(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();
        response = putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",
                true, "NewContent", "NewSlug",token);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }


	/**
	* check if update with empty or null fields returns an error
	*/
	@Test
	public void testUpdateIncorrect(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals("slug", getEUrl(response));
        String slug = getEUrl(response);
        News news = newsManager.getNewsBySlug(slug);
        int id = news.getPrimaryKey();

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",
                    true, "NewContent", "",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown1.getStatus());
 		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                    true, "", "slug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown2.getStatus());
 		HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "",true,
                    "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown3.getStatus());
		HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "", "NewTitle",true,
                    "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown4.getStatus());
		HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true,
                    "NewContent", null,token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown5.getStatus());
 		HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true,
                    null, "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown6.getStatus());
 		HttpClientResponseException thrown7 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", null,true,
                    "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown7.getStatus());
		HttpClientResponseException thrown8 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, null, "NewTitle",true,
                    "NewContent", "NewSlug",token);
        });
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown8.getStatus());
	}


	/**
	* test if updating with wrong session token returns unauthorized
	*/
	@Test
	public void testUpdateUnauthorized(){
        HttpResponse response = addNews(new Date(34189213L) , true, "Health Alert", "Corona virus pandemics",
                true, "COVID-19 originated from Wuhan, China", "slug",token);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        String slug =  getEUrl(response);
        int id = newsManager.getNewsBySlug(slug).getPrimaryKey();

		 HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true,
                    "NewContent", "NewSlug","");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
		 HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			putNews(id, new Date(324189213L), true, "NewDescription", "NewTitle",true,
                    "NewContent", "NewSlug","SOmeVeryCo2rr45ECt231TokEN1");
        });
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}


    /**
     * Quality of life method for updating a news item via the REST api
     * @param primaryKey the primary key
     * @param date the date
     * @param pinned whether its pinned or not
     * @param description the description
     * @param title the title
     * @param urgent if its urgent or not
     * @param content the content
     * @param slug the slug
     * @return The HTTP response produced by the operation
     */
    protected HttpResponse putNews(Integer primaryKey, Date date, boolean pinned, String description, String title,
                                   boolean urgent, String content, String slug,String token) {
        HttpRequest request = HttpRequest.PUT("/news", new NewsUpdateCommand(primaryKey, date, pinned, description,
                title, urgent, content,slug)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }


    /**
     * Quality of life method for adding a news item via the REST api
     * @param date the date value
     * @param pinned whether its pinned or not
     * @param description the description
     * @param title the title
     * @param urgent the urgent
     * @param content the content
     * @param slug the slug
     * @return The HTTP response produced by the operation
     */
    protected HttpResponse addNews(Date date, boolean pinned, String description, String title, boolean urgent,
                                   String content, String slug,String token){
        HttpRequest request = HttpRequest.POST("/news", new NewsAddCommand(date, pinned, description, title,
                urgent, content, slug)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }


    /**
     * Quality of life method for retrieving all news items via the REST api
     * @return The list of news items
     */
    protected List<News> getAllNews(){
        HttpRequest request = HttpRequest.GET("/news");
        return client.toBlocking().retrieve(request, Argument.of(List.class, News.class));
    }


    /**
     * Method for returning the url of an object involved in a HTTP response
     * @param response the response
     * @return the url
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
