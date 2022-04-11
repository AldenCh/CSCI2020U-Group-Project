package project.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new OWindow();
        new XWindow();
        new ServerWindow();
    }

    public class OWindow extends Stage{
        public OWindow() {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("clientO.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 320, 240);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setTitle("Player O");
            this.setScene(scene);
            this.show();
        }
    }

    public class XWindow extends Stage{
        public XWindow() {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("clientX.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 320, 240);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setTitle("Player X");
            this.setScene(scene);
            this.show();
        }
    }

    public class ServerWindow extends Stage{
        public ServerWindow() {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("clientX.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 320, 240);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setTitle("Board");
            this.setScene(scene);
            this.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}