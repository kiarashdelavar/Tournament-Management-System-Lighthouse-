package com.kiarash.tournamentsystem;

import java.io.Serializable;

public class Team implements Serializable {
    private String name;
    private String icon;
    private String sport;
    private String email;

    public Team(String name, String icon, String sport, String email) {
        this.name = name;
        this.icon = icon;
        this.sport = sport;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getSport() {
        return sport;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return icon + " " + name;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Team)) return false;
        Team other = (Team) obj;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

}
