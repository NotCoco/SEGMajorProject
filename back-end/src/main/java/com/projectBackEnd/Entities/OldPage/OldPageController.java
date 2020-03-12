package main.java.com.projectBackEnd.Entities.OldPage;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URI;
import java.util.List;

@Controller("/page")
public class OldPageController {
    protected final OldPageManagerInterface pageManager = OldPageManager.getOldPageManager();
    OldPageController() {}
    @Get(value = "/{slug}", produces = MediaType.TEXT_JSON)
    public OldPage list(String slug) {
        return pageManager.getByPrimaryKey(slug);
    }

    @Get("/")
    public String index() {
        return "OldPage page";
    }

    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<OldPage> list() {
        return pageManager.getAllPages();
    }

//    @Delete("/")
//    public HttpResponse deleteAll() {
//        pageManager.deleteAll();
//        return HttpResponse.noContent();
//    }
    @Post("/")
    public HttpResponse<OldPage> add(@Body OldPage oldPageToAdd) {
        OldPage oldPage = pageManager.addOldPage(oldPageToAdd.getPrimaryKey(), oldPageToAdd.getIndex(), oldPageToAdd.getTitle(), oldPageToAdd.getContent());
        //TODO will still return created even if there's an unsuccessful creation, this if statement prevents that.
        if (pageManager.getByPrimaryKey(oldPageToAdd.getPrimaryKey()) == null) return HttpResponse.serverError(); //I.e. object didn't get created
        return HttpResponse
                .created(oldPage)
                .headers(headers -> headers.location((location(oldPage.getPrimaryKey()))));
    }

    @Put("/")
    public HttpResponse update(@Body OldPage updatedOldPage) {
        OldPage oldPage = new OldPage(updatedOldPage.getPrimaryKey(), updatedOldPage.getIndex(), updatedOldPage.getTitle(), updatedOldPage.getContent());
        pageManager.update(oldPage);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(updatedOldPage.getPrimaryKey()).getPath());
    }

    @Delete("/{slug}")
    public HttpResponse delete(String slug) {
        pageManager.delete(slug);
        return HttpResponse.noContent();
    }

    protected URI location(String slug) { //Make it take not the ID to assign the location
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(slug, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/page/" + encodedSlug);
    }
}
