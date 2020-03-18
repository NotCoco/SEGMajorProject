package main.java.com.projectBackEnd.Entities.News;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;
import java.util.List;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

@Controller("/news")
public class NewsController {

    protected final NewsManagerInterface newsManager = NewsManager.getNewsManager();
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

    @Get("/")
    public List<News> index(){
        return newsManager.getAllNews();
    }

    @Delete("/{slug}")
    public HttpResponse delete(@Header("X-API-Key") String session,String slug) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        newsManager.delete(newsManager.getNewsBySlug(slug).getPrimaryKey());
        return HttpResponse.noContent();
    }

    @Post("/")
    public HttpResponse<News> add(@Header("X-API-Key") String session,@Body NewsAddCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        News news = newsManager.addNews(command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug());

        if(newsManager.getByPrimaryKey(news.getPrimaryKey()) == null)return HttpResponse.serverError();

        return HttpResponse
                .created(news)
                .headers(headers -> headers.location(location(news.getSlug())));
    }


    @Put("/")
    public HttpResponse update(@Header("X-API-Key") String session,@Body NewsUpdateCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
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
