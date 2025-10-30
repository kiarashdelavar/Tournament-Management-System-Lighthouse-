package com.kiarash.tournamentsystem;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.FontFactory;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class LiveStandingsPage {

    private final String sport;
    private Label nextMatchLabel = new Label();
    private Label currentMatchLabel = new Label();
    private int matchIndex = 0;
    private List<Match> matches;
    private TableView<TeamStanding> table;

    public LiveStandingsPage(String sport) {
        this.sport = sport;
    }

    public void show(Stage stage) {
        Label title = new Label("📣 LIVE TOURNAMENT STANDINGS");
        title.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 26));
        title.setTextAlignment(TextAlignment.CENTER);

        Separator separator = new Separator();

        table = new TableView<>();
        refreshStandings(table); // 🟢 Refresh standings on show

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPrefHeight(300);
        table.getColumns().addAll(
                createColumn("🥇 Rank", "rank", 60),
                createColumn("⚽ Team", "teamName", 150),
                createColumn("⏱ Played", "played", 80),
                createColumn("✅ Wins", "wins", 80),
                createColumn("❌ Losses", "losses", 80),
                createColumn("🎯 Points", "points", 80)
        );

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(TeamStanding team, boolean empty) {
                super.updateItem(team, empty);
                if (team == null || empty) {
                    setStyle("");
                } else {
                    Match current = matches.get(matchIndex % matches.size());
                    if (team.getTeamName().equalsIgnoreCase(current.getTeamA()) ||
                            team.getTeamName().equalsIgnoreCase(current.getTeamB())) {
                        setStyle("-fx-background-color: #ffeaa7; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        matches = MatchStorage.load().stream()
                .filter(m -> m.getSport().equalsIgnoreCase(sport))
                .toList();

        currentMatchLabel.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.BOLD, 16));
        nextMatchLabel.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.BOLD, 16));
        updateMatchLabels();

        VBox infoBox = new VBox(10, nextMatchLabel, currentMatchLabel);
        infoBox.setPadding(new Insets(20));
        infoBox.setStyle("-fx-background-color: #e5ffe5; -fx-border-color: #55efc4; -fx-border-width: 2; -fx-border-radius: 10;");
        infoBox.setAlignment(Pos.CENTER);

        Button back = new Button("⬅ Back");
        back.setOnAction(e -> new NextRoundPage(sport).show(stage));
        back.getStyleClass().add("secondary");

        Button awardBtn = new Button("🎊 Award Ceremony");
        awardBtn.setOnAction(e -> new AwardCeremonyPage(sport).show(stage));
        awardBtn.getStyleClass().add("primary");


        Button exportBtn = new Button("🖨 Export PDF");
        exportBtn.setOnAction(e -> exportToPDF(table.getItems()));

        Button exportCSV = new Button("📄 Export CSV");
        exportCSV.setOnAction(e -> exportCSV(table.getItems()));


        VBox layout = new VBox(20, title, separator, table, infoBox, awardBtn, back);

        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 900, 620);
        scene.getStylesheets().add(getClass().getResource("/com/kiarash/tournamentsystem/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Live Standings");
        stage.show();

        // 🔄 Start both timers
        startMatchRotation(table);
        startAutoRefresh(); // Auto-refresh table
    }

    private <T> TableColumn<TeamStanding, T> createColumn(String title, String property, int width) {
        TableColumn<TeamStanding, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }

    private void refreshStandings(TableView<TeamStanding> table) {
        table.setItems(FXCollections.observableArrayList(
                StandingCalculator.calculateStandings(sport)
        ));
    }


    private void updateMatchLabels() {
        if (matches.isEmpty()) {
            currentMatchLabel.setText("🔥 No matches available");
            nextMatchLabel.setText("⏭ All matches completed");
            return;
        }

        Match current = matches.get(matchIndex % matches.size());
        Match next = matches.get((matchIndex + 1) % matches.size());

        currentMatchLabel.setText("🔥 Currently Playing: " + current.getTeamA() + " vs " + current.getTeamB()
                + " – 🔴 " + current.getLocation());
        nextMatchLabel.setText("⏭ Next Match: " + next.getTeamA() + " vs " + next.getTeamB()
                + " – " + next.getTime() + " – 🔵 " + next.getLocation());
    }

    private void startMatchRotation(TableView<TeamStanding> table) {
        // Rotate match info every 10 seconds
        Timeline matchTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    matchIndex = (matchIndex + 1) % matches.size();
                    updateMatchLabels();
                })
        );
        matchTimeline.setCycleCount(Timeline.INDEFINITE);
        matchTimeline.play();

        // Refresh standings every 15 seconds
        Timeline refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(15), e -> refreshStandings(table))
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }


    private void startAutoRefresh() {
        Timeline refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(15), e -> refreshStandings(table)));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void exportToPDF(List<TeamStanding> standings) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("LiveStandings.pdf"));
            document.open();

            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            com.lowagie.text.Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("📣 LIVE TOURNAMENT STANDINGS", titleFont));
            document.add(new Paragraph(" "));

            for (TeamStanding t : standings) {
                document.add(new Paragraph(
                        "Rank " + t.getRank() +
                                " | " + t.getTeamName() +
                                " | Played: " + t.getPlayed() +
                                " | Wins: " + t.getWins() +
                                " | Losses: " + t.getLosses() +
                                " | GD: " + t.getGoalDifference() +
                                " | Points: " + t.getPoints(), textFont
                ));
            }

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "✅ PDF Exported");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void exportCSV(List<TeamStanding> standings) {
        try (PrintWriter writer = new PrintWriter("standings.csv")) {
            writer.println("Rank,Team,Played,Wins,Losses,Points");
            for (TeamStanding t : standings) {
                writer.printf("%d,%s,%d,%d,%d,%d\n", t.getRank(), t.getTeamName(), t.getPlayed(), t.getWins(), t.getLosses(), t.getPoints());
            }
            showAlert(Alert.AlertType.INFORMATION, "✅ CSV Exported!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
