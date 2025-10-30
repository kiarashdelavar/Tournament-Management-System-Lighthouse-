package com.kiarash.tournamentsystem;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.List;

public class NextRoundController {

    private final String sport;

    public NextRoundController(String sport) {
        this.sport = sport;
    }

    public void show(Stage stage) {
        Label title = new Label("📈 Tournament Progression");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        Label standingsLabel = new Label("📊 Current Standings:");
        standingsLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));

        TableView<TeamStanding> standingsTable = new TableView<>();
        standingsTable.setPrefHeight(250);
        standingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<TeamStanding, Integer> rankCol = new TableColumn<>("🏅 Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn<TeamStanding, String> teamCol = new TableColumn<>("👥 Team");
        teamCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<TeamStanding, Integer> playedCol = new TableColumn<>("🕒 Played");
        playedCol.setCellValueFactory(new PropertyValueFactory<>("played"));

        TableColumn<TeamStanding, Integer> pointCol = new TableColumn<>("🎯 Points");
        pointCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        standingsTable.getColumns().addAll(rankCol, teamCol, playedCol, pointCol);
        ObservableList<TeamStanding> standings = FXCollections.observableArrayList(
                StandingCalculator.calculateStandings(sport)
        );
        standingsTable.setItems(standings);

        Button proceedBtn = new Button("➡ Proceed to Next Round");
        proceedBtn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold;");

        VBox matchBox = new VBox(10);
        matchBox.setPadding(new Insets(10));
        Label matchupsLabel = new Label("🧮 Next Round Match-Ups:");
        matchupsLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        matchupsLabel.setVisible(false);

        Label note = new Label("⚠ Note: Match-ups based on rankings and bracket rules.");
        note.setStyle("-fx-background-color: #ff7675; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
        note.setVisible(false);

        proceedBtn.setOnAction(e -> {
            matchBox.getChildren().clear();

            if (standings.size() < 4) {
                matchBox.getChildren().add(new Label("⚠ Not enough teams to generate next round."));
                return;
            }

            matchupsLabel.setVisible(true);
            note.setVisible(true);

            TeamStanding team1 = standings.get(0); // rank 1
            TeamStanding team2 = standings.get(1); // rank 2
            TeamStanding team3 = standings.get(2); // rank 3
            TeamStanding team4 = standings.get(3); // rank 4

            Label m1 = new Label("🏟 Match 1: " + team1.getTeamName() + " vs " + team4.getTeamName());
            Label m2 = new Label("🏟 Match 2: " + team2.getTeamName() + " vs " + team3.getTeamName());

            matchBox.getChildren().addAll(m1, m2);
            matchBox.setAlignment(Pos.CENTER);
        });

        Button backBtn = new Button("⬅ Back");
        backBtn.setOnAction(e -> new ScoreEntryPage(sport).show(stage));

        VBox layout = new VBox(20,
                title,
                standingsLabel,
                standingsTable,
                proceedBtn,
                matchupsLabel,
                matchBox,
                note,
                backBtn
        );

        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(layout, 850, 600);
        stage.setScene(scene);
        stage.setTitle("Next Round - " + sport);
        stage.show();
    }
}
