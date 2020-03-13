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
        HttpRequest request = HttpRequest.POST("/medicine", new MedicineAddCommand("ShouldBeDeleted", "Liquid"));
        HttpResponse response = client.toBlocking().exchange(request);
        ids.add(getEId(response).intValue());
        request = HttpRequest.POST("/medicine", new MedicineAddCommand("ShouldBeDeleted", "Liquid"));
        response = client.toBlocking().exchange(request);
        ids.add(getEId(response).intValue());
        request = HttpRequest.POST("/medicine", new MedicineAddCommand("ShouldBeDeleted", "Liquid"));
        response = client.toBlocking().exchange(request);
        ids.add(getEId(response).intValue());
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.GET("/medicine/list");
        List<Medicine> medicineList = client.toBlocking().retrieve(request, Argument.of(List.class, Medicine.class));
        for(int i=0; i<ids.size();i++){
            assertEquals(ids.get(i), medicineList.get(i).getPrimaryKey());
        }
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
        HttpRequest request = HttpRequest.POST("/medicine", new MedicineAddCommand("ShouldBeDeleted", "Liquid"));
        HttpResponse response = client.toBlocking().exchange(request);
        Long id = getEId(response);
        // Asserting that we've added a medicine
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.DELETE("/medicine/"+id);
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
        HttpRequest request = HttpRequest.POST("/medicine", new MedicineAddCommand("Med1", "Liquid"));
        HttpResponse response = client.toBlocking().exchange(request);
        Long id = getEId(response);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.GET("/medicine/"+id);

        Medicine testMed = client.toBlocking().retrieve(request, Medicine.class);

        assertEquals("Med1", testMed.getName());
    }

    @Test
    public void testAddAndUpdateMedicine(){
        HttpRequest request = HttpRequest.POST("/medicine", new MedicineAddCommand("Med1", "Liquid"));
        HttpResponse response = client.toBlocking().exchange(request);
        int id =  getEId(response).intValue();

        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.PUT("/medicine", new MedicineUpdateCommand(id, "newName", "newType"));
        response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/medicine/" + id);
        Medicine m = client.toBlocking().retrieve(request, Medicine.class);
        assertEquals("newName", m.getName());
        assertEquals("newType", m.getType());
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
