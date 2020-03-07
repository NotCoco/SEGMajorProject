package main.java.com.projectBackEnd.Entities.Medicine;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/medicine")
public class MedicineController {

//    protected final MedicineManagerInterface medicineManager;
//
//    MedicineController(MedicineManagerInterface medicineManager){
//        this.medicineManager = medicineManager;
//    }
//
//    @Get(value = "/list", produces = MediaType.TEXT_JSON)
//    public List<Medicine> list() {
//        return medicineManager.getAllMedicines();
//    }

    @Get("/")
    public String index(){
        return "Hi test string";
    }

//    @Post("/")
//    public HttpResponse<Medicine> add(@Body @Valid MedicineAddCommand command) {
//        Medicine med = medicineManager.addMedicine(command.getName(), command.getType());
//
//        return HttpResponse
//                .created(med)
//                .headers(headers -> headers.location(location(med.getPrimaryKey())));
//    }
//
//    @Put("/")
//    public HttpResponse update(@Body @Valid MedicineUpdateCommand command) {
//        Medicine medObject = new Medicine(command.getId(), command.getName(), command.getType());
//        medicineManager.update(medObject);
//
//        return HttpResponse
//                .noContent()
//                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
//    }
//
//    protected URI location(int id) {
//        return URI.create("/medicine/" + id);
//    }
//
//    protected URI location(Medicine medicine) {
//        return location(medicine.getPrimaryKey());
//    }

}