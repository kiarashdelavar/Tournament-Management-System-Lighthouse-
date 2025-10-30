package com.kiarash.tournamentsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RegisterTeamController {

    @FXML private TextField teamNameField;
    @FXML private ComboBox<String> teamIconBox;
    @FXML private ComboBox<String> sportComboBox;
    @FXML private TextField emailField;

    @FXML
    public void initialize() {
        // Icons only
        teamIconBox.getItems().addAll("🦁", "🐯", "🐉", "🐸", "🦅", "🐍", "🐺", "🐼");

        // Sport names
        sportComboBox.getItems().addAll("Football", "Volleyball", "Korfball");
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String name = teamNameField.getText();
        String icon = teamIconBox.getValue();
        String sport = sportComboBox.getValue();
        String email = emailField.getText();

        if (name.isEmpty() || icon == null || sport == null || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid email format.");
            return;
        }

        Team team = new Team(name, icon, sport, email);
        TeamStorage.saveTeam(team);

        showAlert(Alert.AlertType.INFORMATION,
                "Team \"" + icon + " " + name + "\" registered for " + sport + "!\nContact: " + email);

        teamNameField.clear();
        teamIconBox.getSelectionModel().clearSelection();
        sportComboBox.getSelectionModel().clearSelection();
        emailField.clear();
    }

    @FXML
    private void handleViewTeams(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/kiarash/tournamentsystem/view-teams.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/kiarash/tournamentsystem/main-menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
