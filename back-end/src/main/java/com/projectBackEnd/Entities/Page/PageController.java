package main.java.com.projectBackEnd.Entities.Page;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;

@Controller("/page")
public class PageController {
    protected final PageManagerInterface pageManager = PageManager.getPageManager();
    PageController() {}
    @Get(value = "/{slug}", produces = MediaType.TEXT_JSON)
    public Page list(String id) {
        return pageManager.getByPrimaryKey(id);
    }

    @Get("/")
    public String index() {
        return "Page page";
    }

    @Post("/")
    public HttpResponse<Page> add(@Body Page pageToAdd) {
        Page page = pageManager.addPage(pageToAdd.getPrimaryKey(), pageToAdd.getIndex(), pageToAdd.getTitle(), pageToAdd.getContent());
        return HttpResponse
                .created(page)
                .headers(headers -> headers.location(location(page.getPrimaryKey())));
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
        return URI.create("/page/" + slug);
    }
    protected URI location(Page page) {
        return location(page.getPrimaryKey());
    }
}
