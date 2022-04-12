package project.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    @FXML
    private void initialize() throws IOException {
        if (null == gc){
            System.out.println("Null");
            gc = canvas.getGraphicsContext2D();
        }
        DrawBoard(gc);

        OServer oServer = new OServer();
        OThread = new Thread(oServer);
        OThread.start();

        XServer xServer = new XServer();
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
        XThread.interrupt();
        OThread.interrupt();
        SceneController.switchTo(window.End);
        System.out.println("Clicked on end button");
    }

    public class OServer implements Runnable{
        ServerSocket Oss;
        Socket OSocket;
        DataInputStream in;
        @Override
        public void run() {
            try {
                Oss = new ServerSocket(4999);
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
                            xTurn = !xTurn;
                        }
                    }
                }
                in.close();
                OSocket.close();
                Oss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class XServer implements Runnable{
        ServerSocket Xss;
        Socket XSocket;
        DataInputStream in;
        @Override
        public void run() {
            try {
                Xss = new ServerSocket(4998);
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
                            System.out.println("Made it");
                            xTurn = !xTurn;
                        }
                    }
                }
                in.close();
                XSocket.close();
                Xss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}