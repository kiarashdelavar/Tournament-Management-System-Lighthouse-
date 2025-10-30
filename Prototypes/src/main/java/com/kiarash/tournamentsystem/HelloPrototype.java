package com.kiarash.tournamentsystem;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;

import java.util.*;

public class HelloPrototype extends Application {

    private MediaPlayer mediaPlayer;
    private Label audioIcon;

    @Override
    public void start(Stage stage) {
        // 🎵 Background music
        Media bgMusic = new Media(getClass().getResource("/com/kiarash/tournamentsystem/background.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(bgMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.2);
        mediaPlayer.play();

        // 🔊 Toggle audio icon
        audioIcon = new Label("🔈");
        audioIcon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        audioIcon.setStyle("-fx-cursor: hand;");
        audioIcon.setOnMouseClicked(e -> toggleAudio());

        StackPane root = new StackPane();
        StackPane.setAlignment(audioIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(audioIcon, new Insets(20));
        root.getChildren().add(audioIcon);

        stage.setTitle("Team Registration");

        // 🏷 Title
        Label title = new Label("🏆 Lighthouse Team Registration");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#2b67f6"));

        // 📄 Form Inputs
        Label nameLabel = new Label("👥 Team Name:");
        nameLabel.setFont(Font.font(20));
        TextField nameField = new TextField();
        nameField.setPrefWidth(350);
        nameField.setFont(Font.font(18));

        Label iconLabel = new Label("🏆 Select Team Icon:");
        iconLabel.setFont(Font.font(20));
        ComboBox<String> iconBox = new ComboBox<>();
        iconBox.getItems().addAll("🦁", "🐯", "🐉", "🐸", "🦅", "🐍", "🐺", "🐼");
        iconBox.setPrefWidth(350);
        iconBox.setStyle("-fx-font-size: 18px;");

        Label sportLabel = new Label("🎮 Select Sport:");
        sportLabel.setFont(Font.font(20));
        ComboBox<String> sportBox = new ComboBox<>();
        sportBox.getItems().addAll("⚽ Football", "🏐 Volleyball", "🥏 Korfball");
        sportBox.setPrefWidth(350);
        sportBox.setStyle("-fx-font-size: 18px;");

        Label emailLabel = new Label("✉️ Contact Email:");
        emailLabel.setFont(Font.font(20));
        TextField emailField = new TextField();
        emailField.setPrefWidth(350);
        emailField.setFont(Font.font(18));

        Button submitBtn = new Button("✔ Submit");
        submitBtn.setPrefWidth(350);
        submitBtn.setFont(Font.font(18));
        submitBtn.setStyle("-fx-background-color: #2b67f6; -fx-text-fill: white; -fx-font-weight: bold;");

        // ➕ Handle Submit
        submitBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String icon = iconBox.getValue();
            String selectedSportRaw = sportBox.getValue();
            String email = emailField.getText().trim();

            if (name.isEmpty() || icon == null || selectedSportRaw == null || email.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
                return;
            }

            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showAlert(Alert.AlertType.ERROR, "Invalid email format.");
                return;
            }

            // Split icon and sport
            String[] sportSplit = selectedSportRaw.split(" ", 2);
            String sportIcon = sportSplit[0];
            String sport = sportSplit[1];

            // Save team
            Team team = new Team(name, icon, sport, email);
            TeamStorage.saveTeam(team);

            // Show confirmation
            showAlert(Alert.AlertType.INFORMATION,
                    "✅ Team Registered!\n\n" +
                            "Team: " + icon + " " + name + "\n" +
                            "Sport: " + sportIcon + " " + sport + "\n" +
                            "Email: " + email);

            // Generate matches
            List<String> opponents = Arrays.asList("Team A", "Team B", "Team C", "Team D");
            List<String> halls = Arrays.asList("🏟 Hall 1", "🎯 Hall 2", "🎪 Hall 3", "🏛 Hall 4");
            List<String> times = Arrays.asList("10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM");

            Random rand = new Random();
            List<String> emojis = Arrays.asList("🦁", "🐯", "🐉", "🦄", "🐻", "🐗");

            int matchCount = 3;
            for (int i = 0; i < matchCount; i++) {
                String opponent = opponents.get(rand.nextInt(opponents.size()));
                String opponentIcon = emojis.get(rand.nextInt(emojis.size()));
                String time = times.get(rand.nextInt(times.size()));
                String hall = halls.get(rand.nextInt(halls.size()));

                Match match = new Match(
                        "Match " + (i + 1),
                        sportIcon + " " + icon + " " + name,
                        sportIcon + " " + opponentIcon + " " + opponent,
                        time,
                        hall,
                        sport
                );
                MatchStorage.addMatch(match);
            }

            // Clear form
            nameField.clear();
            iconBox.getSelectionModel().clearSelection();
            sportBox.getSelectionModel().clearSelection();
            emailField.clear();

            new GameSchedulePage().show(stage);
        });

        VBox form = new VBox(18,
                title,
                new HBox(12, nameLabel, nameField),
                new HBox(12, iconLabel, iconBox),
                new HBox(12, sportLabel, sportBox),
                new HBox(12, emailLabel, emailField),
                submitBtn
        );
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(40));
        form.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0.3, 0, 4);");

        // 🖼 Logo Side
        Image logo = new Image(getClass().getResourceAsStream("/com/kiarash/tournamentsystem/logo (2).png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(220);
        logoView.setPreserveRatio(true);

        VBox logoCard = new VBox(logoView);
        logoCard.setAlignment(Pos.CENTER);
        logoCard.setPadding(new Insets(40));
        logoCard.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.3, 0, 4);");

        HBox content = new HBox(50, form, logoCard);
        content.setAlignment(Pos.CENTER);

        BackgroundImage bg = new BackgroundImage(
                new Image(getClass().getResourceAsStream("/com/kiarash/tournamentsystem/bg.jpg")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        root.setBackground(new Background(bg));
        root.getChildren().add(content);

        Scene scene = new Scene(root, 1400, 800);
        stage.setScene(scene);
        stage.getIcons().add(logo);
        stage.show();
    }

    private void toggleAudio() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            audioIcon.setText("🔇");
        } else {
            mediaPlayer.play();
            audioIcon.setText("🔈");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}

