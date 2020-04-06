package main.java.com.projectBackEnd.Media;

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
 * Media Controller is a REST API endpoint.
 * It deals with the media related requests users might need.
 * It provides HTTP requests for each of the queries that carry out the adding, deletion and retrieval of medias,
 * videos, and other medias from the server-side storage directories.
 */
@Controller("/medias")
public class MediaController {

	protected final MediaManagerInterface mediaManager;
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

	/** Main constructor */
	public MediaController(){mediaManager = MediaManager.getMediaManager();}

	/**
	 * Add a new media via an HTTP Post method on the current session
	 * @param session	current session
	 * @param file		media to add to the server
	 * @return HTTP response with relevant information resulting form the insertion of the new media
	 */
	@Post(value = "/", consumes = MediaType.MULTIPART_FORM_DATA)
	public HttpResponse<String> add(@Header("X-API-Key") String session, @Body CompletedFileUpload file) {

		if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
		try {
			return saveMedia(file);
		}
		catch (IOException e){
			e.printStackTrace();
			return HttpResponse.noContent();
		}
	}

	/**
	 * Saves a media by passing its encodings to the mediaManager
	 * @param file	Media to be encoded for saving
	 * @return HTTP response based on success.
	 * @throws IOException
	 */
	private HttpResponse saveMedia(CompletedFileUpload file) throws IOException {

		String[] strings = file.getFilename().split("\\.");
		String extension = strings[strings.length-1];
		byte[] encoded = Base64.getEncoder().encode(file.getBytes());
		String msg = mediaManager.saveMedia(new String(encoded), extension);

		if (msg == null) return HttpResponse.serverError();
		else return HttpResponse
					.created(msg)
					.headers(headers -> headers.location(location(msg)));

	}

	/**
	 * Remove the media corresponding to the given name via an HTTP Delete method on the current session
	 * @param session	current session
	 * @param mediaName	name of the media to remove fom the server
	 * @return HTTP response with relevant information resulting from the deletion of the media
	 **/
	@Delete("/{mediaName}")
	public HttpResponse delete(@Header("X-API-Key") String session, String mediaName) {
		if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
		if(mediaManager.deleteMedia(mediaName)) return HttpResponse.noContent();
		else return HttpResponse.serverError();
	}

	/**
	 * Get the media corresponding to the given name via an HTTP Get method
	 * @param mediaName	name of the media to retrieve
	 * @return the media file
	 */
	@Get(value = "/{mediaName}", produces = MediaType.MULTIPART_FORM_DATA)
	@Size
	public File get(String mediaName) {
		return mediaManager.getMedia(mediaName);
	}

	/**
	 * Create URI with existing media name
	 * @param mediaName	name of the media to locate
	 * @return created URI for that media
	 */
	protected URI location(String mediaName) {
		return URI.create("/medias/" + mediaName);
	}


}
