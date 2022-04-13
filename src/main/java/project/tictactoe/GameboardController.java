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

public class GameboardController {
    @FXML
    public Canvas canvas;
    private GraphicsContext gc = null;
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
        o = new OWindow();
        o.setX(870);
        o.setY(320);
        x = new XWindow();
        x.setX(340);
        x.setY(320);

        if (null == gc){
            System.out.println("Null");
            gc = canvas.getGraphicsContext2D();
        }
        DrawBoard(gc);

        Oss = new ServerSocket(4999);
        OServer oServer = new OServer(Oss);
        OThread = new Thread(oServer);
        OThread.start();

        Xss = new ServerSocket(4998);
        XServer xServer = new XServer(Xss);
        XThread = new Thread(xServer);
        XThread.start();

        // example for calling the drawO/drawX functions
//        DrawO(gc,1,2);
//        DrawX(gc,1,1);
//        DrawX(gc,2,2);
//        DrawO(gc,1,3);
//        DrawO(gc,3,3);
    }

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

    private void DrawX(GraphicsContext gc, double row, double col){
        double xStart = (col - 1) * 100 + 25;
        double yStart = (row - 1) * 100 + 25;
        double xEnd = xStart + 60;
        double yEnd = yStart + 60;

        gc.setStroke(Color.RED);
        gc.setLineWidth(25);
        gc.strokeLine(xStart, yStart, xEnd, yEnd);
        gc.strokeLine(xStart, yEnd, xEnd, yStart);
    }

    private void DrawO(GraphicsContext gc, double row, double col){
        double xStart = (col - 1) * 100 + 10;
        double yStart = (row - 1) * 100 + 10;

        gc.setFill(Color.LIGHTGREEN);
        gc.fillOval(xStart,yStart,90,90);
        gc.setFill(Color.WHITE);
        gc.fillOval(xStart + 20,yStart + 20,50,50);
    }

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
    private void Exit() {
        System.out.println("Clicked on Exit button");
        running = false;
        System.exit(0);
    }

    @FXML
    public void End() throws IOException { // this button will be removed, End should be called when
                                           // the game ends (win/tie), EndController should also know who won
                                           // to display the correct message.
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
                        SceneController.switchTo(window.End);
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

                            String status = checkWin(board, position[0]);
                            if (status.equals("X") || status.equals("O") || status == "tie"){
                                OSocket.close();
                                Thread.sleep(500);
                                End();
                                break;
                            }

                            xTurn = !xTurn;
                        }
                    }
                }
                in.close();
                OSocket.close();
                Oss.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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

                            String status = checkWin(board, position[0]);
                            if (status.equals("X") || status.equals("O") || status == "tie"){
                                XSocket.close();
                                Thread.sleep(500);
                                End();
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