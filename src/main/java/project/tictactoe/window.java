package project.tictactoe;

/***
 * Used with SceneController to switch between scene
 */
public enum window {
    // list of fxml files for the scenes
    Server("server.fxml"),
    Gameboard("gameboard.fxml"),
    XEnd("XEnd.fxml"),
    OEnd("OEnd.fxml"),
    TEnd("TEnd.fxml");

    private String fileName;

    /***
     * sets fileName to the input
     * @param fn inputted filename
     */
    window(String fn) {
        fileName = fn;
    }

    /***
     * returns fileName
     */
    public String getFileName() {
        return fileName;
    }
}
