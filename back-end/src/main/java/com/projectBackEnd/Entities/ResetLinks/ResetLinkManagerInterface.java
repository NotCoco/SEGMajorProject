package main.java.com.projectBackEnd.Entities.ResetLinks;


/**
 * The interface for all ResetLinkManagers
 */
public interface ResetLinkManagerInterface{
	public void delete(String token);
	public String create(String email) throws EmailNotExistException;
	public String getEmail(String token);
	public boolean exist(String token);
}
