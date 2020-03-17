package main.java.com.projectBackEnd.Entities.ResetLinks;
import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name = Link.TABLENAME)
public class Link implements TableEntity{
	public final static String TABLENAME = "Links";
	private final static String TOKEN = "token";
	private final static String EMAIL = "email";
	@Id @Column(name = Link.TOKEN)
	private String token;
	@Column(name = Link.EMAIL)
	private String email;
	public Link(){};
	public Link(String email){
		this.email = email;
		token = generateToken();	
	}
	public String getToken(){
		return token;
	}
	public String getEmail(){
		return email;
	}
	public Serializable getPrimaryKey(){
		return token;
	}
    	public TableEntity copy(TableEntity newCopy){
		if(newCopy instanceof Link){		
			token = ((Link)newCopy).getToken();
			email = ((Link)newCopy).getEmail();
			return this;
		}
		else
			return null;
		
	}
	private String generateToken(){
	Random rand = new Random();
	String s = null;
        String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
	do{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 50;++i){
			sb.append(alphaNum.charAt(rand.nextInt(alphaNum.length())));
		}
		s = sb.toString();
	}while(s == null && ResetLinkManager.getResetLinkManager().exist(s));
		return s;
	}
}
