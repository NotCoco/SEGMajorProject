package main.java.com.projectBackEnd.Entities.News;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;
import java.util.List;

@Controller("/news")
public class NewsController {

    protected final NewsManagerInterface newsManager = NewsManager.getNewsManager();

    @Get(value = "/{id}", produces = MediaType.TEXT_JSON)
    public News list(int id) {
        return newsManager.getByPrimaryKey(id);
    }

    @Get("/")
    public List<News> index(){
        return newsManager.getAllNews();
    }

    @Delete("/{id}")
    public HttpResponse delete(int id) {
        newsManager.delete(id);
        return HttpResponse.noContent();
    }

    @Post("/")
    public HttpResponse<News> add(@Body NewsAddCommand command) {

        News n = newsManager.addNews(command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug());

        if(newsManager.getByPrimaryKey(n.getPrimaryKey()) == null)return HttpResponse.serverError();

        return HttpResponse
                .created(n)
                .headers(headers -> headers.location(location(n.getPrimaryKey())));
    }


    @Put("/")
    public HttpResponse update(@Body NewsUpdateCommand command) {
        News n = new News(command.getId(), command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug());
        newsManager.update(n);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    protected URI location(int id) {
        return URI.create("/news/" + id);
    }

    protected URI location(News news) {
        return location(news.getPrimaryKey());
    }

}