package main.java.com.projectBackEnd.Entities.Site;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

@Controller("/sites")
public class SiteController {
    final SiteManagerInterface siteManager = SiteManager.getSiteManager();
    SiteController() {}
    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<Site> list() {
        return siteManager.getAllSites();
    }

    @Get("/")
    public String index(){
        return "This is our site index page";
    }

    @Post("/")
    public HttpResponse<Site> add(@Body SiteAddCommand command) {
        Site s = siteManager.addSite(command.getName());
        if(siteManager.getByPrimaryKey(s.getPrimaryKey()) == null){
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(s)
                .headers(headers -> headers.location(location(s.getName())));
    }

    @Get(value = "/id/{id}", produces = MediaType.TEXT_JSON)
    public Site list(int id) {
        return siteManager.getByPrimaryKey(id);
    }

    @Get(value = "/{name}")
    public Site list(String name){return siteManager.getBySiteName(name);}

    @Delete("/{id}")
    public HttpResponse delete(int id) {
        siteManager.delete(id);
        return HttpResponse.noContent();
    }

    @Put("/")
    public HttpResponse update(@Body Site updatedSite) {
        Site site = new Site(updatedSite.getPrimaryKey(), updatedSite.getName());
        siteManager.update(site);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(updatedSite.getName()).getPath());
    }

    protected URI location(String siteName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug);
    }



}
