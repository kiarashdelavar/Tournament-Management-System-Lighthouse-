package com.kiarash.tournamentsystem;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class StandingsPage extends Application {

    private List<Standing> standings = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("📊 Tournament Standings");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📊 Updated Standings");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));

        GridPane table = new GridPane();
        table.setPadding(new Insets(20));
        table.setHgap(20);
        table.setVgap(12);

        // Headers
        table.addRow(0,
                bold("🏅 Rank"), bold("🏁 Team"), bold("🎮 Played"),
                bold("🏆 Wins"), bold("❌ Losses"), bold("📈 Points")
        );

        // Read from CSV
        loadCSV();

        // Sort by points
        standings.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

        // Rows
        for (int i = 0; i < standings.size(); i++) {
            Standing s = standings.get(i);
            table.addRow(i + 1,
                    new Label(s.getTeamName()),
                    new Label(String.valueOf(s.getPlayed())),
                    new Label(String.valueOf(s.getWins())),
                    new Label(String.valueOf(s.getLosses())),
                    new Label(String.valueOf(s.getPoints()))
            );

        }

        root.getChildren().addAll(title, table);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void loadCSV() {
        String path = Paths.get("src", "main", "resources", "com", "kiarash", "tournamentsystem", "scorestorage.csv").toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 6) {
                    standings.add(new Standing(
                            p[1].trim(), // Team Name
                            Integer.parseInt(p[2].trim()), // Played
                            Integer.parseInt(p[3].trim()), // Wins
                            Integer.parseInt(p[4].trim()), // Losses
                            Integer.parseInt(p[5].trim())  // Points
                    ));


                }
            }
        } catch (Exception e) {
            System.err.println("Error loading standings: " + e.getMessage());
        }
    }

    private Label bold(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        return label;
    }

    public static void main(String[] args) {
        launch();
    }
}
