package project.tictactoe;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientXController {
    @FXML
    GridPane gridPain;
    String location;

    public void getString(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        ObservableList<Node> kids = gridPain.getChildren();
        for (Node kid : kids) {
            if (kid == node) {
                location =  "X " + gridPain.getRowIndex(kid) + " " + gridPain.getColumnIndex(kid);
            }
        }
        Response r = new Response();
        Thread XThread = new Thread(r);
        XThread.start();
    }

    public class Response implements Runnable {
        Socket XSocket;

        @Override
        public void run() {
            try {
                XSocket = new Socket("localhost", 4998);
                XSocket.getOutputStream().flush();
                DataOutputStream out = new DataOutputStream(XSocket.getOutputStream());
                out.writeUTF(location);
                out.close();
                XSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
