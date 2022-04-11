package project.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;

public class GameboardController {
    @FXML
    public Canvas canvas;

    @FXML
    private void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        DrawBoard(gc);
        // example for calling the drawO/drawX functions
        DrawO(gc,1,2);
        DrawX(gc,1,1);
        DrawX(gc,2,2);
        DrawO(gc,1,3);
        DrawO(gc,3,3);
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
        System.exit(0);
    }

    @FXML
    public void End() throws IOException { // this button will be removed, End should be called when
                                           // the game ends (win/tie), EndController should also know who won
                                           // to display the correct message.
        SceneController.switchTo(window.End);
        System.out.println("Clicked on end button");
    }
}