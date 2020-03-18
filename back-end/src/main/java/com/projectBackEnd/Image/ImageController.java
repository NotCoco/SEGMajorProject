package main.java.com.projectBackEnd.Image;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.net.URI;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

@Controller("/images")
public class ImageController {

	protected final ImageManager imageManager;
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
	public ImageController(){
		imageManager = new ImageManager();}
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
