package project.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/***
 * The class associated with the main window
 */
public class GameApplication extends Application {
    /***
     * The function to call the creation of the main window at the beginning of the program
     *
     * @param stage The window to be displayed
     * @throws IOException To display any Input or Output Exceptions that may occur during
     *                     the loading of the associated FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        new ServerWindow();
    }

    /***
     * The class that creates the main window used by the Gameboard, Start Page, and End Page.
     *
     * @see GameboardController
     * @see ServerController
     * @see EndController
     */
    public class ServerWindow extends Stage{
        public ServerWindow() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("server.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 315, 375);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setTitle("Board");
            this.setScene(scene);
            SceneController.setScene(scene);
            SceneController.switchTo(window.Server);
            this.show();
        }
    }

    /***
     * The function to launch our JavaFX application
     *
     * @see Application
     */
    public static void main(String[] args) {
        launch();
    }
}