package com.newsaggregator.server.jobs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Articles;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class SendEmailRunnable implements Runnable {

    @Override
    public void run() {

        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
        Articles articleManager = new Articles(db);
        List<OutletArticle> articleList = articleManager.getAllArticles();

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
            message.setSubject("News Aggregator Status Report");
            message.setText("Hello, it's time for the latest database status report. \n\n There are currently " + articleList.size() + " articles in the database");

            // Send message
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}
