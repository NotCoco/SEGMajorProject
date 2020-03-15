package main.java.com.projectBackEnd.Entities.Page;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Site.*;
import main.java.com.projectBackEnd.Entities.Page.*;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

/**
 * Pages are components of sites, but page control methods could be moved into a more
 * cohesive class of their own for maintaining PageControl Code. The SiteController class still continues to use
 * these API endpoints and test them.
 * Essentially, these methods could be grouped in another way.
 * //TODO Someone verify if this was a good idea please!
 */
public abstract class PageController {
    final PageManagerInterface pageManager = PageManager.getPageManager();

    public PageController() {
    }

    @Get("/{name}/pages")
    public List<Page> pages(String name) {
        return pageManager.getAllPagesOfSite(name);
    }

    @Patch("/{name}/page-indices")
    public HttpResponse<Page> patchPage(String name, @Body List<PagePatchCommand> patchCommandList) {
        for (PagePatchCommand p : patchCommandList) {
            String slug = p.getSlug();
            Page page = pageManager.getPageBySiteAndSlug(name, slug);
            page.setIndex(p.getIndex());
            pageManager.update(page);
        }
        return HttpResponse
                .noContent();
    }

    @Post("/{name}/pages")
    public HttpResponse<Page> addPage(String name, @Body PageAddCommand pageToAdd) {
        Page p = pageManager.addPage(pageToAdd.getSite(), pageToAdd.getSlug(), pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent());
        if (pageManager.getPageBySiteAndSlug(p.getSite(), p.getSlug()) == null) {
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(p)
                .headers(headers -> headers.location(pageLocation(name, p.getSlug())));
    }

    @Delete("/{name}/pages/{page}")
    public HttpResponse deletePage(String name, String page) {
        Page p = pageManager.getPageBySiteAndSlug(name, page);
        pageManager.delete(p);
        return HttpResponse.noContent();
    }

    @Get("/{name}/pages/{page}")
    public Page getPage(String name, String page) {
        return pageManager.getPageBySiteAndSlug(name, page);
    }

    @Put("{name}/pages/")
    public HttpResponse updatePage(String name, @Body Page updatedPage) {
        //Page updatedPage = new Page(updatedPageCommand.getID(), updatedPageCommand.getSite(), updatedPageCommand.getSlug(), updatedPageCommand.getIndex(), updatedPageCommand.getTitle(), updatedPageCommand.getContent());
        pageManager.update(updatedPage);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, pageLocation(name, updatedPage.getSlug()).getPath());
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
}