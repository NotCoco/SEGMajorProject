package main.java.com.projectBackEnd.Entities.News.Micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.net.URI;
import java.util.List;

import main.java.com.projectBackEnd.Entities.News.Hibernate.News;
import main.java.com.projectBackEnd.Entities.News.Hibernate.NewsManager;
import main.java.com.projectBackEnd.Entities.News.Hibernate.NewsManagerInterface;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

/**
 * News Controller is a REST API endpoint.
 * It deals with the interactions between the server and the News table in the database.
 * It provides HTTP requests for each of the queries that need to be made to add, remove, update and retrieve
 * news articles from the database.
 */
@Controller("/news")
public class NewsController {

    protected final NewsManagerInterface newsManager = NewsManager.getNewsManager();
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();


    /**
     * Get the list of all news stored in the database via an HTTP Get request
     * @return List of all the News in the database
     */
    @Get("/")
    public List<News> index(){
        return newsManager.getAllNews();
    }

    /**
     * Delete news with specified slug by http Delete method
     * @param session
     * @param slug
     * @return Http response with relevant information which depends on the result of
     * deleting news
     */
    @Delete("/{slug}")
    public HttpResponse delete(@Header("X-API-Key") String session,String slug) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        newsManager.delete(newsManager.getNewsBySlug(slug).getPrimaryKey());
        return HttpResponse.noContent();
    }


    /**
     * Insert a news article into the database using NewsAddCommand methods via an HTTP Post request
     * @param session   Current session
     * @param command   Dedicated NewsAddCommand class to add a news to the database
     * @return HTTP response with relevant information resulting from the insertion of a news into the database
     */
    @Post("/")
    public HttpResponse<News> add(@Header("X-API-Key") String session,@Body NewsAddCommand command) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        News news = newsManager.addNews(new News(command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug()));

        if(newsManager.getByPrimaryKey(news.getPrimaryKey()) == null) return HttpResponse.serverError();

        else return HttpResponse
                .created(news)
                .headers(headers -> headers.location(location(news.getSlug())));
    }


    /**
     * Update a medicine in the database using MedicineUpdateCommand methods, via an HTTP Put request
     * @param session   Current session
     * @param command   Dedicated NewsUpdateCommand class to update news
     * @return Http response with path
     */
    @Put("/")
    public HttpResponse update(@Header("X-API-Key") String session,@Body NewsUpdateCommand command) {

        if(!sessionManager.verifySession(session)) return HttpResponse.unauthorized();
        News news= new News(command.getPrimaryKey(), command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug());
        newsManager.update(news);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getSlug()).getPath());
    }


    /**
     * Create URI with the specified slug
     * @param slug  Slug of the object to locate
     * @return created URI
     */
    protected URI location(String slug) {
        return URI.create("/news/" + slug);
    }


    /**
     * Create URI with existing news object
     * @param news  News object to locate
     * @return created URI
     */
    protected URI location(News news) {
        return location(news.getSlug());
    }
}
