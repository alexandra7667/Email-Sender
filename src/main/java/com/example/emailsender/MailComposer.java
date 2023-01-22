/**
 * This is the MailComposer which handles the logic of the program.
 * It runs as a separate thread but waits until the Controller wakes it up.
 * It composes an e-mail and sends it.
 *
 * @author Alexandra Härnström
 * @version 1
 */

package com.example.emailsender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailComposer implements Runnable{

    private Controller controller;
    private Properties properties;
    private volatile boolean alive;
    private static Object lock;

    public MailComposer() {
        controller = new Controller();
        lock = controller.getLock();
        properties = new Properties();
        alive = true;
    }

    /**
     * This method runs as a thread but waits until the Controller wakes it up.
     * It then retrieves the user input from the Controller as an array.
     * The array is sent to the sendMail() method.
     */
    @Override
    public void run() {

        while(alive) {
            synchronized(lock) {
                try {
                    lock.wait();
                    composeMail(controller.getFields());
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method composes an e-mail.
     * It sets the properties, creates a Session object, and a Message object from the user input.
     * It sends the e-mail on TLS port 587.
     * @param fields - The user input values
     */
    private void composeMail(String[] fields) {
        String server = fields[0];
        String username = fields[1];
        String password = fields[2];
        String mailFrom = fields[3];
        String mailTo = fields[4];
        String subject = fields[5];
        String body = fields[6];

        setProperties();

        Session session = createSession(username, password);

        Message message = createMessage(session, subject, body, mailTo, mailFrom);

        sendMail(session, server, username, password, message);
    }

    /**
     * This method sets the e-mail's properties
     */
    private void setProperties() {
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.connectiontimeout", "5000");
    }

    /**
     * This method creates a new Session by authenticating the password
     * @param username - The log-in user name
     * @param password - The log-in password
     * @return - A new Session object
     */
    private Session createSession(String username, String password) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        return session;
    }

    /**
     * This method creates a new MimeMessage object using user input.
     * @param session - The Session object authenticating the sender
     * @param subject - The e-mail's subject
     * @param body - The e-mail's body text
     * @param mailTo - The e-mail's recipient
     * @param mailFrom - The e-mail's respond-to e-mail address (N.B. not the same as sender)
     * @return - A new MimeMessage
     */
    private Message createMessage(Session session, String subject, String body, String mailTo, String mailFrom) {
        Message message = null;
        try {
            message = new MimeMessage(session);
            message.setSubject(subject);
            message.setText(body);
            Address recipientAddress = new InternetAddress(mailTo);
            message.setRecipient(Message.RecipientType.TO, recipientAddress);
            Address senderAddress = new InternetAddress(mailFrom);
            Address[] addresses = new Address[1];
            addresses[0] = senderAddress;
            message.setReplyTo(addresses);
            message.saveChanges();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * This method creates a Transport object to send the e-mail.
     * The e-mail is sent over TLS port 587 (GMail specified secure port).
     * @param session - The Session object authenticating the sender.
     * @param server - The Mail server (GMail smtp)
     * @param username - The sender's log-in user name
     * @param password - The sender's log-in password
     * @param message - The MimeMessage to be sent
     */
    private void sendMail(Session session, String server, String username, String password, Message message) {
        int TLSPort = 587;

        try {
            Transport transport = session.getTransport();
            transport.connect(server, TLSPort, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method stops the run method to run as a thread.
     */
    protected void stopThread() {
        alive = false;
    }
}
