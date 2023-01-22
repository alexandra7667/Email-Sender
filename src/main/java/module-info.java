module com.example.emailsender_3_2_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;


    opens com.example.emailsender_3_2_1 to javafx.fxml;
    exports com.example.emailsender_3_2_1;
}