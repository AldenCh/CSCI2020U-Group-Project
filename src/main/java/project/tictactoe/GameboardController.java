package project.tictactoe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/***
 * The controller for the main gameboard which displays the game and takes input from the two clients (X,O)
 * The programs starts off with two canvas' one for displaying the game and one for displaying the turn
 * The program also has functionality for drawing an X or O in a specified row and column
 */
public class GameboardController {
    @FXML
    public Canvas canvas;
    @FXML
    public Canvas turnCanvas;
    private GraphicsContext gc;
    private GraphicsContext tgc;
    private String[][] board = {
            {"-", "-", "-"},
            {"-", "-", "-"},
            {"-", "-", "-"}
    };
    private boolean xTurn = true;
    private boolean running = true;
    private Thread OThread;
    private Thread XThread;
    private ServerSocket Oss;
    private ServerSocket Xss;
    private OWindow o;
    private XWindow x;

    @FXML
    private void initialize() throws IOException {
        // initialize player O's control window
        o = new OWindow();
        o.setX(1450);
        o.setY(350);
        o.setHeight(160);
        o.setWidth(140);
        o.setTitle("");

        // initialize player X's control window
        x = new XWindow();
        x.setX(970);
        x.setY(350);
        x.setHeight(160);
        x.setWidth(140);
        x.setTitle("");

        gc = canvas.getGraphicsContext2D();
        tgc = turnCanvas.getGraphicsContext2D();

        // draw the board and display the current turn as X
        DrawBoard(gc);
        tgc.setStroke(Color.RED);
        tgc.setLineWidth(5);
        tgc.strokeLine(5, 5, 35, 35);
        tgc.strokeLine(5, 35, 35, 5);

        Oss = new ServerSocket(4999);
        OServer oServer = new OServer(Oss);
        OThread = new Thread(oServer);
        OThread.start();

        Xss = new ServerSocket(4998);
        XServer xServer = new XServer(Xss);
        XThread = new Thread(xServer);
        XThread.start();
    }

    /***
     * Takes a GraphicsContext gc and draws the board using black lines
     * @param gc GraphicsContext of the gameboard canvas
     */
    private void DrawBoard(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        // draw border
        gc.strokeLine(5,5,5,305);
        gc.strokeLine(5,305,305,305);
        gc.strokeLine(305,305,305,5);
        gc.strokeLine(5,5,305,5);

        // draw grid pattern
        gc.strokeLine(105,5,105,305);
        gc.strokeLine(205,5,205,305);
        gc.strokeLine(5,105,305,105);
        gc.strokeLine(5,205,305,205);
    }

    /***
     * Draws an X on the board according to the row and column specified
     * @param gc GraphicsContext of the gameboard canvas
     * @param row Desired row to draw an X (0-2)
     * @param col Desired column to draw an X (0-2)
     */
    private void DrawX(GraphicsContext gc, double row, double col){
        // setup variables
        double xStart = (col - 1) * 100 + 25;
        double yStart = (row - 1) * 100 + 25;
        double xEnd = xStart + 60;
        double yEnd = yStart + 60;

        // draw the X
        gc.setStroke(Color.RED);
        gc.setLineWidth(25);
        gc.strokeLine(xStart, yStart, xEnd, yEnd);
        gc.strokeLine(xStart, yEnd, xEnd, yStart);
    }

    /***
     * Draws an O on the board according to the row and column specified
     * @param gc GraphicsContext of the gameboard canvas
     * @param row Desired row to draw an O (0-2)
     * @param col Desired column to draw an O (0-2)
     */
    private void DrawO(GraphicsContext gc, double row, double col){
        // setup variables
        double xStart = (col - 1) * 100 + 10;
        double yStart = (row - 1) * 100 + 10;

        // draw the O
        gc.setFill(Color.LIGHTGREEN);
        gc.fillOval(xStart,yStart,90,90);
        gc.setFill(Color.WHITE);
        gc.fillOval(xStart + 20,yStart + 20,50,50);
    }

    /***
     * Draws an O on the board according to the row and column specified
     *
     * @param board A 2x2 2D array that represents the state of the game board
     * @param symbol Either "X" or "O" to declare which side is checking for a win
     */
    private String checkWin(String[][] board, String symbol) {
        //row check
        for (int x = 0; x <= 2; x++) {
            int correct = 0;
            for (int y = 0; y <= 2; y++) {
                if (board[x][y].equals(symbol)) {
                    correct++;
                }
            }
            if (3 == correct) {
                return symbol;
            }
        }
        //column check
        for (int y = 0; y <= 2; y++) {
            int correct = 0;
            for (int x = 0; x <= 2; x++) {
                if (board[x][y].equals(symbol)) {
                    correct++;
                }
            }
            if (3 == correct) {
                return symbol;
            }
        }
        int diagCheck = 0;
        for (int x = 0; x <= 2; x++) {
            if (board[x][x].equals(symbol)) {
                diagCheck++;
            }
        }
        if (3 == diagCheck) {
            return symbol;
        }
        diagCheck = 0;
        int x = 2;
        for (int y = 0; y <= 2; y++, x--) {
            if (board[x][y].equals(symbol)) {
                diagCheck++;
            }
        }
        if (3 == diagCheck) {
            return symbol;
        }
        //tie check
        for (int z = 0; z <= 2; z++) {
            for (int y = 0; y <= 2; y++) {
                if (board[z][y].equals("-")) {
                    return "none";
                }
            }
        }
        return "tie";
    }

