package main.java.com.projectBackEnd.Entities.Site;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;

import java.util.List;

public class SiteController {
    final SiteManagerInterface siteManager = SiteManager.getSiteManager();
    SiteController() {}
    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<Site> list() {
        return siteManager.getAllSites();
    }

}
