package main.java.com.projectBackEnd.Entities.Site;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Page.*;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

@Controller("/sites")
public class SiteController extends PageController {
    final SiteManagerInterface siteManager = SiteManager.getSiteManager();
    //final PageManagerInterface pageManager = PageManager.getPageManager();

    SiteController() {
        super();
    }
//    @Get(value = "/list", produces = MediaType.TEXT_JSON)
//    public List<Site> list() {
//        return siteManager.getAllSites();
//    }

    @Get("/")
    public List<Site> index(){
        return siteManager.getAllSites();
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

    // can delete if confirmed not needed
    @Get(value = "/id/{id}", produces = MediaType.TEXT_JSON)
    public Site list(int id) {
        return siteManager.getByPrimaryKey(id);
    }

    @Get(value = "/{name}")
    public Site list(String name){return siteManager.getBySiteName(name);}

    @Delete("/{name}")
    public HttpResponse delete(String name){
        Site s = siteManager.getBySiteName(name);
        siteManager.delete(s);
        return HttpResponse.noContent();
    }

//    @Delete("/{id}")
//    public HttpResponse delete(int id) {
//        siteManager.delete(id);
//        return HttpResponse.noContent();
//    }




    @Put("/{name}")
    public HttpResponse update(String name, @Body Site updatedSite) {
        siteManager.update(updatedSite);
        //List<Page> p = pageManager.getAllPagesOfSite(updatedSite);
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
