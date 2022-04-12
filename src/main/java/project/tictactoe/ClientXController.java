package project.tictactoe;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class ClientXController {
    @FXML
    GridPane gridPain;
    public String getString(ActionEvent event) {


        Node node = (Node) event.getSource();
        ObservableList<Node> kids = gridPain.getChildren();
        for (Node kid : kids) {
            if (kid == node) {
                System.out.println("X " + gridPain.getRowIndex(kid)  + " " + gridPain.getColumnIndex(kid));
                return "X " + gridPain.getRowIndex(kid) + " " + gridPain.getColumnIndex(kid);
            }
        }
        return "error";
    }
}
