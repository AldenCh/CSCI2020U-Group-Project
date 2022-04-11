package project.tictactoe;

public enum window {
    // list of fxml files for the scenes
    Server("server.fxml"),
    Gameboard("gameboard.fxml"),
    End("end.fxml");

    private String fileName;

    // sets the fileName to the input
    window(String fn) {
        fileName = fn;
    }

    // returns fileName
    public String getFileName() {
        return fileName;
    }
}
