package main.java.com.projectBackEnd.Entities.User.Micronaut;


import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;
import main.java.com.projectBackEnd.Entities.Session.NoSessionException;

import main.java.com.projectBackEnd.Entities.User.Hibernate.*;

import main.java.com.projectBackEnd.Entities.ResetLinks.ResetLinkManager;
import main.java.com.projectBackEnd.Entities.ResetLinks.ResetLinkManagerInterface;
import main.java.com.projectBackEnd.Entities.ResetLinks.EmailNotExistException;


import java.net.URI;

/**
 * User Controller class is used for the interactions between frontend and backend
 * There are functionalites :
 *    - add a new user
 *    - login
 *    - delete a user
 *    - change password
 *    - reset password
 *    - change email
 */
@Controller("/user")
public class UserController {
    private final UserManagerInterface userManager = UserManager.getUserManager();
    private final SessionManagerInterface sessionManager = SessionManager.getSessionManager();

	/**
	 * Add a new user to the database with user by http POST method
	 * @param user
	 * @return Http response with relevant information which depends on the result of
	 * inserting a new user
	 */
	@Post("/create")//
	public HttpResponse createUser(@Body UserBody user){

		try{
			userManager.addUser(user.getEmail(),user.getPassword(),user.getName());
			return HttpResponse.created("user created"); 
		}
		catch(EmailExistsException e){
			return HttpResponse.badRequest("user already exsits");
		}
		catch(InvalidEmailException e){
			return HttpResponse.badRequest("invalid email address");
		}
	}

	/**
	 * Login of user by http POST method
	 * @param user
	 * @return Http response with relevant information which depends on the result of
	 * login
	 */
	@Post("/login")
	public HttpResponse<String> login(@Body UserBody user){
		String token = userManager.verifyUser(user.getEmail(),user.getPassword());
		if(token != null)
			return HttpResponse.ok(token);
		else
			return HttpResponse.notFound("invalid credentials");
	}

	/**
	 * Delete a user by http Delete method
	 * @param user
	 * @return Http response with relevant information which depends on the result of
	 * deleting
	 */
    	@Delete("/delete_user")
	public HttpResponse deleteUser(@Body UserBody user){
		try{
			userManager.deleteUser(user.getEmail(), user.getPassword());
			return HttpResponse.ok();
		}
		catch(UserNotExistException e){
			return HttpResponse.notFound("user does not exsist");
		}
	}

	/**
	 * Change user's password with PasswordResetBody
	 * @param body Dedicated PasswordResetBody class to update password
	 * @return Http response with relevant information which depends on the result of
	 * updating password
	 */
	@Put("/password_reset_change") 
	public HttpResponse<String> passwordReset(@Body PasswordResetBody body){
		try{
        	PasswordReset.getPasswordResetManager().changePassword(body.getToken(), body.getPassword());
			return HttpResponse.ok();
		}
		catch(TokenNotExistException e){
			return HttpResponse.notFound("incorrect token");
		}
		catch(UserNotExistException e){
			return HttpResponse.notFound("token did not match any user");
		}	
	}

	/**
	 * Reset the password by http POST method
	 * @param body
	 * @return Http response with relevant information which depends on the result of
	 * resetting a user's password
	 */
	@Post("/password_reset_request")
	public HttpResponse<String> getPasswordReset(@Body StringBody body){

		try{

			PasswordReset.getPasswordResetManager().sendPasswordResetLink(body.getString());
			return HttpResponse.ok();
		}
		catch(EmailNotExistException e){
			return HttpResponse.notFound("incorrect email");
		}
		catch(ServerErrorException e){
			return HttpResponse.serverError();
		}	
	}
	
	/**
	 * Change user's email with by Http PUT method
	 * @param session
	 * @param body Dedicated ChangeEmailBody class to update email
	 * @return Http response with relevant information which depends on the result of
	 * updating email
	 */
	@Put("/change_email") 
	public HttpResponse<String> changeEmail(@Header("X-API-Key") String session, @Body ChangeEmailBody body){
		try{
			if(!sessionManager.getEmail(session).equals(body.getOldEmail()))
				return HttpResponse.unauthorized();	
		}
		catch(NoSessionException e){
			return HttpResponse.unauthorized();
		}
		if(sessionManager.verifySession(session))
		{
			try{
				userManager.changeEmail(body.getOldEmail(), body.getNewEmail());
				return HttpResponse.ok();
			}
			catch(UserNotExistException e){
				return HttpResponse.notFound("no user with such email");
			}
			catch(EmailExistsException r){
				return HttpResponse.badRequest("this email already exists");
			}
		}
		else
			return HttpResponse.unauthorized();
		
	}
	@Put("/change_name") 
	public HttpResponse<String> changeName(@Header("X-API-Key") String session, @Body ChangeEmailBody body){
		try{
			if(!sessionManager.getEmail(session).equals(body.getOldEmail()))
				return HttpResponse.unauthorized();	
		}
		catch(NoSessionException e){
			return HttpResponse.unauthorized();
		}
		if(sessionManager.verifySession(session))
		{
			try{
				userManager.changeEmail(body.getOldEmail(), body.getNewEmail());
				return HttpResponse.ok();
			}
			catch(UserNotExistException e){
				return HttpResponse.notFound("no user with such email");
			}
			catch(EmailExistsException r){
				return HttpResponse.badRequest("this email already exists");
			}
		}
		else
			return HttpResponse.unauthorized();
		
	}

	@Put("/change_password") 
	public HttpResponse<String> changePassword(@Header("X-API-Key") String session,@Body StringBody body){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		try{
        		userManager.changePassword(sessionManager.getEmail(session), body.getString());
			return HttpResponse.ok();
		}
		catch(UserNotExistException e){
			return HttpResponse.notFound("token did not match any user");
		}	
		catch(NoSessionException e){
			return HttpResponse.unauthorized();
		}
	}


}

