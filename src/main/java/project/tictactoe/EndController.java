package project.tictactoe;

import javafx.fxml.FXML;
import java.io.IOException;

/***
 * Controller for the end screen, provides functionality for the exit and newgame buttons
 */
public class EndController {
    @FXML
    /***
     * When the exit button is clicked Exit() is called, closing all windows
     */
    private void Exit() {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }

    @FXML
    /***
     * When the new game button is clicked New() is called, switching back to the menu scene
     */
    public void New() throws IOException {
        SceneController.switchTo(window.Server);
        System.out.println("Clicked on Menu button");
    }
}