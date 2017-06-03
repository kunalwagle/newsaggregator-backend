package com.newsaggregator.server.jobs;

import com.newsaggregator.Utils;
import com.newsaggregator.base.DigestHolder;
import com.newsaggregator.base.User;
import com.newsaggregator.db.Digests;
import com.newsaggregator.db.Users;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 17/05/2017.
 */
public class DigestRunnable implements Runnable {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void run() {

        logger.info("Starting digests");

        Users userManager = new Users(Utils.getDatabase());
        Digests digestsManager = new Digests(Utils.getDatabase());

        List<User> users = userManager.getAllUsers();

        List<DigestHolder> digestHolders;

        try {

            logger.info("It's time");

            digestHolders = users.stream().map(u -> new DigestHolder(u, true)).filter(d -> d.getTopicCount() > 0).collect(Collectors.toList());

            logger.info("Number of digests is " + digestHolders.size());

            digestsManager.saveDigests(digestHolders);

        } catch (Exception e) {
            logger.error("Unable to save digests", e);
            return;
        }

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

        for (DigestHolder digestHolder : digestHolders) {
            try {

                String link = Utils.getWebAddress() + "/digest/" + digestHolder.getId();

                HtmlEmail message = new HtmlEmail();
                message.setFrom(username, "NewSumm");
                message.setAuthentication(username, password);
                message.addTo(digestHolder.getEmailAddress());
                message.setSubject("Your NewSumm daily digest");
                message.setSSLOnConnect(true);
                message.setHostName("smtp.gmail.com");
                message.setSmtpPort(587);

                URL url = new URL(link);
                String cid = message.embed(url, "Digest");

                File img = new File("./LogoFullForm.png");
                StringBuffer msg = new StringBuffer();
                msg.append("<html><head><style> ");
                msg.append("body {background-color : #28546C} ");
                msg.append("h1 {color: white; text-align:center;} ");
                msg.append("input {background-color: #6F90A2;\n" +
                        "    border: 1px solid #6F90A2;\n" +
                        "    color: white;\n" +
                        "    border-radius: 5px;\n" +
                        "    height: 50px;\n" +
                        "    width: 100px;\n" +
                        "    margin: auto;\n" +
                        "    display: block;} ");
                msg.append("</style></head><body>");
                msg.append("<img src=cid:").append(message.embed(img)).append("><br><br>");
                msg.append("<h1>Your daily digest is ready.</h1>");
                msg.append("<form action=").append(link).append(">\n" +
                        "    <input type=\"submit\" value=\"View Now\" />\n" +
                        "</form>");
                msg.append("</body></html>");

                message.setHtmlMsg(msg.toString());

                message.setTextMsg("Your daily digest is ready. Navigate to " + link + " to view it");

                String id = message.send();

            } catch (Exception e) {
                logger.error("Exception sending email digest", e);
            }
        }


    }
}
