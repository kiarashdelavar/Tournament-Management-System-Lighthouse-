package com.kiarash.tournamentsystem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
//import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;

public class AwardCeremonyPage {
    private final String sport;
    private boolean isDarkMode = false;
    private Scene scene;

    public AwardCeremonyPage(String sport) {
        this.sport = sport;
    }

    public void show(Stage stage) {
        // 🏆 Trophy image + title
        Label title = new Label("Award Ceremony - " + sport);
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 24));

        Image trophy = new Image(getClass().getResource("/com/kiarash/tournamentsystem/trophy.png").toExternalForm());
        ImageView trophyIcon = new ImageView(trophy);
        trophyIcon.setFitHeight(32);
        trophyIcon.setPreserveRatio(true);

        HBox titleBox = new HBox(10, trophyIcon, title);
        titleBox.setAlignment(Pos.CENTER);

        // 🥉 Subtitle
        Label subtitle = new Label("🥉 Top 3 Teams");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        // 📊 Load standings
        List<TeamStanding> standings = StandingCalculator.calculateStandings(sport);
        VBox podium = new VBox(12);
        podium.setAlignment(Pos.CENTER);

        String[] trophies = {"🥇", "🥈", "🥉"};
        for (int i = 0; i < Math.min(3, standings.size()); i++) {
            TeamStanding team = standings.get(i);
            Label teamLabel = new Label(trophies[i] + " " + team.getTeamName() + " - " + team.getPoints() + " pts");
            teamLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));
            teamLabel.setOpacity(0);

            FadeTransition fade = new FadeTransition(Duration.seconds(1), teamLabel);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setDelay(Duration.seconds(i));
            fade.play();

            TranslateTransition bounce = new TranslateTransition(Duration.seconds(1), teamLabel);
            bounce.setFromY(-50);
            bounce.setToY(0);
            bounce.setCycleCount(1);
            bounce.setInterpolator(Interpolator.EASE_OUT);
            bounce.setDelay(Duration.seconds(i));
            bounce.play();

            podium.getChildren().add(teamLabel);
        }

        // 📄 Export CSV
        Button exportBtn = new Button("📄 Export CSV");
        exportBtn.setOnAction(e -> exportToCSV(standings));

        // 📸 Screenshot export
        Button screenshotBtn = new Button("📋 Copy Screenshot");
        screenshotBtn.setOnAction(e -> captureToClipboard(stage));


        // 🌗 Dark mode toggle
        Button toggleTheme = new Button("🌗 Toggle Dark Mode");
        toggleTheme.setOnAction(e -> {
            isDarkMode = !isDarkMode;
            scene.getStylesheets().clear();
            String css = isDarkMode
                    ? "/com/kiarash/tournamentsystem/award-dark.css"
                    : "/com/kiarash/tournamentsystem/award.css";
            scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
        });

        // 🔙 Back
        Button backBtn = new Button("⬅ Back");
        backBtn.setOnAction(e -> new NextRoundPage(sport).show(stage));

        VBox layout = new VBox(20, title, subtitle, podium, exportBtn, screenshotBtn, toggleTheme, backBtn);

        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        // 🎨 Scene styling
        scene = new Scene(layout, 700, 520);
        scene.getStylesheets().add(getClass().getResource("/com/kiarash/tournamentsystem/award.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Award Ceremony");

        // 🎵 Victory music
        try {
            String path = getClass().getResource("/com/kiarash/tournamentsystem/victory.mp3").toExternalForm();
            MediaPlayer player = new MediaPlayer(new Media(path));
            player.setVolume(0.8);
            player.setOnEndOfMedia(() -> player.stop());
            player.play();
        } catch (Exception e) {
            System.out.println("🎵 Could not load music file.");
        }

        stage.show();
    }

    private void exportToCSV(List<TeamStanding> standings) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Standings as CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Rank,Team,Points\n");
                for (TeamStanding t : standings) {
                    writer.write(t.getRank() + "," + t.getTeamName() + "," + t.getPoints() + "\n");
                }
                showAlert(Alert.AlertType.INFORMATION, "✅ Exported to CSV!");
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "❌ Failed to save file.");
            }
        }
    }

    private void captureToClipboard(Stage stage) {
        WritableImage image = stage.getScene().snapshot(null);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
        showAlert(Alert.AlertType.INFORMATION, "📸 Screenshot copied to clipboard!");
    }



    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
