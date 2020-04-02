package main.java.com.projectBackEnd.Entities.AppInfo;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;

import main.java.com.projectBackEnd.Entities.AppInfo.*;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;


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
    public HttpResponse update(@Header("X-API-Key") String session,@Body AppInfo info) {
        if(!sessionManager.verifySession(session))
            return HttpResponse.unauthorized();
        infoManager.updateInfo(info);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(info.getHospitalName()).getPath());
    }
    /**
     * Create URI with the specified id
     * @return created URI
     */
    protected URI location(String hospitalName) {
        return URI.create("/appinfo/" + hospitalName);
    }


}