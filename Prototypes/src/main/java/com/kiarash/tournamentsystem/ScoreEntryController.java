package com.kiarash.tournamentsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ScoreEntryController {

    @FXML private ComboBox<String> teamAComboBox;
    @FXML private ComboBox<String> teamBComboBox;
    @FXML private TextField teamAScoreField;
    @FXML private TextField teamBScoreField;
    @FXML private Label infoLabel;

    @FXML private TableView<Standing> standingsTable;
    @FXML private TableColumn<Standing, Integer> rankCol;
    @FXML private TableColumn<Standing, String> teamCol;
    @FXML private TableColumn<Standing, Integer> playedCol;
    @FXML private TableColumn<Standing, Integer> winCol;
    @FXML private TableColumn<Standing, Integer> lossCol;
    @FXML private TableColumn<Standing, Integer> pointsCol;

    private final ObservableList<Standing> standings = FXCollections.observableArrayList();
    private String sport;

    @FXML
    public void initialize() {
        setupTable();
    }

    public void setSport(String sport) {
        this.sport = sport;
        loadTeams();
        loadStandings();
    }

    private void setupTable() {
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));
        winCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        lossCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        standingsTable.setItems(standings);
    }

    private void loadTeams() {
        List<Team> allTeams = TeamStorage.loadTeams();

        // Normalize sport: remove emoji if present
        String normalizedSport = normalizeSport(sport);

        List<String> matchingTeams = new ArrayList<>();
        for (Team t : allTeams) {
            if (normalizeSport(t.getSport()).equals(normalizedSport)) {
                matchingTeams.add(t.getSport() + " " + t.getName()); // include emoji + name
            }
        }

        teamAComboBox.setItems(FXCollections.observableArrayList(matchingTeams));
        teamBComboBox.setItems(FXCollections.observableArrayList(matchingTeams));

        // Fill standings with empty scores
        for (String team : matchingTeams) {
            if (standings.stream().noneMatch(s -> s.getTeamName().equals(team))) {
                standings.add(new Standing(team, 0, 0, 0, 0));
            }
        }

        sortAndRank();
    }

    private String normalizeSport(String sport) {
        return sport.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    @FXML
    private void handleSubmit() {
        String teamA = teamAComboBox.getValue();
        String teamB = teamBComboBox.getValue();
        String scoreA = teamAScoreField.getText().trim();
        String scoreB = teamBScoreField.getText().trim();

        if (teamA == null || teamB == null || scoreA.isEmpty() || scoreB.isEmpty()) {
            showMessage("Please complete all fields.", true);
            return;
        }

        if (teamA.equals(teamB)) {
            showMessage("Teams must be different.", true);
            return;
        }

        try {
            int score1 = Integer.parseInt(scoreA);
            int score2 = Integer.parseInt(scoreB);

            updateStanding(teamA, score1, score1 > score2);
            updateStanding(teamB, score2, score2 > score1);

            showMessage("✔ Score recorded: " + teamA + " " + score1 + " - " + score2 + " " + teamB, false);

            teamAComboBox.getSelectionModel().clearSelection();
            teamBComboBox.getSelectionModel().clearSelection();
            teamAScoreField.clear();
            teamBScoreField.clear();

            sortAndRank();
            StandingStorage.save(standings);

        } catch (NumberFormatException e) {
            showMessage("Scores must be valid numbers.", true);
        }
    }

    private void updateStanding(String teamName, int goals, boolean won) {
        Standing s = standings.stream()
                .filter(t -> t.getTeamName().equals(teamName))
                .findFirst()
                .orElseGet(() -> {
                    Standing t = new Standing(teamName, 0, 0, 0, 0);
                    standings.add(t);
                    return t;
                });

        s.setPlayed(s.getPlayed() + 1);
        if (won) {
            s.setWins(s.getWins() + 1);
            s.setPoints(s.getPoints() + 3);
        } else {
            s.setLosses(s.getLosses() + 1);
        }
    }

    private void sortAndRank() {
        standings.sort(Comparator.comparingInt(Standing::getPoints).reversed());
        for (int i = 0; i < standings.size(); i++) {
            standings.get(i).setRank(i + 1);
        }
        standingsTable.refresh();
    }

    private void loadStandings() {
        standings.clear();
        standings.addAll(StandingStorage.load());
        sortAndRank();
    }

    private void showMessage(String msg, boolean error) {
        infoLabel.setText(msg);
        infoLabel.setStyle("-fx-text-fill: " + (error ? "red" : "green") + ";");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kiarash/tournamentsystem/main-menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) teamAComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextRound() {
        try {
            URL fxmlURL = getClass().getResource("/com/kiarash/tournamentsystem/next-round.fxml");
            if (fxmlURL == null) {
                throw new IOException("❌ File not found: next-round.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Next Round");
            stage.show();

            Stage current = (Stage) teamAComboBox.getScene().getWindow();
            current.close();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "⚠ Failed to load next-round.fxml\n" + e.getMessage()).showAndWait();
        }
    }
}
