package com.kiarash.tournamentsystem;

import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreEntryPage {

    private final String selectedSport;

    public ScoreEntryPage(String selectedSport) {
        this.selectedSport = selectedSport;
    }

    public void show(Stage stage) {
        Label title = new Label("🏁 Score Entry for " + selectedSport);
        title.getStyleClass().add("title");

        List<Match> allMatches = MatchStorage.load();
        List<Match> sportMatches = allMatches.stream()
                .filter(m -> m.getSport().equalsIgnoreCase(selectedSport))
                .collect(Collectors.toList());

        ComboBox<Match> matchComboBox = new ComboBox<>();
        matchComboBox.getItems().addAll(sportMatches);
        matchComboBox.setPrefWidth(450);
        matchComboBox.setPromptText("Select a match");

        TextField scoreAField = new TextField();
        scoreAField.setPrefWidth(60);
        TextField scoreBField = new TextField();
        scoreBField.setPrefWidth(60);

        VBox scoreABox = new VBox(5, new Label("Team A Score:"), scoreAField);
        VBox scoreBBox = new VBox(5, new Label("Team B Score:"), scoreBField);
        HBox scoreFields = new HBox(15, scoreABox, scoreBBox);
        scoreFields.setAlignment(Pos.CENTER);

        TableView<TeamStanding> standingsTable = new TableView<>();
        standingsTable.setPrefHeight(250);
        standingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        standingsTable.getColumns().addAll(
                createColumn("🏅 Rank", "rank"),
                createColumn("🏴 Team", "teamName"),
                createColumn("🎮 Played", "played"),
                createColumn("✅ Wins", "wins"),
                createColumn("❌ Losses", "losses"),
                createColumn("⭐ Points", "points")
        );

        standingsTable.setItems(FXCollections.observableArrayList(
                StandingCalculator.calculateStandings(selectedSport)
        ));

        Label standingsTitle = new Label("📊 Updated Standings");
        standingsTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));

        Button submitBtn = new Button("✔ Submit");
        submitBtn.getStyleClass().add("green");

        Button backBtn = new Button("⬅ Back");
        backBtn.getStyleClass().add("secondary");
        backBtn.setOnAction(e -> new GameSchedulePage().show(stage));

        Button nextBtn = new Button("➡ Next Round");
        nextBtn.getStyleClass().add("secondary");
        nextBtn.setOnAction(e -> new NextRoundPage(selectedSport).show(stage));

        HBox navButtons = new HBox(10, backBtn, nextBtn);
        navButtons.setAlignment(Pos.CENTER);

        submitBtn.setOnAction(e -> {
            Match selected = matchComboBox.getValue();
            if (selected == null || scoreAField.getText().isEmpty() || scoreBField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please select a match and enter both scores.");
                return;
            }

            try {
                int scoreA = Integer.parseInt(scoreAField.getText());
                int scoreB = Integer.parseInt(scoreBField.getText());

                List<Match> all = MatchStorage.load();
                for (Match m : all) {
                    if (m.getMatchName().equals(selected.getMatchName())) {
                        m.setScoreA(scoreA);
                        m.setScoreB(scoreB);
                    }
                }

                MatchStorage.save(all);

                standingsTable.setItems(FXCollections.observableArrayList(
                        StandingCalculator.calculateStandings(selectedSport)
                ));

                showAlert(Alert.AlertType.INFORMATION, "✔ Score updated!");
                scoreAField.clear();
                scoreBField.clear();
                matchComboBox.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Scores must be valid numbers.");
            }
        });



        VBox layout = new VBox(15, title, matchComboBox, scoreFields, submitBtn, navButtons, standingsTitle, standingsTable);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/kiarash/tournamentsystem/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Score Entry");
        stage.show();
    }

    private TableColumn<TeamStanding, ?> createColumn(String title, String property) {
        TableColumn<TeamStanding, Object> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
