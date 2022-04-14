package project.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

/***
 * Controller for the menu screen, provides functionality for the exit and start game buttons
 */
public class ServerController {

    @FXML
    /***
     * When the start game button is clicked Start() is called, changing the scene to the gameboard
     */
    private void Start() throws IOException {
        SceneController.switchTo(window.Gameboard);
        System.out.println("Clicked on Start Game button");
    }

    @FXML
    /***
     * When the exit button is clicked Exit() is called, closing all windows
     */
    private void Exit() throws IOException {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }
}