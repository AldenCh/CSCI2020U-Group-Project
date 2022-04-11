package project.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class ServerController {
    @FXML
    private void Start() throws IOException {
        SceneController.switchTo(window.Gameboard);
        System.out.println("Clicked on Start Game button");
    }

    @FXML
    private void Exit() {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }
}