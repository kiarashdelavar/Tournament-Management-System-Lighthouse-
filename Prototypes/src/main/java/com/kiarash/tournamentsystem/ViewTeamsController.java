package com.kiarash.tournamentsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class ViewTeamsController {

    @FXML
    private TableView<Team> teamsTable;

    @FXML
    private TableColumn<Team, String> nameColumn;

    @FXML
    private TableColumn<Team, String> sportColumn;

    @FXML
    private TableColumn<Team, String> emailColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sport"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        List<Team> teams = TeamStorage.loadTeams();
        teamsTable.getItems().addAll(teams);
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
}
