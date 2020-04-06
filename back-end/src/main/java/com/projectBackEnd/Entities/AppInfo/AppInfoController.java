package main.java.com.projectBackEnd.Entities.AppInfo;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

/**
 * AppInfoController creating REST API endpoints for the frontend to connect to.
 */
@Controller("/appinfo")
public class AppInfoController {

    protected final AppInfoManagerInterface infoManager = AppInfoManager.getInfoManager();
    protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

    /**
     * Default constructor
     */
    public AppInfoController() {}

    /**
     * Get the stored information out of the singleton manager
     * @return The AppInfo stored in the manager
     */
    @Get("/")
    public AppInfo getInfo(){
        return infoManager.getInfo();
    }

    /**
     * Update the AppInfo saved in the manager via an HTTP Put request
     * @param session   The login session with X-API Key to check permissions
     * @param info      The new information to be saved
     * @return A HTTP Response regarding success of the operation
     */
    @Put("/")
    public HttpResponse update(@Header("X-API-Key") String session, @Body AppInfo info) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        infoManager.updateInfo(info);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(info.getHospitalName()).getPath());
    }

    /**
     * Convert the string hospital name into a URI Location by encoding it.
     * @param hospitalName The name of the hospital
     * @return The encoded hospital name and its access location.
     */
    protected URI location(String hospitalName) {
        String encodedSlug;
        try {
            encodedSlug = URLEncoder.encode(hospitalName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/appinfo/" + encodedSlug);
    }


}