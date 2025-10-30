package com.kiarash.tournamentsystem;

import java.io.Serializable;

public class Standing implements Serializable {
    private String teamName;
    private int played;
    private int wins;
    private int losses;
    private int points;
    private int rank;

    public Standing(String teamName, int played, int wins, int losses, int points) {
        this.teamName = teamName;
        this.played = played;
        this.wins = wins;
        this.losses = losses;
        this.points = points;
        this.rank = 0;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getPoints() {
        return points;
    }

    public int getRank() {
        return rank;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    // Helpful for debugging
    @Override
    public String toString() {
        return rank + ". " + teamName + " - " + points + " pts";
    }
}
