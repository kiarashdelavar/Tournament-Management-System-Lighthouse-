package com.kiarash.tournamentsystem;

import javafx.scene.control.Alert;

import java.util.List;
import java.util.stream.Collectors;

public class EmailSender {

    public static void sendEmailPreview(String sport, List<Match> matches) {
        String subject = "📅 Schedule for " + sport;
        StringBuilder content = new StringBuilder();
        content.append("Dear Teams,\n\n")
                .append("Please find the tournament schedule for ").append(sport).append(" below:\n\n");

        for (Match match : matches) {
            content.append("Match: ").append(match.getMatchName())
                    .append("\nTeams: ").append(match.getTeamAandB())
                    .append("\nTime: ").append(match.getTime())
                    .append("\nLocation: ").append(match.getLocation())
                    .append("\n\n");
        }

        List<String> emails = TeamStorage.loadTeams().stream()
                .filter(t -> t.getSport().equalsIgnoreCase(sport))
                .map(Team::getEmail)
                .distinct()
                .collect(Collectors.toList());

        content.append("Recipients:\n").append(String.join(", ", emails));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📧 Email Preview");
        alert.setHeaderText(subject);
        alert.setContentText(content.toString());
        alert.getDialogPane().setPrefWidth(600); // Better formatting
        alert.showAndWait();
    }
}
