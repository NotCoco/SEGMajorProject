package main.java.com.projectBackEnd.Entities.Page.Micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Page.Hibernate.Page;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManager;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManagerInterface;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;


/**
 * Page Controller is a REST API endpoint.
 * It deals with the interactions between the server and the Page table in the database.
 * It provides HTTP requests for each of the queries that need to be made to add, remove, update and retrieve
 * pages from the database.
 */
@Controller("/sites")
public class PageController {

    final PageManagerInterface pageManager = PageManager.getPageManager();

    final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

    /**
     * Default constructor
     */
    public PageController() {}


    /**
     * Insert a new page into the database using PageAddCommand methods via an HTTP Post request
     * @param session   Current session
     * @param pageToAdd Dedicated PageAddCommand class to add new page to the database
     * @return HTTP response with relevant information resulting from the insertion of the page
     */
    @Post("/{name}/pages")
    public HttpResponse<Page> addPage(@Header("X-API-Key") String session, String name, @Body PageAddCommand pageToAdd) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        Page p = pageManager.addPage(new Page(pageToAdd.getSite(), pageToAdd.getSlug(),
                pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent()));
        if (pageManager.getByPrimaryKey(p.getPrimaryKey()) == null) return HttpResponse.serverError();

        return HttpResponse
                .created(p)
                .headers(headers -> headers.location(pageLocation(name, p.getSlug())));
    }

    /**
     * Update a page with PageUpdateCommand methods via an HTTP Put request
     * @param name                  Name of the Page
     * @param updatedPageCommand    Dedicated PageUpdateCommand for updating the page
     * @return HTTP response resulting from the Put request with path
     */
    @Put("{name}/pages/")
    public HttpResponse updatePage(@Header("X-API-Key") String session, String name, @Body PageUpdateCommand updatedPageCommand) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        Page updatedPage = new Page(updatedPageCommand.getPrimaryKey(), updatedPageCommand.getSite(), updatedPageCommand.getSlug(),
                updatedPageCommand.getIndex(), updatedPageCommand.getTitle(), updatedPageCommand.getContent());
        pageManager.update(updatedPage);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, pageLocation(name, updatedPage.getSlug()).getPath());
    }


    /**
     * Get the specific Page corresponding to the given site name and page name via an HTTP Get request
     * @param name  Site name
     * @param page  Page name
     * @return      Page found
     */
    @Get("/{name}/pages/{page}")
    public Page getPage(String name, String page) {
        return pageManager.getPageBySiteAndSlug(name, page);
    }


    /**
     * Get a list of all the pages under the same site via an HTTP GET request
     * @param name  Name of the parent site
     * @return      List of all pages belonging to specified site
     */
    @Get("/{name}/pages")
    public List<Page> pages(String name) {
        return pageManager.getAllPagesOfSite(name);
    }


    /**
     * Update multiple pages via an HTTP Patch request
     * @param name              Name of the parent site
     * @param patchCommandList  List of patch commands
     * @return HTTP response with no content
     */
    @Patch("/{name}/page-indices")
    public HttpResponse<Page> patchPage(@Header("X-API-Key") String session, String name, @Body List<PagePatchCommand> patchCommandList) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();

        for (PagePatchCommand p : patchCommandList) {
            String slug = p.getSlug();
            Page page = pageManager.getPageBySiteAndSlug(name, slug);
            page.setIndex(p.getIndex());
            pageManager.update(page);
        }

        return HttpResponse.noContent();
    }


    /**
     * Remove the Page corresponding to the given Page name and Site name from the database via an HTTP Delete request
     * @param name  Name of the parent site
     * @param page  Name of the page to remove
     * @return Http response with relevant information resulting from the deletion of the page
     */
    @Delete("/{name}/pages/{page}")
    public HttpResponse deletePage(@Header("X-API-Key") String session, String name, String page) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        Page p = pageManager.getPageBySiteAndSlug(name, page);
        pageManager.delete(p.getPrimaryKey());

        return HttpResponse.noContent();
    }


    /**
     * Get the URI of a specific page
     * @param siteName  Name of the site
     * @param pageName  Name of the page to locate
     * @return URI of the page
     */
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