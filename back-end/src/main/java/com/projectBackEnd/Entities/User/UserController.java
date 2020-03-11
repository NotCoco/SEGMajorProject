package main.java.com.projectBackEnd.Entities.User;


import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;

@Controller("/user")
public class UserController {
    private final UserManagerInterface userManager = UserManager.getUserManager();
    
	@Post("/create")
	public HttpResponse createUser(){
        	return HttpResponse.created("");
	}
	@Post("/login")
	public HttpResponse<String> login(){
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
	public HttpResponse deleteUser(){
        	return HttpResponse.ok();
	}

    	@Post("/verify_token")
	public HttpResponse verifyToken(){
        	return HttpResponse.ok();
	}


}

