/**
 * This is the Controller class which handles interaction between the user (GUI) and
 * the logic of the program.
 *
 * @author Alexandra Härnström
 * @version 1
 */
package com.example.emailsender;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    private static String[] fields;
    private int emailCount;
    private static Object lock;
    private MailComposer mailComposer;

    public Controller() {
        fields = new String[7];
        lock = new Object();
        mailComposer = new MailComposer();
        Thread thread = new Thread(mailComposer);
        thread.start();
    }
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField textFieldFrom;
    @FXML
    private TextField textFieldTo;
    @FXML
    private TextField textFieldSubject;
    @FXML
    private TextArea textAreaMail;
    @FXML
    private Label labelSent;

    /**
     * This method retrieves user input from the GUI and the values are stored as a String array.
     * Using a lock, the logic class MailComposer is notified that it can now fetch the
     * array from the Controller.
     * A lock is used to prevent the MailComposer from running (as a separate thread) needlessly.
     */
    @FXML
    protected void sendBtn() {
        synchronized(lock) {
            //fields[0] = textFieldServer.getText();
            //This program was made specifically using GMail
            fields[0] = "smtp.gmail.com";
            fields[1] = textFieldUsername.getText();
            fields[2] = passwordField.getText();
            fields[3] = textFieldFrom.getText();
            fields[4] = textFieldTo.getText();
            fields[5] = textFieldSubject.getText();
            fields[6] = textAreaMail.getText();

            labelSent.setText("E-mails sent: " + ++emailCount);

            lock.notify();
        }
    }

    /**
     * This method returns the values of the user input
     * @return
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * This method returns the lock
     * @return - The Controller's lock object
     */
    public Object getLock() {
        return lock;
    }

    /**
     * This method stops MailComposer from running as a thread.
     */
    public void exit() {
        mailComposer.stopThread();
    }
}