package main.java.com.projectBackEnd.Entities.Site;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import main.java.com.projectBackEnd.Entities.Page.Page;
import main.java.com.projectBackEnd.Entities.Page.PageManager;
import main.java.com.projectBackEnd.Entities.Page.PageManagerInterface;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

@Controller("/sites")
public class SiteController {
    final SiteManagerInterface siteManager = SiteManager.getSiteManager();
    final PageManagerInterface pageManager = PageManager.getPageManager();

    SiteController() {}
    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<Site> list() {
        return siteManager.getAllSites();
    }

    @Get("/")
    public String index(){
        return "This is our site index page";
    }

    @Get("/{name}/pages")
    public List<Page> pages(String name){
        return pageManager.getAllPagesOfSite(name);
    }

    @Post("/{name}/pages")
    public HttpResponse<Page> addPage(String name, @Body Page pageToAdd){
        Page p = pageManager.addPage(pageToAdd);
        if(pageManager.getByPrimaryKey(p.getPrimaryKey()) == null){
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(p)
                .headers(headers -> headers.location(pageLocation(name, p.getSlug())));
    }

    @Get("/{name}/pages/{page}")
    public Page getPage(String name, String page){
        return pageManager.getPageBySiteAndSlug(name, page);
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
        siteManager.update(updatedSite);
        //List<Page> p = pageManager.getAllPagesOfSite(updatedSite);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(updatedSite.getName()).getPath());
    }

    protected URI pageLocation(String siteName, String pageName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
//        System.out.println("/sites/" + siteName + "/pages/" + encodedSlug);
//        System.out.println("/sites/" + siteName + "/pages/" + encodedSlug);
//        System.out.println("/sites/" + siteName + "/pages/" + encodedSlug);
        return URI.create("/sites/" + siteName + "/pages/" + encodedSlug);
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
