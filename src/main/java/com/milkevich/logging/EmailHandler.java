package com.milkevich.logging;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by aksenov on 28.04.2016.
 */
public class EmailHandler extends Handler{

    private String sendTo;
    private LogManager logManager = LogManager.getLogManager();
    private final String user = "milkevich.logging@gmail.com";
    private final String password = "1Qaz2Wsx3Edc";

    public EmailHandler() {
        setSendTo(logManager.getStringProperty(getClass().getName() + ".sendTo", user));
        setFormatter(logManager.getFormatterProperty(getClass().getName() + ".formatter", new TextFormatter()));
    }

    public EmailHandler(String sendTo) {
        this.sendTo = sendTo;
        setFormatter(logManager.getFormatterProperty(getClass().getName() + ".formatter", new TextFormatter()));
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    @Override
    public void writeMessage(Message msg) {

        Formatter formatter = getFormatter();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        try {

            javax.mail.Message messageEmail = new MimeMessage(session);
            messageEmail.setFrom(new InternetAddress(user));

            String[] recipientsEmail = sendTo.split("\\s*(,|\\s)\\s*");

            for (String email: recipientsEmail) {
                messageEmail.setRecipients(javax.mail.Message.RecipientType.TO,
                        InternetAddress.parse(email));
            }

            messageEmail.setSubject("Log info");
            if (formatter instanceof HTMLFormatter) {
                messageEmail.setContent(formatter.format(msg),"text/html");
            } else {
                messageEmail.setText(formatter.format(msg));
            }

            Transport.send(messageEmail);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
