package test.java;

import io.micronaut.core.type.Argument;
import io.micronaut.http.*;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;

import main.java.com.projectBackEnd.Image.*;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA_TYPE;
import static org.junit.jupiter.api.Assertions.*;


import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;

import main.java.com.projectBackEnd.HibernateUtility;

import java.io.File;
/**
 * The purpose of this class is to test the REST endpoints associated with the image related executions
 */
@MicronautTest
public class ImageControllerTest {
	private static ImageManager imageManager;
    private static String token;

	private File file;
	private File largeFile;

	/**
	*	Constructor gets a new image manager Singleton
	*/
	public ImageControllerTest(){
		imageManager = ImageManager.getImageManager();
		file = new File(System.getProperty("user.dir")+"/src/test/resources/TestImages/UploadedImage/"+"testImage.jpg");
		largeFile = new File(System.getProperty("user.dir")+"/src/test/resources/TestImages/UploadedImage/"+"17MB.jpg");
	}
	@Inject
	@Client("/")
	HttpClient client;
  
	/**
	 * Set up the user table for sessions and set the target directory of generated images to the specified folder
	 */
	@BeforeAll
	public static void setUpBefore() {
		DirectoryHolder.getDirectoryHolder().setDir(System.getProperty("user.dir")+"/src/test/resources/TestImages/Generated/");
		HibernateUtility.setResource("testhibernate.cfg.xml");
		try{

			UserManager.getUserManager().addUser("test@test.com" , "123","name");
			token = UserManager.getUserManager().verifyUser("test@test.com" , "123");
		}
		catch(Exception e){
			fail();
		}
	}

	/**
	 * Deletes all images in the database before each test
	 */
	@BeforeEach
	public void setUp() {imageManager.deleteAll();}
	/**
	* delete all images, delete the user, close the factory
	*/
	@AfterAll
	public static void deleteCreatedImages() {
		imageManager.deleteAll();
		try{
			UserManager.getUserManager().deleteUser("test@test.com" , "123");
			HibernateUtility.shutdown();
		} catch(Exception e){
			fail();
		}
		DirectoryHolder.getDirectoryHolder().setDefaultDir();
	}


	/**
	 * Tests that legal images can be added to the database via the endpoint, using a POST request
	 */
	@Test
	public void testAddLegalImage(){
		HttpResponse response = addImage(file,token);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		String imageName = getEUrl(response);
		assertTrue(imageManager.getImageUrls().contains(imageManager.getDir()+imageName));
	}

	/**
	 * Test if adding a image without a correct session token returns http unauthorized exception
	 */
	@Test
	public void testAddUnauthorized(){
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			addImage(file,"");
        	});
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());


        	HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			addImage(file,"Ve2R7y5Co3215r8re7CtTok5En13");
        	});
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}
	/**
	 * Add image with wrong directory / null image
	 */
	@Test
	public void testAddIncorrect(){
		File badFile = new File(System.getProperty("user.dir")+"/src/");
		assertThrows(java.lang.IllegalArgumentException.class, () -> {
			addImage(badFile, token);
        	});
	}
	/**
	 * Delete an image
	 */
	@Test
	public void testDeleteImage(){
		HttpResponse response = addImage(file,token);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		String imageName = getEUrl(response);
		HttpRequest request = HttpRequest.DELETE("/images/"+imageName).header("X-API-Key",token);
		HttpResponse response2 = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NO_CONTENT, response2.getStatus());
	}
	/**
	 * Delete wrong image's name
	 */


	@Test
	public void testDeleteIncorrect(){
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/images/27182").header("X-API-Key",token));
        	});
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR , thrown.getStatus());
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/images/").header("X-API-Key",token));
        	});
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED , thrown1.getStatus());
		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/images/name314").header("X-API-Key",token));
        	});
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR , thrown2.getStatus());
	}

	/**
	 * Delete with unionization
	 * test if deleting a image without a correct session token returns http unauthorized excepiton
	 */
	@Test
	public void testDeleteUnauthorized(){
		HttpResponse response = addImage(file, token);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		String imageName = getEUrl(response);

        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/images/"+imageName).header("X-API-Key",""));
        	});
		assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());


        	HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/images/"+imageName).header("X-API-Key","Ve2R7y5Co3215r8re7CtTok5En13"));
        	});
		assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	
	}

	/**
	 * Add and get the same image
	 */
	@Test
	public void testAddAndGetImage(){
		HttpResponse response = addImage(file,token);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		String imageName = getEUrl(response);
		assertTrue(imageManager.getImageUrls().contains(imageManager.getDir()+imageName));
		HttpRequest request = HttpRequest.GET("/images/"+imageName)
				.contentType(MediaType.MULTIPART_FORM_DATA_TYPE);
		File targetImage = client.toBlocking().retrieve(request, Argument.of(File.class));
		assertNotNull(targetImage);
	}

	/**
	 * Add an large image
	 */
	@Test
	public void testAddLargeImage(){
		HttpResponse response = addImage(largeFile,token);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		String imageName = getEUrl(response);
		assertTrue(imageManager.getImageUrls().contains(imageManager.getDir()+imageName));
	}
	/**
	 * Get the url of the image
	 * @param response
	 * @return url
	 */

	private String getEUrl(HttpResponse response) {
		String val = response.header(HttpHeaders.LOCATION);
		if (val != null) {
			int index = val.indexOf("/images/");
			if (index != -1) {
				return (val.substring(index + "/images/".length()));
			}
			return null;
		}
		return null;
	}
	/**
	 * Add image by http POST request
	 * @param file image file
	 * @param token
	 * @return http response
	 */
	protected HttpResponse addImage(File file, String token){
		MultipartBody body = MultipartBody.builder()
				.addPart("file","testImage.jpg",MULTIPART_FORM_DATA_TYPE,file)
				.build();

			HttpRequest request = HttpRequest.POST("/images", body).header("X-API-Key", token)
					.contentType(MediaType.MULTIPART_FORM_DATA_TYPE);
			HttpResponse response = client.toBlocking().exchange(request);
			return response;
	}
}
