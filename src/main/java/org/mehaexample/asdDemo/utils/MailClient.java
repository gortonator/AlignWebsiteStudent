package org.mehaexample.asdDemo.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailClient
{
	private static final String EMAIL = "te97399@gmail.com"; 
	private static final String PASS = "Test#786"; 

	public static void sendPasswordResetEmail(String toEmail, String registrationKey) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); 
		props.put("mail.smtp.port", "587"); 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.starttls.enable", "true"); 

		//create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL, PASS);
			}
		};
		Session session = Session.getInstance(props, auth);

		String resetLink = "http://localhost:8080/alignWebsite/webapi/student-facing/password-create";
		sendEmail(session, toEmail,"Password Reset Email Northeastern Account", 
				"Hello! \n \n" +
						"Your Registration key is: " + registrationKey + "\n" +
						"Password Reset link: " + resetLink + "\n" +
						"Thanks," + "\n" + 
				"Neu Team");
	}



	public static void sendRegistrationEmail(String toEmail, String registrationKey) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

		//create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL, PASS);
			}
		};
		Session session = Session.getInstance(props, auth);

		String resetLink = "http://localhost:8080/alignWebsite/webapi/student-facing/password-create";
		sendEmail(session, toEmail,"Registration Email Northeastern Account",
				"Hello! \n \n" +
						"Your Registration key is: " + registrationKey + "\n" +
						"Registeration link: " + resetLink + "\n" +
						"Thanks," + "\n" + 
				"Neu Team");
	}

	private static boolean sendEmail(Session session, String toEmail, String subject, String body){
		try
		{
			MimeMessage msg = new MimeMessage(session);

			msg.setSubject(subject);

			msg.setText(body);

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			Transport.send(msg);
			
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}


