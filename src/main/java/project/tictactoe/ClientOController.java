package project.tictactoe;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/***
 * The class responsible for the functionality of the O controller
 */
public class ClientOController{
    @FXML
    GridPane gridPane;
    String location;

    /***
     * The function called by the O controller buttons
     * which stores the associated coordinates of that button
     *
     * @param event The event associated to the press of one the O controller buttons
     */
    public void getString(ActionEvent event) {

        Node node = (Node) event.getSource();
        ObservableList<Node> kids = gridPane.getChildren();
        for (Node kid : kids) {
            if (kid == node) {
                location =  "O " + gridPane.getRowIndex(kid) + " " + gridPane.getColumnIndex(kid);
            }
        }
        Response r = new Response();
        Thread OThread = new Thread(r);
        OThread.start();
    }

    /***
     * The thread created to handle sending server messages
     * regarding button presses
     */
    public class Response implements Runnable {
        Socket OSocket;

        @Override
        public void run() {
            try {
                OSocket= new Socket("localhost", 4999);
                OSocket.getOutputStream().flush();
                DataOutputStream out = new DataOutputStream(OSocket.getOutputStream());
                out.writeUTF(location);
                out.close();
                OSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
