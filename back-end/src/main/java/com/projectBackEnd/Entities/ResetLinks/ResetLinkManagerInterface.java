package main.java.com.projectBackEnd.Entities.ResetLinks;

public interface ResetLinkManagerInterface{
	public void delete(String token);
	public String create(String email) throws EmailNotExistException;
	public String getEmail(String token);
	public boolean exist(String token);
}
