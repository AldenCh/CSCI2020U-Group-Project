package project.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class EndController {
    @FXML
    Label label;
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

    public void TieGame() {
        label.setText("Tie Game!");
    }

    public void XWon() {
        label.setText("X Won!");
    }

    public void OWon() {
        label.setText("O Won!");
    }
}