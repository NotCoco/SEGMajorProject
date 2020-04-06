package main.java.com.projectBackEnd.Image;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;

import io.micronaut.http.MediaType;

import io.micronaut.http.multipart.CompletedFileUpload;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

import javax.validation.constraints.Size;

/**
 * Image Controller is a REST API endpoint.
 * It deals with the image related requests users might need.
 * It provides HTTP requests for each of the queries that carry out the adding, deletion and retrieval of images
 * from the server-side storage directories.
 */
@Controller("/images")
public class ImageController {

	protected final ImageManagerInterface imageManager;
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

	/** Main constructor */
	public ImageController(){imageManager = ImageManager.getImageManager();}

	/**
	 * Add a new image by http POST method
	 * @param session
	 * @param file
	 * @return Http response with relevant information which depends on the result of
	 * inserting new image
	 */
	@Post(value = "/", consumes = MediaType.MULTIPART_FORM_DATA)
	public HttpResponse<String> add(@Header("X-API-Key") String session, @Body CompletedFileUpload file) {

		if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
		try {
			return saveImage(file);
		}
		catch (IOException e){
			e.printStackTrace();
			return HttpResponse.noContent();
		}
	}

	/**
	 * Saves an image by passing its encodings to the imageManager
	 * @param file File to be encoded for saving
	 * @return HTTP response based on success.
	 * @throws Encoding may throw IOExceptions
	 */
	private HttpResponse saveImage(CompletedFileUpload file) throws IOException {

		String[] strings = file.getFilename().split("\\.");
		String extension = strings[strings.length-1];
		byte[] encoded = Base64.getEncoder().encode(file.getBytes());
		String msg = imageManager.saveImage(new String(encoded), extension);

		if (msg == null) return HttpResponse.serverError();
		else return HttpResponse
					.created(msg)
					.headers(headers -> headers.location(location(msg)));

	}

	/**
	 * Delete an image with the image name by http Delete method
	 * @param session
	 * @param imageName
	 * @return Http response with relevant information which depends on the result of
	 * deleting the image
	 */
	@Delete("/{imageName}")
	public HttpResponse delete(@Header("X-API-Key") String session, String imageName) {
		if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
		if(imageManager.deleteImage(imageName)) return HttpResponse.noContent();
		else return HttpResponse.serverError();
	}

	/**
	 * Get an image with the image name by http Get method
	 * @param imageName
	 * @return the image file
	 */
	@Get(value = "/{imageName}", produces = MediaType.MULTIPART_FORM_DATA)
	@Size
	public File get(String imageName) {
		return imageManager.getImage(imageName);
	}

	/**
	 * Create URI with existing image name
	 * @param imageName
	 * @return created URI
	 */
	protected URI location(String imageName) {
		return URI.create("/images/" + imageName);
	}


}
