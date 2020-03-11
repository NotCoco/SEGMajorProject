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
		HttpResponse response = null;
		try{
			userManager.addUser(user.getEmail(),user.getPassword());
			response = HttpResponse.created("user created"); 
		}
		catch(EmailExistsException e){
			response = HttpResponse.badRequest("user already exsits");
		}
		catch(InvalidEmailException e){
			response = HttpResponse.badRequest("invalid email address");
		}
		finally{
			if(response == null) // should not be possible
				return HttpResponse.serverError();
			else
				return response;
		}
	}

	@Post("/login")
	public HttpResponse<String> login(@Body User user){
        	return HttpResponse.ok("");
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
        	return HttpResponse.ok();
	}

    	@Post("/verify_token")
	public HttpResponse verifyToken(@Body String token){
        	return HttpResponse.ok();
	}


}

