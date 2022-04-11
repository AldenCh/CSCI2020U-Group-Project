package project.tictactoe;

import javafx.fxml.FXML;

import java.io.IOException;

public class EndController {
    @FXML
    private void Exit() {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }

    @FXML
    public void New() throws IOException {
        SceneController.switchTo(window.Server);
        System.out.println("Clicked on Menu button");
    }
}