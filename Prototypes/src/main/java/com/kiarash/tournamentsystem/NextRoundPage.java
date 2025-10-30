package com.kiarash.tournamentsystem;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.*;

public class NextRoundPage {

    private final String sport;

    public NextRoundPage(String sport) {
        this.sport = sport;
    }

    public void show(Stage stage) {
        Label title = new Label("📈 Tournament Progression");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        Label currentLabel = new Label("📊 Current Standings:");
        currentLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));

        TableView<TeamStanding> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(250);

        table.getColumns().addAll(
                createColumn("🥇 Rank", "rank", 50),
                createColumn("🧑‍🤝‍🧑 Team", "teamName", 150),
                createColumn("⏱ Played", "played", 80),
                createColumn("📌 Points", "points", 80)
        );

        refreshTable(table); // 🔄 Load standings

        Button proceedBtn = new Button("▶ Proceed to Next Round");
        proceedBtn.setOnAction(e -> showNextMatchups(table.getItems()));

        Button liveViewBtn = new Button("📺 Live Tournament Display");
        liveViewBtn.getStyleClass().add("primary");
        liveViewBtn.setOnAction(e -> new LiveStandingsPage(sport).show(stage));

        Button back = new Button("⬅ Back");
        back.setOnAction(e -> new ScoreEntryPage(sport).show(stage));

        VBox layout = new VBox(15, title, currentLabel, table, proceedBtn, liveViewBtn, back);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/kiarash/tournamentsystem/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Next Round - " + sport);
        stage.show();
    }

    private void refreshTable(TableView<TeamStanding> table) {
        table.setItems(FXCollections.observableArrayList(
                StandingCalculator.calculateStandings(sport)
        ));
    }

    private void showNextMatchups(List<TeamStanding> standings) {
        if (standings.size() < 4) {
            showAlert("Need at least 4 teams for next round.");
            return;
        }

        TeamStanding t1 = standings.get(0);
        TeamStanding t2 = standings.get(1);
        TeamStanding t3 = standings.get(2);
        TeamStanding t4 = standings.get(3);

        String match1 = "Match 1: ⚔ " + t1.getTeamName() + " vs " + t4.getTeamName();
        String match2 = "Match 2: ⚔ " + t2.getTeamName() + " vs " + t3.getTeamName();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Next Round Matchups");
        alert.setHeaderText("🆕 Bracket Matchups:");
        alert.setContentText(match1 + "\n" + match2 + "\n\n🔴 Note: Based on current rankings.");
        alert.showAndWait();
    }

    private <T> TableColumn<TeamStanding, T> createColumn(String title, String property, int width) {
        TableColumn<TeamStanding, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
