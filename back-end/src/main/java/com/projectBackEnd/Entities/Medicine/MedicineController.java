package main.java.com.projectBackEnd.Entities.Medicine;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/medicine")
public class MedicineController {

    protected final MedicineManagerInterface medicineManager = MedicineManager.getMedicineManager();

    MedicineController(){
        //this.medicineManager = medicineManager;
    }

    @Get(value = "/list", produces = MediaType.TEXT_JSON)
    public List<Medicine> list() {
        return medicineManager.getAllMedicines();
    }

    @Get(value = "/{id}", produces = MediaType.TEXT_JSON)
    public Medicine list(int id) {
        return medicineManager.getByPrimaryKey(id);
    }

    @Get("/")
    public String index(){
        return "This is our medicine index page";
    }


    @Post("/")
    public HttpResponse<Medicine> add(@Body MedicineAddCommand command) {
        Medicine med = medicineManager.addMedicine(command.getName(), command.getType());

        return HttpResponse
                .created(med)
                .headers(headers -> headers.location(location(med.getPrimaryKey())));
    }

    @Delete("/{id}")
    public HttpResponse delete(int id) {
        medicineManager.delete(id);
        return HttpResponse.noContent();
    }

    @Put("/")
    public HttpResponse update(@Body MedicineUpdateCommand command) {
        Medicine medObject = new Medicine(command.getId(), command.getName(), command.getType());
        medicineManager.update(medObject);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    protected URI location(int id) {
        return URI.create("/medicine/" + id);
    }

    protected URI location(Medicine medicine) {
        return location(medicine.getPrimaryKey());
    }

}
