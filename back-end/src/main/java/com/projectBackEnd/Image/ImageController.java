package main.java.com.projectBackEnd.Image;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/images")
public class ImageController {

	protected final ImageHandler imageHandler;

	public ImageController(){imageHandler = new ImageHandler();}

	public ImageController(String dir){imageHandler = new ImageHandler(dir);}
	@Post("/")
	public HttpResponse<String> add(@Body String imageBytes) {
		String msg = imageHandler.saveImage(imageBytes);
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
		if(imageHandler.deleteImage(imageName)){
			return HttpResponse.noContent();
		}
		else {
			return HttpResponse.serverError();
		}
	}

	public void deleteAll(){
		imageHandler.deleteAll();
	}

	protected URI location(String imageName) {
		return URI.create("/images/" + imageName);
	}


}
