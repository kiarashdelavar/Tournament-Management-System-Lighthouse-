package com.kiarash.tournamentsystem;

public class TeamStanding {
    private int rank;
    private String teamName;
    private int played;
    private int wins;
    private int losses;
    private int points;
    private int goalsFor;
    private int goalsAgainst;

    public TeamStanding(String teamName) {
        this.teamName = teamName;
        this.played = 0;
        this.wins = 0;
        this.losses = 0;
        this.points = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
    }

    // Getters
    public int getRank() { return rank; }
    public String getTeamName() { return teamName; }
    public int getPlayed() { return played; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getPoints() { return points; }
    public int getGoalsFor() { return goalsFor; }
    public int getGoalsAgainst() { return goalsAgainst; }
    public int getGoalDifference() { return goalsFor - goalsAgainst; }

    // Setters
    public void setRank(int rank) { this.rank = rank; }
    public void setPlayed(int played) { this.played = played; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setPoints(int points) { this.points = points; }
    public void setGoalsFor(int goalsFor) { this.goalsFor = goalsFor; }
    public void setGoalsAgainst(int goalsAgainst) { this.goalsAgainst = goalsAgainst; }
}
