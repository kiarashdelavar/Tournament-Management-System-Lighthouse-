package com.kiarash.tournamentsystem;

import java.util.*;

public class StandingCalculator {

    public static List<TeamStanding> calculateStandings(String sport) {
        List<Match> matches = MatchStorage.load();
        Map<String, TeamStanding> map = new HashMap<>();

        for (Match m : matches) {
            if (!m.getSport().equalsIgnoreCase(sport)) continue;
            if (m.getScoreA() < 0 || m.getScoreB() < 0) continue;

            String teamA = m.getTeamA();
            String teamB = m.getTeamB();
            int scoreA = m.getScoreA();
            int scoreB = m.getScoreB();

            map.putIfAbsent(teamA, new TeamStanding(teamA));
            map.putIfAbsent(teamB, new TeamStanding(teamB));

            TeamStanding a = map.get(teamA);
            TeamStanding b = map.get(teamB);

            a.setPlayed(a.getPlayed() + 1);
            b.setPlayed(b.getPlayed() + 1);

            a.setGoalsFor(a.getGoalsFor() + scoreA);
            a.setGoalsAgainst(a.getGoalsAgainst() + scoreB);

            b.setGoalsFor(b.getGoalsFor() + scoreB);
            b.setGoalsAgainst(b.getGoalsAgainst() + scoreA);

            if (scoreA > scoreB) {
                a.setWins(a.getWins() + 1);
                a.setPoints(a.getPoints() + 3);
                b.setLosses(b.getLosses() + 1);
            } else if (scoreA < scoreB) {
                b.setWins(b.getWins() + 1);
                b.setPoints(b.getPoints() + 3);
                a.setLosses(a.getLosses() + 1);
            } else {
                a.setPoints(a.getPoints() + 1);
                b.setPoints(b.getPoints() + 1);
            }
        }

        List<TeamStanding> standings = new ArrayList<>(map.values());
        standings.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

        for (int i = 0; i < standings.size(); i++) {
            standings.get(i).setRank(i + 1);
        }

        return standings;
    }
}
