package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Summaries;
import com.newsaggregator.db.Topics;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class SendEmailRunnable implements Runnable {

    @Override
    public void run() {

        MongoDatabase db = Utils.getDatabase();
        Articles articleManager = new Articles(db);
        Topics topicManager = new Topics(db);
        Summaries summariesManager = new Summaries(db);

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
            message.setText("Hello, it's time for the latest database status report. \n\n There are currently " + articleManager.count() + " articles in the database. \n\n There are currently " + topicManager.count() + " topics in the database. \n\n There are currently " + summariesManager.count() + " summaries in the database.\n\n There are currently " + articleManager.getUnlabelledArticles().size() + " unlabelled articles, " + topicManager.getClusteringTopics().size() + " unclustered topics, and " + summariesManager.getUnsummarisedClusters().size() + " unsummarised clusters.");

            // Send message
            Transport.send(message);

            Path file = Paths.get("timestamp.txt");
            Files.write(file, Lists.newArrayList(String.valueOf(articleManager.count())), Charset.forName("UTF-8"));

        } catch (Exception mex) {
            mex.printStackTrace();
            Utils.sendExceptionEmail(mex);
        }

    }

}
