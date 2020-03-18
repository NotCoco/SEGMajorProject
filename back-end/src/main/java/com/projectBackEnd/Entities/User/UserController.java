package main.java.com.projectBackEnd.Entities.User;


import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

import main.java.com.projectBackEnd.Entities.ResetLinks.ResetLinkManager;
import main.java.com.projectBackEnd.Entities.ResetLinks.ResetLinkManagerInterface;
import main.java.com.projectBackEnd.Entities.ResetLinks.EmailNotExistException;


import java.net.URI;

@Controller("/user")
public class UserController {
    private final UserManagerInterface userManager = UserManager.getUserManager();
    private final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
    
	@Post("/create")//
	public HttpResponse createUser(@Body User user){

		try{
			userManager.addUser(user.getEmail(),user.getPassword());
			return HttpResponse.created("user created"); 
		}
		catch(EmailExistsException e){
			return HttpResponse.badRequest("user already exsits");
		}
		catch(InvalidEmailException e){
			return HttpResponse.badRequest("invalid email address");
		}
	}

	@Post("/login")
	public HttpResponse<String> login(@Body User user){
		String token = userManager.verifyUser(user.getEmail(),user.getPassword());
		if(token != null)
			return HttpResponse.ok(token);
		else
			return HttpResponse.notFound("invalid credentials");
	}

    	@Delete("/delete_user")
	public HttpResponse deleteUser(@Body User user){
		try{
			userManager.deleteUser(user.getEmail(), user.getPassword());
			return HttpResponse.ok();
		}
		catch(UserNotExistException e){
			return HttpResponse.notFound("user does not exsist");
		}
	}

	@Put("/change_password") 
	public HttpResponse<String> changePassword(@Body PasswordResetBody body){
		try{
        	PasswordReset.getPasswordResetManager().changePassword(body.token, body.password);
			return HttpResponse.ok();
		}
		catch(TokenNotExistException e){
			return HttpResponse.notFound("incorrect token");
		}
		catch(UserNotExistException e){
			return HttpResponse.notFound("token did not match any user");
		}	
	}
	@Post("/password_reset") 
	public HttpResponse<String> getPasswordReset(@Body StringBody body){

		try{

			PasswordReset.getPasswordResetManager().sendPasswordResetLink(body.string);
			return HttpResponse.ok();
		}
		catch(EmailNotExistException e){
			return HttpResponse.notFound("incorrect email");
		}
		catch(ServerErrorException e){
			return HttpResponse.serverError();
		}	
	}
	@Put("/change_email") 
	public HttpResponse<String> changeEmail(@Header String session, @Body ChangeEmailBody body){
		if(sessionManager.verifySession(session))
		{
			try{
				userManager.changeEmail(body.oldEmail, body.newEmail);
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
	


}

