package main.java.com.projectBackEnd.Image;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.net.URI;

@Controller("/images")
public class ImageController {

	protected final ImageManager imageManager;

	public ImageController(){
		imageManager = new ImageManager();}
	@Post("/")
	public HttpResponse<String> add(@Body String imageBytes) {
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
	public HttpResponse delete(String imageName) {
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