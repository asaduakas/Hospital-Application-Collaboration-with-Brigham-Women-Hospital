package edu.wpi.cs3733.d21.teamD.views; // MUST HAVE JAVAMAIL AND JAF IN CLASSPATH

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {

  public static void sendPassEmail(String to, boolean pass) {
    // RECIPIENT EMAIL
    // Note: while you DO need sender username and password you do NOT need recipients (obviously)
    // String to = "recipient email goes here";

    // SENDER EMAIL
    String from = "diamondDdragons@gmail.com";

    // SEND EMAIL USING GMAIL SERVERS
    String host = "smtp.gmail.com";

    // Get system properties
    Properties props = System.getProperties();

    // SETUP : DO NOT TOUCH
    props.setProperty("mail.transport.protocol", "smtp");
    props.setProperty("mail.host", "smtp.gmail.com");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    props.put("mail.debug", "true");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");

    // something something internet session who cares
    Session session =
        Session.getDefaultInstance(
            props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                // PUT SENDER USERNAME AND PASSWORD HERE
                return new PasswordAuthentication("diamondddragons@gmail.com", "wpics3733d");
              }
            });

    try {
      // DONT TOUCH THESE LINES
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

      if (pass) {
        // THIS STRING IS THE SUBJECT
        message.setSubject("EMAIL CONFIRMATION");

        // THIS STRING IS THE BODY OF THE EMAIL
        message.setText("Go on peasant");
      } else {
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
