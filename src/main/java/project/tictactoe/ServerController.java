package project.tictactoe;

import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Set;

public class ServerController {

    @FXML
    private void Start() throws IOException {
        SceneController.switchTo(window.Gameboard);
        System.out.println("Clicked on Start Game button");
    }

    @FXML
    private void Exit() throws IOException {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }

}