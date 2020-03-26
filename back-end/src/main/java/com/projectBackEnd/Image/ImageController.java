package main.java.com.projectBackEnd.Image;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.net.URI;

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
	 * @param imageBytes image bytes encoded with Base64
	 * @return Http response with relevant information which depends on the result of
	 * inserting new image
	 */
	@Post("/")
	public HttpResponse<String> add(@Header("X-API-Key") String session,@Body String imageBytes) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		String msg = imageManager.saveImage(imageBytes);
		if(msg.equals("Failed")){
			return HttpResponse.serverError();
		}
		else{
			return HttpResponse
					.created(msg)
					.headers(headers -> headers.location(location(msg)));
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
