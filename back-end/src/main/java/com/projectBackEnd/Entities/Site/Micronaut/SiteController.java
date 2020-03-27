package main.java.com.projectBackEnd.Entities.Site.Micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.Site;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManagerInterface;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

/**
 * Site Controller class is used for the interactions between frontend and backend
 * There are functionalites :
 *    - get all the sites
 *    - add a new site
 *    - get a site
 *    - delete a site
 *    - update a site
 */
@Controller("/sites")
public class SiteController {

    final SiteManagerInterface siteManager = SiteManager.getSiteManager();
	private final SessionManagerInterface sessionManager = SessionManager.getSessionManager();


    SiteController() {
        super();
    }

    /**
     * Get all the sites by http GET method
     * @return get a list of all the sites
     */
    @Get("/")
    public List<Site> index(){
        return siteManager.getAllSites();
    }

    /**
     * Add a new site to the database with SiteAddCommand by http POST method
     * @param session
     * @param command Dedicated SiteAddCommand class to add new medicine
     * @return Http response with relevant information which depends on the result of
     * inserting the new site
     */
    @Post("/")
    public HttpResponse<Site> add(@Header("X-API-Key") String session,@Body SiteAddCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Site s = siteManager.addSite(new Site(command.getSlug(), command.getName()));
        if(siteManager.getByPrimaryKey(s.getPrimaryKey()) == null){
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(s)
                .headers(headers -> headers.location(location(s.getSlug())));
    }

    /**
     * Get the specified site with id by http GET method
     * @param id primary id of the site
     * @return get the specified site
     */
    // can delete if confirmed not needed
    @Get(value = "/id/{id}", produces = MediaType.TEXT_JSON)
    public Site list(int id) {
        return siteManager.getByPrimaryKey(id);
    }

    /**
     * Get all the sites by http GET method
     * @param slug slug name
     * @return get the specified site
     */
    @Get(value = "/{slug}")
    public Site list(String slug){return siteManager.getBySiteSlug(slug);}

    /**
     * Delete a site with specified slug name by http Delete method
     * @param session
     * @param slug
     * @return Http response with no content
     */
    @Delete("/{slug}")
    public HttpResponse delete(@Header("X-API-Key") String session,String slug){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Site s = siteManager.getBySiteSlug(slug);
        siteManager.delete(s.getPrimaryKey());
        return HttpResponse.noContent();
    }

    /**
     * Update a site with SiteUpdateCommand
     * @param session
     * @param updatedSiteCommand Dedicated SiteUpdateCommand class to update site
     * @return Http response with path
     */
    @Put("/")
    public HttpResponse update(@Header("X-API-Key") String session,@Body SiteUpdateCommand updatedSiteCommand) {
  		  if(!sessionManager.verifySession(session))
			    return HttpResponse.unauthorized();
        System.out.println("+++++++++" + updatedSiteCommand.getId() + " " + updatedSiteCommand.getSlug() + " " + updatedSiteCommand.getName());
        Site newSite = new Site(updatedSiteCommand.getId(), updatedSiteCommand.getSlug(), updatedSiteCommand.getName());
        siteManager.update(newSite);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(updatedSiteCommand.getSlug()).getPath());
    }

    /**
     * Create a URI with the specified site slug name
     * @param siteSlug
     * @return created URI
     */
    protected URI location(String siteSlug) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteSlug, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/sites/" + encodedSlug);
    }


}
