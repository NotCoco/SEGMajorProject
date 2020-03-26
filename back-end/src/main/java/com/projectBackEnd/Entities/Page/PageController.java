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
 * PageController no longer affects Site Controller, they just share the same @Controller tag.
 */
@Controller("/sites")
public class PageController {
    final PageManagerInterface pageManager = PageManager.getPageManager();

    public PageController() {
    }

    @Get("/{name}/pages")
    public List<Page> pages(String name) {
        return pageManager.getAllPagesOfSite(name);
    }

   @Get("/{name}/pages/{page}")
    public Page getPage(String name, String page) {
        return pageManager.getPageBySiteAndSlug(name, page);
    }


    @Patch("/{name}/page-indices")
    public HttpResponse<Page> patchPage(@Header("X-API-Key") String session, String name, @Body List<PagePatchCommand> patchCommandList) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
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
    public HttpResponse<Page> addPage(@Header("X-API-Key") String session, String name, @Body PageAddCommand pageToAdd) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Page p = pageManager.addPage(pageToAdd.getSite(), pageToAdd.getSlug(), pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent());
        System.out.println(" ++++++"+ p.getPrimaryKey() + " " + p.getSite().getSlug());
        if (pageManager.getByPrimaryKey(p.getPrimaryKey()) == null) {
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(p)
                .headers(headers -> headers.location(pageLocation(name, p.getSlug())));
    }

    @Delete("/{name}/pages/{page}")
    public HttpResponse deletePage(@Header("X-API-Key") String session, String name, String page) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Page p = pageManager.getPageBySiteAndSlug(name, page);
        pageManager.delete(p);
        return HttpResponse.noContent();
    }

    @Put("{name}/pages/")
    public HttpResponse updatePage(@Header("X-API-Key") String session, String name, @Body PageUpdateCommand updatedPageCommand) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Page updatedPage = new Page(updatedPageCommand.getPrimaryKey(), updatedPageCommand.getSite(), updatedPageCommand.getSlug(), updatedPageCommand.getIndex(), updatedPageCommand.getTitle(), updatedPageCommand.getContent());

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
