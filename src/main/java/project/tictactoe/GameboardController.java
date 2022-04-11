package project.tictactoe;

import javafx.fxml.FXML;

import java.io.IOException;

public class GameboardController {
    @FXML
    private void Exit() {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }

    @FXML
    public void End() throws IOException {
        SceneController.switchTo(window.End);
        System.out.println("Clicked on end button");
    }
}