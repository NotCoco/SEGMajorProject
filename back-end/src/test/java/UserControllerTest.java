package test.java;


import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;

import main.java.com.projectBackEnd.Entities.User.Hibernate.*;
import main.java.com.projectBackEnd.Entities.User.Micronaut.*;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.ResetLinks.*;


import javax.inject.Inject;


import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.EntityManager;

@MicronautTest
public class UserControllerTest{
    

	
	@Inject
    @Client("/")
    HttpClient client;
	private static final UserManagerInterface userManager = UserManager.getUserManager();
	private static final ResetLinkManagerInterface linkManager = ResetLinkManager.getResetLinkManager();
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
    }

    @AfterAll
    public static void closeDatabase() {
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
		((EntityManager)userManager).deleteAll();
    }





}
