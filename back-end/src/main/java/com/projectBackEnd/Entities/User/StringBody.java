package main.java.com.projectBackEnd.Entities.User;


public class StringBody implements StringBodyInterface{
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
