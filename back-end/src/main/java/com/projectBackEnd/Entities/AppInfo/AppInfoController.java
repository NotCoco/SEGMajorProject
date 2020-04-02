package main.java.com.projectBackEnd.Entities.AppInfo;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.AppInfo.*;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

@Controller("/appinfo")
public class AppInfoController {

    protected final AppInfoManagerInterface infoManager = AppInfoManager.getInfoManager();
    protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
    public AppInfoController() {}

    @Get("/")
    public AppInfo getInfo(){
        return infoManager.getInfo();
    }

    @Put("/")
    public HttpResponse update(@Header("X-API-Key") String session, @Body AppInfo info) {
        if(!sessionManager.verifySession(session))
            return HttpResponse.unauthorized();
        infoManager.updateInfo(info);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(info.getHospitalName()).getPath());
    }

    protected URI location(String hospitalName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(hospitalName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/appinfo/" + encodedSlug);
    }


}