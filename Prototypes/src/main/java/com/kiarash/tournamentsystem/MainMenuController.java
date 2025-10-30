package com.kiarash.tournamentsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void goToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kiarash/tournamentsystem/register-team.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Register Team");
        stage.show();
    }

    @FXML
    private void goToView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kiarash/tournamentsystem/view-teams.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("View Teams");
        stage.show();
    }
    @FXML
    private void goToScoreEntry() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kiarash/tournamentsystem/score-entry.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Score Entry");
        stage.show();
    }
}
