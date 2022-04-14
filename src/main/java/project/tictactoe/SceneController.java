package project.tictactoe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

/***
 * SceneController uses the enum window to swap between the different scenes
 */
public class SceneController{
    private static Scene scene;

    /***
     * sets the current scene to the inputted scene
     * @param scene inputted scene
     */
    public static void setScene(Scene scene) {
        SceneController.scene = scene;
    }

    /***
     * // switches the window depending on the input
     * @param view the window to switch to
     */
    public static void switchTo(window view) throws IOException{
        Parent root;
        root = FXMLLoader.load(SceneController.class.getResource(view.getFileName()));
        scene.setRoot(root);
    }
}