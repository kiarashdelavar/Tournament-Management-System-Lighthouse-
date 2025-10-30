package com.kiarash.tournamentsystem;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TeamStorage {

    private static final String FILE_PATH = "src/main/resources/com/kiarash/tournamentsystem/teams.csv";

    public static List<Team> loadTeams() {
        List<Team> teams = new ArrayList<>();

        try (InputStream is = TeamStorage.class.getResourceAsStream("/com/kiarash/tournamentsystem/teams.csv")) {
            System.out.println("🔍 is == null ? " + (is == null));
            if (is == null) {
                System.err.println("❌ teams.csv NOT FOUND in resources!");
                return teams;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    String icon = parts[1].trim();
                    String sport = parts[2].trim().replaceAll("[^a-zA-Z]", "").toLowerCase();
                    String email = parts[3].trim();
                    teams.add(new Team(name, icon, sport, email));
                }
            }

            System.out.println("✅ Loaded " + teams.size() + " teams from resources.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public static void saveTeam(Team team) {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(team.getName() + "," + team.getIcon() + "," + team.getSport() + "," + team.getEmail());
                writer.newLine();
                System.out.println("💾 Saved team: " + team.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
