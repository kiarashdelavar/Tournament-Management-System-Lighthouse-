package com.kiarash.tournamentsystem;

import java.util.*;

public class MatchScheduler {

    private static final String[] TIMES = {
            "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM"
    };

    private static final String[] LOCATIONS = {
            "🏟 Hall 1", "🏟 Hall 2", "🏟 Hall 3"
    };

    // For internal team-based scheduling (used by score calculations etc.)
    public static List<Match> generateValidMatches(List<Team> inputTeams, String sport) {
        List<Match> matches = new ArrayList<>();
        List<Team> teams = new ArrayList<>(inputTeams); // ✅ Ensure mutability

        Collections.shuffle(teams);

        int matchCounter = 1;
        int timeIndex = 0;
        int locationIndex = 0;
        Map<String, String> lastMatchTime = new HashMap<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team teamA = teams.get(i);
                Team teamB = teams.get(j);

                String time = TIMES[timeIndex];
                String location = LOCATIONS[locationIndex];

                if (time.equals(lastMatchTime.get(teamA.getName())) || time.equals(lastMatchTime.get(teamB.getName()))) {
                    timeIndex = (timeIndex + 1) % TIMES.length;
                    locationIndex = (locationIndex + 1) % LOCATIONS.length;
                    time = TIMES[timeIndex];
                    location = LOCATIONS[locationIndex];
                }

                lastMatchTime.put(teamA.getName(), time);
                lastMatchTime.put(teamB.getName(), time);

                matches.add(new Match(
                        "Match " + matchCounter++,
                        teamA.getName(),
                        teamB.getName(),
                        time,
                        location,
                        sport
                ));

                timeIndex = (timeIndex + 1) % TIMES.length;
                locationIndex = (locationIndex + 1) % LOCATIONS.length;
            }
        }

        return matches;
    }

    // ✅ Main method used in GameSchedulePage — this was EMPTY before!
    public static List<Match> generateValidMatches(List<String> sportTeamNames, String sport, String selectedIcon) {
        List<Match> matches = new ArrayList<>();
        List<String> teams = new ArrayList<>(sportTeamNames); // Make mutable copy
        Collections.shuffle(teams); // Shuffle to randomize

        String[] TIMES = {"10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM"};
        String[] LOCATIONS = {"🏟 Hall 1", "🏟 Hall 2", "🏟 Hall 3"};

        int matchNumber = 1;
        int timeIndex = 0;
        int locationIndex = 0;
        Map<String, String> teamLastTime = new HashMap<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                String teamA = teams.get(i);
                String teamB = teams.get(j);

                String time = TIMES[timeIndex];
                String location = LOCATIONS[locationIndex];

                // Check if teams already scheduled at this time
                if (time.equals(teamLastTime.get(teamA)) || time.equals(teamLastTime.get(teamB))) {
                    timeIndex = (timeIndex + 1) % TIMES.length;
                    locationIndex = (locationIndex + 1) % LOCATIONS.length;
                    time = TIMES[timeIndex];
                    location = LOCATIONS[locationIndex];
                }

                teamLastTime.put(teamA, time);
                teamLastTime.put(teamB, time);

                matches.add(new Match("Match " + matchNumber++, teamA, teamB, time, location, sport));
                timeIndex = (timeIndex + 1) % TIMES.length;
                locationIndex = (locationIndex + 1) % LOCATIONS.length;
            }
        }

        return matches;
    }
}