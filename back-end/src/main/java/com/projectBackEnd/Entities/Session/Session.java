package main.java.com.projectBackEnd.Entities.Session;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;
import java.nio.charset.Charset;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Random;
import java.sql.Timestamp;

import java.io.Serializable;
@Entity
@Table(name = Session.TABLENAME)
public class Session implements TableEntity{
	public final static String TABLENAME = "Sessions";
	private final static String TOKEN = "Token";
	private final static String DATE = "Date";
	private final static String TIMEOUT = "Timeout";
	private final static String USERNAME = "Username";
	@Id
    	@Column(name = TOKEN)
	private String token;
	@Column(name = DATE)
	private Timestamp date;
	@Column(name = TIMEOUT)
	private Timestamp timeout;
	@Column(name = USERNAME)
	private String username;
	/**
	*	timeout in sec
	*/
	public Session(String username, int timeout){
		this.username = username;
		this.date = new Timestamp(System.currentTimeMillis());
		this.timeout = new Timestamp(System.currentTimeMillis() + timeout * 1000);
		this.token = generateToken();
	}
	public Session(){};
    	public Serializable getPrimaryKey(){
		return token;
	}
	private String generateToken(){
		Random rand = new Random();
		String s = null;
		do{
			byte[] bytes = new byte[256];
			rand.nextBytes(bytes);
			s =  new String(bytes,Charset.forName("UTF-8"));
		}while(s == null && SessionManager.getSessionManager().verifySession(s));	
		return s;
	}
	public String getToken(){
		return token;	
	}
	public Timestamp getDate(){
		return date;
	}
	public Timestamp getTimeout(){
		return timeout;
	}
	public String getUsername(){
		return username;
	}

    	public TableEntity copy(TableEntity newCopy){
		if(newCopy instanceof Session){		
			token = ((Session)newCopy).getToken();
			date = ((Session)newCopy).getDate();
			timeout = ((Session)newCopy).getTimeout();
			username = ((Session)newCopy).getUsername();
			return this;
		}
		else
			return null;
		
	}
	

	
} 
