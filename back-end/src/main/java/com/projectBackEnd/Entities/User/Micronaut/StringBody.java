package main.java.com.projectBackEnd.Entities.User.Micronaut;


public class StringBody{
	private String string;
	public StringBody(){}
	public StringBody(String string){
		this.string = string;
	}
	public String getString(){
		return string;
	}
	public void setString(String string){
		this.string = string;
	}

}
