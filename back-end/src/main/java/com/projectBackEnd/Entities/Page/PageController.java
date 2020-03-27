package main.java.com.projectBackEnd.Entities.Page;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Site.*;
import main.java.com.projectBackEnd.Entities.Page.*;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

/**
 * PageController no longer affects Site Controller, they just share the same @Controller tag.
 */


/**
 * Page Controller class is used for the interactions between frontend and backend
 * There are functionalites :
 *    - get all the pages
 *    - get one specific page
 *    - update a page with multiple changes
 *    - add a new page
 *    - delete a page
 *    - update a page
 */
@Controller("/sites")
public class PageController {
    final PageManagerInterface pageManager = PageManager.getPageManager();
	final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
    public PageController() {
    }

    /**
     * Get all the pages under the same site by http GET method
     * @param name of the site
     * @return get the list of pages
     */
    @Get("/{name}/pages")
    public List<Page> pages(String name) {
        return pageManager.getAllPagesOfSite(name);
    }

    /**
     * Update a list pages with multiple changes
     * @param name of the site
     * @param patchCommandList
     * @return Http response with no content
     */
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

    /**
     * Add a new page to the specified site with the name of site and dedicated PageAddCommand
     * by http POST method
     * @param name the name of the site
     * @param  pageToAdd dedicated PageAddCommand class to add a new page
     * @return Http response with relevant information which depends on the result of
     * inserting the new page
     */
    @Post("/{name}/pages")
    public HttpResponse<Page> addPage(@Header("X-API-Key") String session, String name, @Body PageAddCommand pageToAdd) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Page p = pageManager.addPage(new Page(pageToAdd.getSite(), pageToAdd.getSlug(), pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent()));
        if (pageManager.getByPrimaryKey(p.getPrimaryKey()) == null) {
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(p)
                .headers(headers -> headers.location(pageLocation(name, p.getSlug())));
    }

    /**
     * Delete a page with specified id by the name of site and the page name
     * @param name site name
     * @param page page name
     * @return Http response with relevant information which depends on the result of
     * deleting the specified page
     */
    @Delete("/{name}/pages/{page}")
    public HttpResponse deletePage(@Header("X-API-Key") String session, String name, String page) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Page p = pageManager.getPageBySiteAndSlug(name, page);
        pageManager.delete(p.getPrimaryKey());
        return HttpResponse.noContent();
    }
    
    /**
     * Get the page by http GET method
     * @param name site name
     * @param page page name
     * @return the page
     */
    @Get("/{name}/pages/{page}")
    public Page getPage(String name, String page) {
        return pageManager.getPageBySiteAndSlug(name, page);
    }

    /**
     * Update a page with site name and PageUpdateCommand
     * @param name site name
     * @param updatedPageCommand the dedicated PageUpdateCommand for updating the page
     * @return Http response with path
     */
    @Put("{name}/pages/")
    public HttpResponse updatePage(@Header("X-API-Key") String session,String name, @Body PageUpdateCommand updatedPageCommand) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Page updatedPage = new Page(updatedPageCommand.getId(), updatedPageCommand.getSite(), updatedPageCommand.getSlug(), updatedPageCommand.getIndex(), updatedPageCommand.getTitle(), updatedPageCommand.getContent());


        pageManager.update(updatedPage);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, pageLocation(name, updatedPage.getSlug()).getPath());
    }

    /**
     * Get the URI of a specific page
     * @param siteName
     * @param pageName
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
