package main.java.com.projectBackEnd.Entities.ResetLinks;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import java.util.List;
public class ResetLinkManager extends EntityManager implements ResetLinkManagerInterface{
	private static ResetLinkManagerInterface resetLinkManager;
	private ResetLinkManager(){
		super();
		setSubclass(Link.class);
		HibernateUtility.addAnnotation(Link.class);
		resetLinkManager = this;
	}
	public static ResetLinkManagerInterface getResetLinkManager(){
		if(resetLinkManager != null)
			return resetLinkManager;
		
		else
			return new ResetLinkManager();
	}

	public String create(String email) throws EmailNotExistException{
		if(UserManager.getUserManager().verifyEmail(email)){
			Link reset = new Link(email);
			insertTuple(reset);
			return reset.getToken();
		}
		else
			throw new EmailNotExistException("there is no user with email: " + email);
	}
	public String getEmail(String token){
		List<Link> links = getAll();
		for(Link l: links){
			if(l.getToken().equals(token)){
				return l.getEmail();
			}
		}
		return null;
	}
	public void delete(String token){
		List<Link> links = getAll();
		for(Link l: links){
			if(l.getToken().equals(token)){
				delete(l);
				break;
			}
		}

	}
	public boolean exist(String token){
		List<Link> links = getAll();
		boolean found = false;
		for(Link l: links){
			if(l.getToken().equals(token)){
				return true;
			}
		}
		return false;
	}

}
