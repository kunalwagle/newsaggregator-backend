package com.newsaggregator;


import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.Outlet;
import com.newsaggregator.server.Connection;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
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

    public static void sendServerRestartEmail(int port) {

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
            message.setSubject("Restarted server");
            message.setText("Hello, the server has been restarted on port " + port);

            // Send message
            Transport.send(message);
            System.out.println("Message sent successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        MongoClient client = Connection.MONGO.getClient();
        return client.getDatabase("NewsAggregator");
    }

    public static List<String> allSources() {
        ArrayList<String> list = Lists.newArrayList();
        for (Outlet outlet : Outlet.values()) {
            list.add(outlet.getSourceString());
        }
        return list;
    }

    public static String getWebAddress() {
        return "http://kunalnewsaggregator.co.uk";
    }

    public static void printActiveThreads() {
        int nbRunning = 0;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.RUNNABLE) nbRunning++;
        }
        Logger.getLogger(Utils.class).info("Number of active threads is " + nbRunning);
    }



}
