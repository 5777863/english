package my.english.services;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.logging.log4j.*;

class MyAuthenticator extends Authenticator {
	private String user;
	private String password;

	MyAuthenticator() {

	}

	MyAuthenticator(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		String user = this.user;
		String password = this.password;
		return new PasswordAuthentication(user, password);

	}
}

public class mailSend {
	public void send(String adress, String messageTo, String subj) throws AddressException, MessagingException {
		// Recipient's email ID needs to be mentioned.
		// String to = "5777863@mail.ru";
		// Sender's email ID needs to be mentioned
		// String from = "www.testingwords.ru@mail.ru";
		String from = "www.testingwords.ru@mail.ru";

		// Assuming you are sending email from localhost
		// String host = "localhost";
		// Get system properties
		// Properties properties = System.getProperties();
		Properties properties = new Properties();
		Authenticator auth = new MyAuthenticator(from, "*****");
		// Authenticator auth = new MyAuthenticator("testingwords@mail.ru",
		// "999999qwer");
		// Setup mail server
		System.out.println(0);
		properties.put("mail.smtp.host", "smtp.mail.ru");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.mime.charset", "UTF-8");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		// properties.put("mail.smtp.starttls.enable", "true");

		/*
		 * properties.setProperty("mail.smtp.host", "smtp.mail.ru");
		 * properties.setProperty("mail.smtp.port", "465");
		 * properties.setProperty("mail.smtp.socketFactory.port", "465");
		 * properties.setProperty("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * properties.setProperty("mail.mime.charset", "UTF-8");
		 * properties.setProperty("mail.smtp.auth", "true");
		 */
		System.out.println(1);

		System.out.println(3);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties, auth);
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(adress));
		// Set Subject: header field
		message.setSubject(subj);
		// Now set the actual message
		message.setContent(messageTo, "text/html;charset=UTF-8");
		message.setDescription("utf-8");
		// Send message
		System.out.println(4);
		Transport.send(message);

		System.out.println("Sent message successfully....");

	}
}