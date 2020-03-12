package main.java.com.projectBackEnd.Entities.Site;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

public class SiteController {
    final SiteManagerInterface siteManager = SiteManager.getSiteManager();
    SiteController() {}
    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<Site> list() {
        return siteManager.getAllSites();
    }

    @Post("/")
    public HttpResponse<Site> add(@Body Site newSiteToAdd) { //TODO Change to site command
        Site site = siteManager.addSite(newSiteToAdd.getName());
        //TODO will still return created even if there's an unsuccessful creation, this if statement prevents that.
        if (siteManager.getByPrimaryKey(site.getPrimaryKey()) == null) return HttpResponse.serverError(); //I.e. object didn't get created
        return HttpResponse
                .created(site)
                .headers(headers -> headers.location((location(site.getName())))); //TODO location should use (also unique) name.
    }

    protected URI location(String siteName) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(siteName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/site/" + encodedSlug);
    }

}
