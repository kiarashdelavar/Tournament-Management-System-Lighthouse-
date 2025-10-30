package com.kiarash.tournamentsystem;

import java.io.*;
import java.util.*;

public class MatchStorage {

    private static final String FILE_PATH = "src/main/resources/com/kiarash/tournamentsystem/matches.csv";

    public static void save(List<Match> matches) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Match m : matches) {
                writer.write(String.join(",",
                        m.getMatchName(),
                        m.getTeamA(),
                        m.getTeamB(),
                        m.getTime(),
                        m.getLocation(),
                        m.getSport(),
                        String.valueOf(m.getScoreA()),
                        String.valueOf(m.getScoreB())
                ));
                writer.newLine();
            }
            System.out.println("💾 Matches saved: " + matches.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Match> load() {
        List<Match> matches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Match m = new Match(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    m.setScoreA(Integer.parseInt(parts[6]));
                    m.setScoreB(Integer.parseInt(parts[7]));
                    matches.add(m);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public static void addMatch(Match match) {
    }
}
