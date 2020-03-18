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


    @Get("/")
    public List<News> index(){
        return newsManager.getAllNews();
    }

    @Delete("/{slug}")
    public HttpResponse delete(String slug) {
        newsManager.delete(newsManager.getNewsBySlug(slug).getPrimaryKey());
        return HttpResponse.noContent();
    }

    @Post("/")
    public HttpResponse<News> add(@Body NewsAddCommand command) {
        int sizeBefore = newsManager.getAllNews().size();
        News news = newsManager.addNews(command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug());
        if (sizeBefore == newsManager.getAllNews().size()) return HttpResponse.serverError();
        //if(newsManager.getByPrimaryKey(news.getPrimaryKey()) == null)return HttpResponse.serverError();
        //The returned news will have an ID of -1 since we don't specify one, so the error should always be thrown?
        else return HttpResponse
                .created(news)
                .headers(headers -> headers.location(location(news.getSlug())));
    }

    @Put("/")
    public HttpResponse update(@Body NewsUpdateCommand command) {
        News news= new News(command.getId(), command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug());
        newsManager.update(news);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getSlug()).getPath());
    }

    protected URI location(String slug) {
        return URI.create("/news/" + slug);
    }

    protected URI location(News news) {
        return location(news.getSlug());
    }

}
