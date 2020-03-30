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

import javax.inject.Inject;

import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.MedicineManager;
import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.MedicineManagerInterface;
import main.java.com.projectBackEnd.Entities.Medicine.Micronaut.MedicineAddCommand;
import main.java.com.projectBackEnd.Entities.Medicine.Micronaut.MedicineUpdateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.HibernateUtility;

/**
* class to unit test interactions between rest calls and system with respect to medicine functionality
*/
@MicronautTest
public class MedicineControllerTest{

    @Inject
    @Client("/")
    HttpClient client;

    static MedicineManagerInterface medicineManager;
    private static String token;
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        medicineManager = MedicineManager.getMedicineManager();
        try{
        	UserManager.getUserManager().addUser("test@test.com" , "123","name");
        	token = UserManager.getUserManager().verifyUser("test@test.com" , "123");
        }
        catch(Exception e){
        	fail();
        }    
}

    @AfterAll
    public static void closeDatabase() {
        try{
        	UserManager.getUserManager().deleteUser("test@test.com" , "123");
        }
        catch(Exception e){
        	fail();
        }    
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
        medicineManager.deleteAll();

    }
    @Test
    public void testNonExistingMedicineReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/medicines/3524"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testAddAndGetAll(){
        ArrayList<Integer> ids = new ArrayList<>();
        HttpResponse response;
        for(int i=0;i<3;i++){
            response = addMedicine("ShouldBeDeleted", "Liquid");
            ids.add(getEId(response).intValue());
        }

        HttpRequest request = HttpRequest.GET("/medicines");
        List<Medicine> medicineList = client.toBlocking().retrieve(request, Argument.of(List.class, Medicine.class));

        for(int i=0; i<ids.size();i++) assertEquals(ids.get(i), medicineList.get(i).getPrimaryKey());

    }
    @Test
    public void testAddEmptyNameMedicine(){
        HttpResponse response = addMedicine("", "Topical");
        int id =  getEId(response).intValue();
        Medicine testMed = getMedicine(id);
        assertEquals("Unnamed", testMed.getName());
    }

    @Test
    public void testAddEmptyTypeMedicine(){
        HttpResponse response = addMedicine("Med1", "");
        int id =  getEId(response).intValue();
        Medicine testMed = getMedicine(id);
        assertEquals("Undefined", testMed.getType());
    }

    @Test
    public void testAddAndGetMedicine(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();

        Medicine testMed = getMedicine(id);

        assertEquals("Med1", testMed.getName());
    }
    @Test
    public void testAddLegalMedicine(){
        HttpResponse response= addMedicine("Med1", "Liquid");
        assertEquals(HttpStatus.CREATED, response.getStatus());
    }
	@Test
	public void testAddMedicineUnauthorised(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/medicines", new MedicineAddCommand("name", "type")).header("X-API-Key",""));
        });
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/medicines", new MedicineAddCommand("name", "type")).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
        assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}

	@Test
	public void testAddMedicineNull(){
        HttpResponse response = addMedicine(null, null);
        int id =  getEId(response).intValue();
        Medicine testMed = getMedicine(id);
        assertEquals("Undefined", testMed.getType());
        assertEquals("Unnamed", testMed.getName());

        HttpResponse response1 = addMedicine("Med1", null);
        int id1 =  getEId(response1).intValue();
        Medicine testMed1 = getMedicine(id1);
        assertEquals("Undefined", testMed1.getType());

        HttpResponse response2 = addMedicine(null, "type");
        int id2 =  getEId(response2).intValue();
        Medicine testMed2 = getMedicine(id2);
        assertEquals("Unnamed", testMed2.getName());
	}

	


    @Test
    public void testDeleteAndGetMedicine(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        // Asserting that we've added a medicine
        assertEquals(HttpStatus.CREATED, response.getStatus());

        HttpRequest request = HttpRequest.DELETE("/medicines/"+id).header("X-API-Key",token);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/medicines/"+id));
        });
    }

	@Test
	public void testDeleteMedicinieUnauthorised(){
        int id =  getEId(addMedicine("name", "type")).intValue();
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/medicines/0").header("X-API-Key",""));
        });
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

	HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/medicines/0").header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
        assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}
	@Test
	public void testDeleteMedicineNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/medicines/-1").header("X-API-Key",token));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/medicines/").header("X-API-Key",token));
        });
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, thrown1.getStatus());
	}

    @Test
    public void testPutLegalMedicine(){
        HttpResponse response= addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        response = putMedicine(id, "NewName", "NewType");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

 @Test
    public void testUpdateMedicineEmptyType() {
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        putMedicine(id, "name", "");
        Medicine found = getMedicine(id);
        assertEquals("Undefined", found.getType());
    }
	
    @Test
    public void testUpdateMedicineEmptyName() {
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        client.toBlocking().exchange(HttpRequest.PUT("/medicines", new MedicineUpdateCommand(id, "", "type")).header("X-API-Key",token));
        Medicine found = getMedicine(id);
        assertEquals("Unnamed", found.getName());
    }
	@Test
	public void testUpdateMedicineNull(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();

        client.toBlocking().exchange(HttpRequest.PUT("/medicines", new MedicineUpdateCommand(id, null, "type")).header("X-API-Key",token));
        Medicine found = getMedicine(id);
        assertEquals("Unnamed", found.getName());

        client.toBlocking().exchange(HttpRequest.PUT("/medicines", new MedicineUpdateCommand(id, null, null)).header("X-API-Key",token));
        Medicine found1 = getMedicine(id);
        assertEquals("Unnamed", found1.getName());
        assertEquals("Undefined", found1.getType());

		client.toBlocking().exchange(HttpRequest.PUT("/medicines", new MedicineUpdateCommand(id, "name", null)).header("X-API-Key",token));
        Medicine found2 = getMedicine(id);
        assertEquals("Undefined", found2.getType());

	}
	@Test
	public void testUpdateMedicinieUnauthorised(){
        int id =  getEId(addMedicine("name", "type")).intValue();
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.PUT("/medicines", new MedicineUpdateCommand(0, "name", "type")).header("X-API-Key",""));
        });
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.PUT("/medicines", new MedicineUpdateCommand(0, "name", "type")).header("X-API-Key","SOmeVeryCo2rr45ECt231TokEN1"));
        });
        assertEquals(HttpStatus.UNAUTHORIZED, thrown1.getStatus());
	}
    @Test
    public void testAddAndUpdateMedicine(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();

        response = putMedicine(id, "newName", "newType");

        Medicine m = getMedicine(id);
        assertEquals("newName", m.getName());
        assertEquals("newType", m.getType());
    }

    protected HttpResponse putMedicine(int id, String name, String type){
        HttpRequest request = HttpRequest.PUT("/medicines", new MedicineUpdateCommand(id, name, type)).header("X-API-Key",token);
        return client.toBlocking().exchange(request);
    }

    protected HttpResponse addMedicine(String name, String type){
        HttpRequest request = HttpRequest.POST("/medicines", new MedicineAddCommand(name, type)).header("X-API-Key",token);
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }

    protected Medicine getMedicine(int id){
        HttpRequest request = HttpRequest.GET("/medicines/" + id);
        return client.toBlocking().retrieve(request, Medicine.class);

    }

    protected Long getEId(HttpResponse response) {
        String val = response.header(HttpHeaders.LOCATION);
        if (val != null) {
            int index = val.indexOf("/medicines/");
            if (index != -1) return Long.valueOf(val.substring(index + "/medicines/".length()));
            else return null;
        }
        else return null;
    }



}
