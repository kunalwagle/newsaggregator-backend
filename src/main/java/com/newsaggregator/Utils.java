package com.newsaggregator;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by kunalwagle on 05/04/2017.
 */
public class Utils {

    public static int numberOfTimesACharacterOccursInAString(String haystack, Character needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    public static void sendExceptionEmail(Exception e) {

        String to = "kmw13@ic.ac.uk";

        final String username = "kunalnewsaggregator@gmail.com";
        final String password = "imperial";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Exception occurred in News Aggregator");
            message.setText("Hello, a new exception occurred. \n\n Restarting server.\n\n Error:\n" + e.toString() + "\n\nError stack trace:\n" + e.getMessage());

            // Send message
            Transport.send(message);
            System.out.println("Message sent successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        MongoClient mongoClient = new MongoClient("178.62.27.53", 27017);
        return mongoClient.getDatabase("NewsAggregator");
    }



}
