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

    @Patch("/{name}/page-indices")
    public HttpResponse<Page> patchPage(String name, @Body List<PagePatchCommand> patchCommandList){
        for(PagePatchCommand p : patchCommandList){
            String slug = p.getSlug();
            Page page = pageManager.getPageBySiteAndSlug(name, slug);
            page.setIndex(p.getIndex());
            pageManager.update(page);
        }
        return HttpResponse
                .noContent();
    }

    @Post("/{name}/pages")
    public HttpResponse<Page> addPage(String name, @Body PageAddCommand pageToAdd){
        Page p = pageManager.addPage(pageToAdd.getSite().getName(), pageToAdd.getSlug(), pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent());
        if(pageManager.getPageBySiteAndSlug(p.getSite(), p.getSlug()) == null){
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(p)
                .headers(headers -> headers.location(pageLocation(name, p.getSlug())));
    }

    @Delete("/{name}/pages/{page}")
    public HttpResponse deletePage(String name, String page){
        Page p = pageManager.getPageBySiteAndSlug(name, page);
        pageManager.delete(p);
        return HttpResponse.noContent();
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

    @Delete("/{id}")
    public HttpResponse delete(int id) {
        siteManager.delete(id);
        return HttpResponse.noContent();
    }

    @Put("{name}/pages/{pageName}")
    public HttpResponse updatePage(String name, String pageName, @Body Page updatedPage){
        pageManager.update(updatedPage);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, pageLocation(name, updatedPage.getSlug()).getPath());
    }


    @Put("/{name}")
    public HttpResponse update(String name, @Body Site updatedSite) {
        siteManager.update(updatedSite);
        //List<Page> p = pageManager.getAllPagesOfSite(updatedSite);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(updatedSite.getName()).getPath());
    }

    protected URI pageLocation(String siteName, String pageName) {
        String encodedSlug = null;
        String encodedPage = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
            encodedPage = URLEncoder.encode(pageName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug + "/pages/" + encodedPage);
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
