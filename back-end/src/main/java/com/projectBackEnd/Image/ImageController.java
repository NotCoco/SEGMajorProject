package main.java.com.projectBackEnd.Image;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import io.micronaut.http.MediaType;

import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.http.multipart.CompletedFileUpload;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

/**
 * Image Controller class is used for the interactions between frontend and backend
 * There are functionalites :
 *    - add an image to file system
 *    - delete an image
 */
@Controller("/images")
public class ImageController {

	protected final ImageManager imageManager;
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
	public ImageController(){imageManager = new ImageManager();}

	/**
	 * Add a new image by http POST method
	 * @param session

	 * @return Http response with relevant information which depends on the result of
	 * inserting new image
	 */
	@Post(value = "/", consumes = MediaType.MULTIPART_FORM_DATA)
	public HttpResponse<String> add(@Header("X-API-Key") String session, @Body CompletedFileUpload file) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		try {
			String extension = file.getFilename().split("\\.")[1];
			byte[] encoded = Base64.getEncoder().encode(file.getBytes());
			String msg = imageManager.saveImage(new String(encoded), extension);
			if(msg.equals("Failed")){
				return HttpResponse.serverError();
			}
			else{
				return HttpResponse
						.created(msg)
						.headers(headers -> headers.location(location(msg)));
			}
		}
		catch(IOException a){
			System.out.println("Error occured");
			return HttpResponse
					.noContent();
		}
	}

	/**
	 * Delete an image with the image name by http Delete method
	 * @param session
	 * @param imageName
	 * @return Http response with relevant information which depends on the result of
	 * deleting the image
	 */
	@Delete("/{imageName}")
	public HttpResponse delete(@Header("X-API-Key") String session,String imageName) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		if(imageManager.deleteImage(imageName)){
			return HttpResponse.noContent();
		}
		else {
			return HttpResponse.serverError();
		}
	}

	public void deleteAll(){
		imageManager.deleteAll();
	}

	protected URI location(String imageName) {
		return URI.create("/images/" + imageName);
	}


}
