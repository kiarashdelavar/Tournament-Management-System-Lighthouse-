package com.kiarash.tournamentsystem;

import java.util.Objects;

public class Match {
    private String matchName;
    private String teamA;
    private String teamB;
    private String time;
    private String location;
    private String sport;
    private int scoreA =-1;
    private int scoreB = -1;

    public Match(String matchName, String teamA, String teamB, String time, String location, String sport) {
        this.matchName = matchName;
        this.teamA = teamA;
        this.teamB = teamB;
        this.time = time;
        this.location = location;
        this.sport = sport;
    }

    public String getMatchName() { return matchName; }
    public String getTeamA() { return teamA; }
    public String getTeamB() { return teamB; }
    public String getTeamAandB() { return teamA + " vs " + teamB; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getSport() { return sport; }
    public int getScoreA() { return scoreA; }
    public int getScoreB() { return scoreB; }

    public void setScoreA(int scoreA) { this.scoreA = scoreA; }
    public void setScoreB(int scoreB) { this.scoreB = scoreB; }

    @Override
    public String toString() {
        return matchName + ": " + teamA + " vs " + teamB + " at " + time + " in " + location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;
        Match other = (Match) o;
        return Objects.equals(matchName, other.matchName)
                && Objects.equals(teamA, other.teamA)
                && Objects.equals(teamB, other.teamB)
                && Objects.equals(time, other.time)
                && Objects.equals(location, other.location)
                && Objects.equals(sport, other.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchName, teamA, teamB, time, location, sport);
    }
}
