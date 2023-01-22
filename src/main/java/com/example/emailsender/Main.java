/**
 * This is the Main class which loads the GUI (fxml file) and associated Controller class.
 *
 * @author Alexandra Härnström
 * @version 1
 */

package com.example.emailsender;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("E-mail Sender");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            Controller controller = fxmlLoader.getController();
            controller.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}