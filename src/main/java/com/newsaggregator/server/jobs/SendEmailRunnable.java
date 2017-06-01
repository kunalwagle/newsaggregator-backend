package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Summaries;
import org.apache.log4j.Logger;

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
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/04/2017.
 */
public class SendEmailRunnable implements Runnable {

    private List<String> labelStrings;
    private int clusters = 0;

    public SendEmailRunnable() {

    }

    public SendEmailRunnable(List<String> labelStrings, int clusters) {
        this.labelStrings = labelStrings.stream().limit(clusters).collect(Collectors.toList());
        this.clusters = clusters;
    }

    @Override
    public void run() {

        MongoDatabase db = Utils.getDatabase();
        Summaries summariesManager = new Summaries(db);
        Articles articlesManager = new Articles(db);

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
            Logger.getLogger(getClass()).info("Starting email runnable");
            StringBuilder stringBuilder = new StringBuilder().append("Hello, it's time for the latest database status report. \n\n There are currently ").append(Utils.getActiveThreads()).append(" active threads.\n\n");
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            if (clusters == 0) {
                message.setSubject("NewSumm Status Update");
            } else {
                message.setSubject("NewSumm Clustering Complete");
                stringBuilder.append("We also just completed one set of clusters \n\n");
                stringBuilder.append("This resulted in ").append(clusters).append(" topics being clustered\n\n. Topics include:\n");
                for (String labelString : labelStrings) {
                    stringBuilder.append(labelString).append("\n");
                }
                stringBuilder.append("\n");
            }

            stringBuilder.append("There are now ").append(summariesManager.count() - summariesManager.unsummarisedCount()).append(" summaries in the database\n\n");
            stringBuilder.append("There are now ").append(articlesManager.unlabelledCount()).append("unlabelled articles in the database, out of a total of").append(articlesManager.count()).append("\n\n");

            stringBuilder.append("Thanks. That's all for now.");

            message.setText(stringBuilder.toString());


            // Send message
            Transport.send(message);
            Logger.getLogger(getClass()).info("Sending email runnable");

            Path file = Paths.get("./timestamp.txt");
            Files.write(file, Lists.newArrayList(String.valueOf(summariesManager.count())), Charset.forName("UTF-8"));

        } catch (Exception mex) {
            mex.printStackTrace();
            Utils.sendExceptionEmail(mex);
        }

    }

}
