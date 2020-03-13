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
import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Medicine.*;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class MedicineControllerTest{

    @Inject
    @Client("/")
    HttpClient client;

    static MedicineManagerInterface medicineManager;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        medicineManager = MedicineManager.getMedicineManager();
    }

    @AfterAll
    public static void closeDatabase() {
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
        medicineManager.deleteAll();
    }

    @Test
    public void testAddAndGetAll(){
        ArrayList<Integer> ids = new ArrayList<>();
        HttpResponse response;
        for(int i=0;i<3;i++){
            response = addMedicine("ShouldBeDeleted", "Liquid");
            ids.add(getEId(response).intValue());
        }

        HttpRequest request = HttpRequest.GET("/medicine/list");
        List<Medicine> medicineList = client.toBlocking().retrieve(request, Argument.of(List.class, Medicine.class));
        for(int i=0; i<ids.size();i++){
            assertEquals(ids.get(i), medicineList.get(i).getPrimaryKey());
        }
    }

    @Test
    public void testAddLegalMedicine(){
        HttpResponse response= addMedicine("Med1", "Liquid");
        assertEquals(HttpStatus.CREATED, response.getStatus());
    }

    @Test
    public void testPutLegalMedicine(){
        HttpResponse response= addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        response = putMedicine(id, "NewName", "NewType");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    @Test
    public void testUpdateMedicineNullType(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/medicine", new MedicineUpdateCommand(id, "TestMed", "")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testUpdateMedicineNullName(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/medicine", new MedicineUpdateCommand(id, "", "Liquid")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testNonExistingMedicineReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/medicine/3524"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testDeleteAndGetMedicine(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();
        // Asserting that we've added a medicine
        assertEquals(HttpStatus.CREATED, response.getStatus());

        HttpRequest request = HttpRequest.DELETE("/medicine/"+id);
        response = client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/medicine/"+id));
        });
    }

    @Test
    public void testAddNullNameMedicine(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/medicine", new MedicineAddCommand("", "Liquid")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }


    @Test
    public void testAddNullTypeMedicine(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.POST("/medicine", new MedicineAddCommand("TestMed", "")));
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    public void testAddAndGetMedicine(){
        HttpResponse response = addMedicine("Med1", "Liquid");
        int id =  getEId(response).intValue();

        Medicine testMed = getMedicine(id);

        assertEquals("Med1", testMed.getName());
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
        HttpRequest request = HttpRequest.PUT("/medicine", new MedicineUpdateCommand(id, "newName", "newType"));
        return client.toBlocking().exchange(request);
    }

    protected HttpResponse addMedicine(String name, String type){
        HttpRequest request = HttpRequest.POST("/medicine", new MedicineAddCommand(name, type));
        HttpResponse response = client.toBlocking().exchange(request);
        return response;
    }

    protected Medicine getMedicine(int id){
        HttpRequest request = HttpRequest.GET("/medicine/" + id);
        return client.toBlocking().retrieve(request, Medicine.class);

    }

    protected Long getEId(HttpResponse response) {
        String val = response.header(HttpHeaders.LOCATION);
        if (val != null) {
            int index = val.indexOf("/medicine/");
            if (index != -1) {
                return Long.valueOf(val.substring(index + "/medicine/".length()));
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }



}