    @FXML
    /***
     * When the exit button is clicked Exit() is called, closing all windows.
     */
    private void Exit() {
        System.out.println("Clicked on Exit button");
        running = false;
        System.exit(0);
    }

    @FXML
    /***
     * Called when the game has resulted in a win or tie
     * Handles switching scenes, closing sockets, and stopping threads
     *
     * @param winner The winner of the game (X,O,tie)
     */
    public void End(String winner) throws IOException, InterruptedException {
        Thread.sleep(500);
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j] = "-";
            }
        }
        xTurn = true;

        Oss.close();
        Xss.close();
        Platform.runLater(
                () -> {
                    x.close();
                    o.close();
                    try {
                        // calls the fxml file of the winner, they all share the same EndController
                        if (winner.equals("X")) {
                            SceneController.switchTo(window.XEnd);
                        } else if (winner.equals("O")) {
                            SceneController.switchTo(window.OEnd);
                        } else {
                            SceneController.switchTo(window.TEnd);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        System.out.println("Clicked on end button");
    }

    public class OServer implements Runnable{
        ServerSocket Oss;
        Socket OSocket;
        DataInputStream in;

        public OServer(ServerSocket ss){
            Oss = ss;
        }

        @Override
        public void run() {
            try {
                while(running){
                    System.out.println("Waiting on connection");
                    OSocket = Oss.accept();
                    if (!xTurn){
                        in = new DataInputStream(OSocket.getInputStream());
                        System.out.println("Connected with O Client ");
                        String message = in.readUTF();
                        String position[] = message.split(" ");
                        if ("-" == board[Integer.parseInt(position[1])][Integer.parseInt(position[2])]) {
                            board[Integer.parseInt(position[1])][Integer.parseInt(position[2])] = position[0];
                            DrawO(gc, Integer.parseInt(position[1]) + 1, Integer.parseInt(position[2]) + 1);

                            // when O's turn ends, draw an X to tell the user that it is X's turn
                            tgc.clearRect(0, 0, turnCanvas.getWidth(), turnCanvas.getHeight());
                            tgc.setStroke(Color.RED);
                            tgc.setLineWidth(5);
                            tgc.strokeLine(5, 5, 35, 35);
                            tgc.strokeLine(5, 35, 35, 5);

                            String status = checkWin(board, position[0]);
                            if (status.equals("X") || status.equals("O") || status == "tie"){

                                OSocket.close();
                                End(status);
                                break;
                            }

                            xTurn = !xTurn;
                            //tgc.clearRect(0, 0, turnCanvas.getWidth(), turnCanvas.getHeight());
                        }
                    }
                }
                in.close();
                OSocket.close();
                Oss.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class XServer implements Runnable{
        ServerSocket Xss;
        Socket XSocket;
        DataInputStream in;

        public XServer(ServerSocket ss){
            Xss = ss;
        }

        @Override
        public void run() {
            try {
                while(running){
                    System.out.println("Waiting on connection");
                    XSocket = Xss.accept();
                    if (xTurn){

                        in = new DataInputStream(XSocket.getInputStream());
                        System.out.println("Connected with X Client ");
                        String message = in.readUTF();
                        String position[] = message.split(" ");
                        if ("-" == board[Integer.parseInt(position[1])][Integer.parseInt(position[2])]) {
                            board[Integer.parseInt(position[1])][Integer.parseInt(position[2])] = position[0];
                            DrawX(gc, Integer.parseInt(position[1]) + 1, Integer.parseInt(position[2]) + 1);

                            // when X's turn ends, draw an O to tell the user that it is O's turn
                            tgc.clearRect(0, 0, turnCanvas.getWidth(), turnCanvas.getHeight());
                            tgc.setFill(Color.LIGHTGREEN);
                            tgc.fillOval(0,0,40,40);
                            tgc.setFill(Color.WHITE);
                            tgc.fillOval(8,8,24,24);

                            String status = checkWin(board, position[0]);
                            if (status.equals("X") || status.equals("O") || status == "tie"){
                                XSocket.close();
                                End(status);
                                break;
                            }
                            xTurn = !xTurn;
                        }
                    }
                }
                in.close();
                XSocket.close();
                Xss.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class OWindow extends Stage {
        public OWindow() {
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("clientO.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 220, 125);
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
                scene = new Scene(fxmlLoader.load(), 220, 125);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setTitle("Player X");
            this.setScene(scene);
            this.show();
        }
    }
}