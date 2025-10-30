package com.kiarash.tournamentsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.*;

public class GameSchedulePage {

    private final ObservableList<Match> matches = FXCollections.observableArrayList();

    public void show(Stage stage) {
        Label title = new Label("🗓 Game Schedule");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2d3436"));

        Label sub = new Label("Tournament Schedule:");
        sub.setFont(Font.font("Segoe UI", 16));

        ComboBox<String> sportComboBox = new ComboBox<>();
        sportComboBox.getItems().addAll("Football", "Volleyball", "Korfball");
        sportComboBox.setValue("Football");

        CheckBox generateCheckBox = new CheckBox("Generate Schedule");

        HBox formRow = new HBox(10, sportComboBox, generateCheckBox);
        formRow.setAlignment(Pos.CENTER_LEFT);
        formRow.setPadding(new Insets(10, 0, 10, 0));

        TableView<Match> table = new TableView<>(matches);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("-fx-font-size: 14px;");

        TableColumn<Match, String> col1 = new TableColumn<>("Match");
        col1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatchName()));

        TableColumn<Match, String> col2 = new TableColumn<>("Teams");
        col2.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTeamA() + " vs " + data.getValue().getTeamB()));

        TableColumn<Match, String> col3 = new TableColumn<>("Time");
        col3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTime()));

        TableColumn<Match, String> col4 = new TableColumn<>("Location");
        col4.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));

        table.getColumns().addAll(col1, col2, col3, col4);

        VBox tableCard = new VBox(table);
        tableCard.setPadding(new Insets(10));
        tableCard.setStyle("-fx-border-color: lightgreen; -fx-border-width: 2; -fx-background-color: #efefef;");

        Label tableTitle = new Label("📊 Generated Matches Table:");
        tableTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        tableTitle.setTextFill(Color.web("#2d3436"));

        Button scoreEntryBtn = new Button("🕒 Real-time Score Entry");
        scoreEntryBtn.setPrefWidth(220);
        scoreEntryBtn.setStyle("-fx-background-color: #00b894; -fx-text-fill: white; -fx-font-weight: bold;");
        scoreEntryBtn.setOnAction(e -> {
            ScoreEntryPage page = new ScoreEntryPage(sportComboBox.getValue());
            page.show(stage);
        });

        Button exportBtn = new Button("📤 Export to PDF");
        exportBtn.setPrefWidth(150);
        exportBtn.setStyle("-fx-background-color: #7e70ff; -fx-text-fill: white; -fx-font-weight: bold;");
        exportBtn.setOnAction(e -> exportToPDF());

        Button emailBtn = new Button("✉️ Send via Email");
        emailBtn.setPrefWidth(180);
        emailBtn.setStyle("-fx-background-color: #7e70ff; -fx-text-fill: white; -fx-font-weight: bold;");
        emailBtn.setOnAction(e -> showEmailPreview());

        HBox buttons = new HBox(15, exportBtn, emailBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));

        // ✅ FIXED: always regenerate, not just once
        generateCheckBox.setOnAction(e -> {
            generateMatches(sportComboBox.getValue());
            generateCheckBox.setSelected(false); // allow re-click
        });

        VBox layout = new VBox(15, title, sub, formRow, tableTitle, tableCard, scoreEntryBtn, buttons);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #fdfbfb, #ebedee);");

        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Game Schedule");
        stage.show();
    }

    private void generateMatches(String sport) {
        matches.clear();

        List<Team> allTeams = TeamStorage.loadTeams(); // 🔁 always reloaded
        Map<String, String> iconMap = Map.of(
                "Football", "⚽", "Volleyball", "🏐", "Korfball", "🥏"
        );

        String selectedIcon = iconMap.getOrDefault(sport, "");
        List<String> sportTeamNames = new ArrayList<>(allTeams.stream()
                .filter(t -> t.getSport().equalsIgnoreCase(sport))
                .map(t -> t.getIcon() + " " + t.getName())
                .toList());

        List<Match> generated = MatchScheduler.generateValidMatches(sportTeamNames, sport, selectedIcon);
        matches.addAll(generated);

        MatchStorage.save(matches);
    }

    private void exportToPDF() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("GameSchedule.pdf"));
            document.open();

            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            com.lowagie.text.Font matchFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("📅 Game Schedule", titleFont));
            document.add(new Paragraph(" "));

            for (Match m : matches) {
                document.add(new Paragraph(
                        m.getMatchName() + "\n" +
                                m.getTeamA() + " vs " + m.getTeamB() + "\n" +
                                "🕒 " + m.getTime() + " | " +
                                "📍 " + m.getLocation() + " | " +
                                "🏷 Sport: " + m.getSport() + "\n", matchFont));
                document.add(new Paragraph(" "));
            }

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "✅ PDF exported successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "❌ PDF export failed.");
        }
    }

    private void showEmailPreview() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Send Schedule");
        dialog.setHeaderText("📤 Simulated Email");
        dialog.setContentText("Enter recipient email:");

        dialog.showAndWait().ifPresent(email -> {
            StringBuilder sb = new StringBuilder("📧 To: " + email + "\n\n");
            sb.append("📅 Match Schedule:\n\n");

            for (Match match : matches) {
                sb.append(match.getMatchName()).append(": ")
                        .append(match.getTeamA()).append(" vs ").append(match.getTeamB())
                        .append(" | ").append(match.getTime()).append(" | ").append(match.getLocation())
                        .append(" | ").append("🏷 ").append(match.getSport()).append("\n");
            }

            TextArea preview = new TextArea(sb.toString());
            preview.setWrapText(true);
            preview.setEditable(false);
            preview.setPrefHeight(300);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email Preview");
            alert.setHeaderText("Simulated Email");
            alert.getDialogPane().setContent(preview);
            alert.showAndWait();
        });
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
