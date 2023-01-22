/**
 * This is the Launcher class which launches the program.
 * Since Main extends Application is cannot be used as a launcher in a program
 * with JavaFX and Scene Builder (JDK 18.0.1).
 *
 * @author Alexandra Härnström
 * @version 1
 */
package com.example.emailsender;

public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
