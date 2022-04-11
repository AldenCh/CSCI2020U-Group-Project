module project.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens project.tictactoe to javafx.fxml;
    exports project.tictactoe;
}