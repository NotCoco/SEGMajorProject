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

import java.util.List;
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

	@Post("/create")
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
		catch(InvalidPasswordException e){
			return HttpResponse.badRequest("invalid password");
		}		
		catch(IncorrectNameException e){
			return HttpResponse.badRequest("invalid name");
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
			return HttpResponse.unauthorized();
	}

	@Get("/logout")
	public HttpResponse logout(@Header("X-API-Key") String session){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		sessionManager.terminateSession(session);
		return HttpResponse.ok();
	}

	@Get("/")
	public HttpResponse<List<User>> index(@Header("X-API-Key") String session){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		return HttpResponse.ok(userManager.getUsers());
	}


	/**
	 * Delete a user by http Delete method
	 * @param user
	 * @return Http response with relevant information which depends on the result of
	 * deleting
	 */
    	@Delete("/delete")
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
			//frontend does not want an exception
		}
		catch(ServerErrorException e){
			//frontend does not want an exception
		}
		finally{
			return HttpResponse.ok();
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
		catch(InvalidPasswordException e){
			return HttpResponse.badRequest("invalid password");
		}
	}

	@Get("/user_details")
	public HttpResponse<UserBody> getUserDetails(@Header("X-API-Key") String session){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		try{
			String email = sessionManager.getEmail(session);
			return HttpResponse.ok(new UserBody(email,"",userManager.getName(email)));

		}
		catch(NoSessionException e){
			return HttpResponse.unauthorized();
		}
		catch(UserNotExistException e){
			return HttpResponse.notFound();
		}
	}	

  /**
	 * Change user's email with by Http PUT method
	 * @param session
	 * @param body Dedicated StringBody class to update email
	 * @return Http response with relevant information which depends on the result of
	 * updating email
	 */

	@Put("/change_email") 
	public HttpResponse<String> changeEmail(@Header("X-API-Key") String session, @Body StringBody body){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		else{
			try{
				String email = sessionManager.getEmail(session);
				userManager.changeEmail(email, body.getString());
				return HttpResponse.ok();
			}
			catch(NoSessionException e){
				return HttpResponse.unauthorized();
			}
			catch(UserNotExistException e){
				return HttpResponse.notFound("user does not exist");
			}
			catch(EmailExistsException e){
				return HttpResponse.badRequest("email already exists");
			}
		}
	}



	/**
	 * Change the name of the user on the current session via an HTTP Put request
	 * @param session	Current session
	 * @param body		New value for the username
	 * @return HTTP response resulting from the user name change
	 */
	@Put("/change_name") 
	public HttpResponse<String> changeName(@Header("X-API-Key") String session, @Body StringBody body){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		else{
			try{
				String email = sessionManager.getEmail(session);
				userManager.changeName(email, body.getString());
				return HttpResponse.ok();
			}
			catch(NoSessionException e){
				return HttpResponse.unauthorized();
			}
			catch(UserNotExistException e){
				return HttpResponse.notFound("user does not exist");
			}
			catch(IncorrectNameException e){
				return HttpResponse.badRequest("incorrect name");
			}
		}
	}
	/**
	 * Change the password of the user on the current session via an HTTP Put request
	 * @param session	Current session
	 * @param body		New password for the user
	 * @return HTTP response resulting from the password change
	 */
	@Put("/change_password") 
	public HttpResponse<String> changePassword(@Header("X-API-Key") String session,@Body StringBody body){
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
		else{
			try{
				String email = sessionManager.getEmail(session);
				userManager.changePassword(email, body.getString());
				return HttpResponse.ok();
			}
			catch(NoSessionException e){
				return HttpResponse.unauthorized();
			}
			catch(UserNotExistException e){
				return HttpResponse.notFound("user does not exist");
			}
			catch(InvalidPasswordException e){
				return HttpResponse.badRequest("invalid password");
			}
		}
	}


}

