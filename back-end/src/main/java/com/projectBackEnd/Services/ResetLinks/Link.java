package main.java.com.projectBackEnd.Services.ResetLinks;
import main.java.com.projectBackEnd.TableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Random;


/**
 * Link defines methods for getting token and PrimaryKey from the database.
 * This class implements TableEntity.
 *
 */
@Entity
@Table(name = Link.TABLENAME)
public class Link implements TableEntity<Link> {
	
	// 'Links' database table name and attributes
	final static String TABLENAME = "Links";
	private final static String TOKEN = "token";
	private final static String EMAIL = "email";
	
	// The primary key token, used for authentication
	@Id @Column(name = Link.TOKEN)
	private String token;

	@Column(name = Link.EMAIL)
	private String email;
	
	/**
	 * Default constructor
	 */	
	public Link(){}
	
	/**
	 * Constructor for class Link
	 * @param email user's email
	 */
	Link(String email){
		this.email = email;
		token = generateToken();	
	}

	/**
	 * Get generated token
	 * @return generated token
	 */
	public String getToken(){
		return token;
	}

	/**
	 * Get email
	 * @return email
	 */
	public String getEmail(){
		return email;
	}

	/**
	 * Get PrimaryKey
	 * @return token(PrimaryKey of table Link)
	 */
	public Serializable getPrimaryKey(){
		return token;
	}


	/**
	 * Create a copy of TableEntity
	 * @param linkToCopy The TableEntity needs to copy
	 * @return the copied TableEntity (this Link)
	 *
	 */
    public Link copy(Link linkToCopy){
		token = linkToCopy.getToken();
		email = linkToCopy.getEmail();
		return this;
	}


	/**
	 * Generate a random Token (50 String Long)
	 * @return Token String
	 */
	private String generateToken(){
	Random rand = new Random();
	String s;
        String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
	do{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 50;++i){
			sb.append(alphaNum.charAt(rand.nextInt(alphaNum.length())));
		}
		s = sb.toString();
	}while(s == null || ResetLinkManager.getResetLinkManager().exist(s));
		return s;
	}
}
