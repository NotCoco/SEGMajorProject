package main.java.com.projectBackEnd.Entities.Medicine.Micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.net.URI;
import java.util.List;

import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.Medicine;
import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.MedicineManager;
import main.java.com.projectBackEnd.Entities.Medicine.Hibernate.MedicineManagerInterface;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

/**
 * Medicine Controller class is used for the interactions between frontend and backend
 * There are functionalites :
 *    - get all medicine
 *    - get a medicine
 *    - add a medicine
 *    - delete a medicine
 *    - update a medicine
 */
@Controller("/medicines")
public class MedicineController {

    protected final MedicineManagerInterface medicineManager = MedicineManager.getMedicineManager();

	protected final SessionManagerInterface sessionManager = SessionManager.getSessionManager();


    public MedicineController(){}

    /**
     * Get the specific medicine by http GET method
     * @param id
     * @return get the Medicine with the specified id
     */
    @Get(value = "/{id}", produces = MediaType.TEXT_JSON)
    public Medicine list(int id) {
        return medicineManager.getByPrimaryKey(id);
    }

    /**
     * Get all the medicines by http GET method
     * @return get a list of all the Medicines
     */
    @Get("/")
    public List<Medicine> index(){
        return medicineManager.getAllMedicines();
    }

    /**
     * Add a new medicine to the database with MedicineAddCommand by http POST method
     * @param session
     * @param command Dedicated MedicineAddCommand class to add new medicine
     * @return Http response with relevant information which depends on the result of
     * inserting the new medicine
     */
    @Post("/")
    public HttpResponse<Medicine> add(@Header("X-API-Key") String session,@Body MedicineAddCommand command) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        Medicine med = medicineManager.addMedicine(new Medicine(command.getName(), command.getType()));
        if(medicineManager.getByPrimaryKey(med.getPrimaryKey()) == null){
            return HttpResponse.serverError();
        }
        return HttpResponse
                .created(med)
                .headers(headers -> headers.location(location(med.getPrimaryKey())));
    }
    /**
     * Delete a medicine with specified id by http Delete method
     * @param session
     * @param id
     * @return Http response with relevant information which depends on the result of
     * the deleting medicine
     */
    @Delete("/{id}")
    public HttpResponse delete(@Header("X-API-Key") String session,int id) {
		if(!sessionManager.verifySession(session))
			return HttpResponse.unauthorized();
        medicineManager.delete(id);
        return HttpResponse.noContent();
    }
    /**
     * Update a medicine with MedicineUpdateCommand
     * @param session
     * @param command Dedicated MedicineUpdateCommand class to update the medicine
     * @return Http response with path
     */
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
    /**
     * Create URI with the specified id
     * @param id medicine id
     * @return created URI
     */
    protected URI location(int id) {
        return URI.create("/medicines/" + id);
    }

    /**
     * Create URI with existing medicine object
     * @param medicine medicine object
     * @return created URI
     */
    protected URI location(Medicine medicine) {
        return location(medicine.getPrimaryKey());
    }

}
