package project.tictactoe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class SceneController{
    private static Scene scene;

    public static void setScene(Scene scene) {
        SceneController.scene = scene;
    }
    // switches the window depending on the input
    public static void switchTo(window view) throws IOException{
        Parent root;
        root = FXMLLoader.load(SceneController.class.getResource(view.getFileName()));
        scene.setRoot(root);
    }
}