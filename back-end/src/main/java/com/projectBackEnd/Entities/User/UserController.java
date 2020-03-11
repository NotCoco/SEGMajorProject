package main.java.com.projectBackEnd.Entities.User;


import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

import java.net.URI;

@Controller("/user")
public class UserController {
    private final UserManagerInterface userManager = UserManager.getUserManager();
    private final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
    
	@Post("/create")
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

	/**
	 TODO:
		- check with email functionality
	@Put("/change_password")
	public HttpResponse changePassword(){
        	return HttpResponse.ok();
	}
	*/

    	@Delete("/delete_user")
	public HttpResponse deleteUser(@Body String email){

		try{
			userManager.deleteUser(email);
			return HttpResponse.ok();
		}
		catch(UserNotExistException e){
			System.out.println(email);
			return HttpResponse.notFound("user does not exsist");
		}
	}

    	@Post("/verify_session")
	public HttpResponse verifySession(@Body String token){
		if(sessionManager.verifySession(token))
			return HttpResponse.ok();
		else
			return HttpResponse.notFound();
		
	}


}

