package main.java.com.projectBackEnd.Entities.Page;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URI;
import java.util.List;

@Controller("/page")
public class PageController {
    protected final PageManagerInterface pageManager = PageManager.getPageManager();
    protected final ImageHandler imageHandler = new ImageHandler();
    PageController() {}
    @Get(value = "/{slug}", produces = MediaType.TEXT_JSON)
    public Page list(String slug) {
        return pageManager.getByPrimaryKey(slug);
    }

    @Get("/")
    public String index() {
        return "Page page";
    }

    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<Page> list() {
        return pageManager.getAllPages();
    }

    @Get(value = "/images", produces = MediaType.TEXT_JSON)
    public List<String> images() { return imageHandler.getImageUrls(); }
//    @Delete("/")
//    public HttpResponse deleteAll() {
//        pageManager.deleteAll();
//        return HttpResponse.noContent();
//    }
    @Post("/")
    public HttpResponse<Page> add(@Body Page pageToAdd) {
        Page page = pageManager.addPage(pageToAdd.getPrimaryKey(), pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent());
        //TODO will still return created even if there's an unsuccessful creation, this if statement prevents that.
        if (pageManager.getByPrimaryKey(pageToAdd.getPrimaryKey()) == null) return HttpResponse.serverError(); //I.e. object didn't get created
        return HttpResponse
                .created(page)
                .headers(headers -> headers.location((location(page.getPrimaryKey()))));
    }

    @Post("/images")
    public HttpResponse<Page> saveImages(@Body List<String> imagesUrls) {
        if (imageHandler.saveImages(imagesUrls)) return HttpResponse.serverError(); //I.e. object didn't get created
        return HttpResponse.noContent();
    }

    @Put("/")
    public HttpResponse update(@Body Page updatedPage) {
        Page page = new Page(updatedPage.getPrimaryKey(), updatedPage.getIndex(), updatedPage.getTitle(), updatedPage.getContent());
        pageManager.update(page);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(updatedPage.getPrimaryKey()).getPath());
    }

    @Delete("/{slug}")
    public HttpResponse delete(String slug) {
        pageManager.delete(slug);
        return HttpResponse.noContent();
    }

    protected URI location(String slug) {
        String encodedSlug = null;
        try {
            encodedSlug = URLEncoder.encode(slug, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return URI.create("/page/" + encodedSlug);
    }
}
