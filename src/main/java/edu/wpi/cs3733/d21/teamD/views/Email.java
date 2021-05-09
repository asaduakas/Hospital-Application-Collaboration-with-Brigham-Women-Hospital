package edu.wpi.cs3733.d21.teamD.views; // MUST HAVE JAVAMAIL AND JAF IN CLASSPATH

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {

  public static void sendPassEmail(String to, boolean pass) {
    // RECIPIENT EMAIL
    // Note: while you DO need sender username and password you do NOT need recipients (obviously)
    //String to = "recipient email goes here";

    // SENDER EMAIL
    String from = "diamondDdragons@gmail.com";

    // SEND EMAIL USING GMAIL SERVERS
    String host = "smtp.gmail.com";

    // Get system properties
    Properties properties = System.getProperties();

    // SETUP : DO NOT TOUCH
    properties.setProperty("mail.smtp.host", host);
    properties.put("mail.smtp.socketFactory.port", "465"); // must be included, default 25
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.auth", "true"); // requires username and password (needed)
    properties.put("mail.smtp.port", "465"); // must be included, default 25

    // something something internet session who cares
    Session session =
        Session.getDefaultInstance(
            properties,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                // PUT SENDER USERNAME AND PASSWORD HERE
                return new PasswordAuthentication(
                    "admin", "admin");
              }
            });

    try {
      // DONT TOUCH THESE LINES
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

      if(pass){
        // THIS STRING IS THE SUBJECT
        message.setSubject("EMAIL CONFIRMATION");

        // THIS STRING IS THE BODY OF THE EMAIL
        message.setText("Go on peasant");
      }
      else{
        // THIS STRING IS THE SUBJECT
        message.setSubject("EMAIL CONFIRMATION");

        // THIS STRING IS THE BODY OF THE EMAIL
        message.setText("stay tf home");
      }

      // Send message
      Transport.send(message);

      // THIS STRING IS PRINTED TO THE CONSOLE (indicates success)
      System.out.println("Sent message successfully....");
    } catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }

}
