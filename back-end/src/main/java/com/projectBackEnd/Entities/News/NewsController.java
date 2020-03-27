package main.java.com.projectBackEnd.Entities.News;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;
import java.util.List;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

/**
 * News Controller class is used for the interactions between frontend and backend
 * There are functionalites :
 *    - get all the news
 *    - update a news
 *    - delete a news
 *    - add news
 */
@Controller("/news")
public class NewsController {

    protected final NewsManagerInterface newsManager = NewsManager.getNewsManager();
	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

    /**
     * Get all news by http GET method
     * @return get a list of all the News
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
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        newsManager.delete(newsManager.getNewsBySlug(slug).getPrimaryKey());
        return HttpResponse.noContent();
    }

    /**
     * Add new news to the database with NewsAddCommand by http POST method
     * @param session
     * @param command Dedicated NewsAddCommand class to add new news
     * @return Http response with relevant information which depends on the result of
     * inserting new news
     */
    @Post("/")
    public HttpResponse<News> add(@Header("X-API-Key") String session,@Body NewsAddCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        News news = newsManager.addNews(new News(command.getDate(), command.isPinned(), command.getDescription(),
                command.getTitle(), command.isUrgent(), command.getContent(), command.getSlug()));

        if(newsManager.getByPrimaryKey(news.getPrimaryKey()) == null)return HttpResponse.serverError();

        else return HttpResponse
                .created(news)
                .headers(headers -> headers.location(location(news.getSlug())));
    }

    /**
     * Update medicine with MedicineUpdateCommand
     * @param session
     * @param command Dedicated NewsUpdateCommand class to update news
     * @return Http response with path
     */
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

    /**
     * Create URI with the specified slug
     * @param slug
     * @return created URI
     */
    protected URI location(String slug) {
        return URI.create("/news/" + slug);
    }

    /**
     * Create URI with existing news object
     * @param news news object
     * @return created URI
     */
    protected URI location(News news) {
        return location(news.getSlug());
    }

}
