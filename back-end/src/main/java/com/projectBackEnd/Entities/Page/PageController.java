package main.java.com.projectBackEnd.Entities.Page;


import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;

@Controller("/page")
public class PageController {
    //TODO
    protected final PageManagerInterface pageManager = new PageManager();
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
    public HttpResponse<Page> add(@Body PageCommand command) {
        Page page = pageManager.addPage(command.getSlug(), command.getIndex(), command.getTitle(), command.getContent());
        return HttpResponse
                .created(page)
                .headers(headers -> headers.location(location(page.getPrimaryKey())));
    }

    @Put("/")
    public HttpResponse update(@Body PageCommand command) {
        Page page = new Page(command.getSlug(), command.getIndex(), command.getTitle(), command.getContent());
        pageManager.update(page);
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getSlug()).getPath());
    }
    protected URI location(String slug) {
        return URI.create("/page/" + slug);
    }
    protected URI location(Page page) {
        return location(page.getPrimaryKey());
    }
}
