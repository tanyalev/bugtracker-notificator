package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.domain;

import ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.models.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

class EmailSender {
    private static final Logger log = Logger.getLogger(EmailSender.class.getName());

    private String username;
    private String password;
    private Properties properties;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;

        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
    }

    public void send(Email email) {
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getSenderEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getReceiverEmail()));
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            Transport.send(message);
        } catch (MessagingException e) {
            log.severe(String.format("Error sending email: %s", e.getMessage()));
        }
    }
}