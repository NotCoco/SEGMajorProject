package main.java.com.projectBackEnd.Entities.Medicine;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

@Controller("/medicines")
public class MedicineController {

    protected final MedicineManagerInterface medicineManager = MedicineManager.getMedicineManager();

	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();


    public MedicineController(){}


    @Get(value = "/{id}", produces = MediaType.TEXT_JSON)
    public Medicine list(int id) {
        return medicineManager.getByPrimaryKey(id);
    }

    @Get("/")
    public List<Medicine> index(){
        return medicineManager.getAllMedicines();
    }


    @Post("/")
    public HttpResponse<Medicine> add(@Header("X-API-Key") String session,@Body MedicineAddCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Medicine med = medicineManager.addMedicine(command.getName(), command.getType());
        if(medicineManager.getByPrimaryKey(med.getPrimaryKey()) == null){
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(med)
                .headers(headers -> headers.location(location(med.getPrimaryKey())));
    }

    @Delete("/{id}")
    public HttpResponse delete(@Header("X-API-Key") String session,int id) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        medicineManager.delete(id);
        return HttpResponse.noContent();
    }

    @Put("/")
    public HttpResponse update(@Header("X-API-Key") String session,@Body MedicineUpdateCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Medicine medObject = new Medicine(command.getId(), command.getName(), command.getType());
        medicineManager.update(medObject);

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    protected URI location(int id) {
        return URI.create("/medicines/" + id);
    }

    protected URI location(Medicine medicine) {
        return location(medicine.getPrimaryKey());
    }

}
