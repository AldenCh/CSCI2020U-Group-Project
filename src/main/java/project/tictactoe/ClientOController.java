package project.tictactoe;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import static java.lang.String.valueOf;

public class ClientOController{
    @FXML
    GridPane gridPane;

    public String getString(ActionEvent event) {
        Node node = (Node) event.getSource();
        ObservableList<Node> kids = gridPane.getChildren();
        for (Node kid : kids) {
            if (kid == node) {
                System.out.println("O " + gridPane.getRowIndex(kid)  + " " + gridPane.getColumnIndex(kid));
                return "O " + gridPane.getRowIndex(kid) + " " + gridPane.getColumnIndex(kid);
            }
        }
        return "error";
    }
}
