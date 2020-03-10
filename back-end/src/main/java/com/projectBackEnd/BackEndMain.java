package main.java.com.projectBackEnd;

import main.java.com.projectBackEnd.Entities.User.SendMail;

public class BackEndMain {

	
	public static void main(String []args){
        /**
         * @para
         * to: receiver`s Email address
         * title: email title
         * content: email content
         */
		SendMail.send("AlbertG2001@outlook.com","Title","Hello Hello!");
	}




}
