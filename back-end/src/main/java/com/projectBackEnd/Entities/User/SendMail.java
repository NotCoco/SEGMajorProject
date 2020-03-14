package main.java.com.projectBackEnd.Entities.User;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 *
 * This is the SendMail class.
 * It will send a email to the end user`s address while signing up.
 *
 */
public class SendMail

{
    /**
     * This method will send a email to a specific email account.
     * The addresser account is my personal account.
     * Only in this way I can get authentication code.
     *
     */
    public static boolean send(String to, String title, String content) {

        String addresser = "476070991@qq.com";
        String authentication = "bxnxbljhsskqcaji";

        // get the system properties and set up the host.
        //use simple mail transfer protocol.
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.qq.com");// qq mail server
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory MailSocket;
        try {
            MailSocket = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) { return false;}
        MailSocket.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", MailSocket);
        //get a default session.
        //including the user name and the authentication code.
        Session session = Session.getDefaultInstance(properties, new Authenticator()
        {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(addresser, authentication);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);// create a mimeMessage to contain the session.
            message.setFrom(new InternetAddress(addresser, "Team Team", "UTF-8"));//use utf-8
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(title, "UTF-8");//set the title  (use utf-8)
            message.setContent(content, "text/html;charset=UTF-8");////set the content fo mail (use utf-8)
            message.setSentDate(new Date());
            Transport.send(message);//send message
        }
        catch (MessagingException | UnsupportedEncodingException mex) { return false; }
        return true;
    }

}