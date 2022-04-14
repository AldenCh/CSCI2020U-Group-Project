package project.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;
import java.util.Set;

/***
 * Controller for the menu screen, provides functionality for the exit and start game buttons
 */
public class ServerController {
    @FXML Label Xwins;
    @FXML Label Owins;
    @FXML Label Ties;

    @FXML
    /***
     * Utilizes File IO to read a csv file containing the number of wins for X,O,Tie and displays them with a label
     */
    private void initialize() {
        // read number of wins for X, O, and Tie
        FileReader file = null;
        try {
            file = new FileReader(new File(
                    "src/main/resources/project/tictactoe","wins.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader;
        reader = new BufferedReader(file);
        try {
            String line = reader.readLine();
            line = reader.readLine();
            String[] split = line.split(",");
            Xwins.setText(" X wins: " + split[0]);
            Owins.setText(" O wins: " + split[1]);
            Ties.setText(" Ties: " + split[2]);

            reader.close();  //closes the scanner

        } catch(IOException e){
            System.out.println("IOException from load()");
            e.printStackTrace();
        }
    }

    @FXML
    /***
     * When the start game button is clicked Start() is called, changing the scene to the gameboard
     */
    private void Start() throws IOException {
        SceneController.switchTo(window.Gameboard);
        System.out.println("Clicked on Start Game button");
    }

    @FXML
    /***
     * When the exit button is clicked Exit() is called, closing all windows
     */
    private void Exit() throws IOException {
        System.out.println("Clicked on Exit button");
        System.exit(0);
    }
}