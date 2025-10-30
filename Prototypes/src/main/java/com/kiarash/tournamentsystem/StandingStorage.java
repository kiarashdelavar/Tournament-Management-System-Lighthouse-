package com.kiarash.tournamentsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StandingStorage {

    private static final String FILE = "standings.dat";

    public static void save(List<Standing> standings) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(standings);
            System.out.println("💾 Standings saved to " + FILE);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to save standings.");
        }
    }

    public static List<Standing> load() {
        File file = new File(FILE);
        if (!file.exists()) {
            System.out.println("📂 No existing standings found.");
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            List<Standing> standings = (List<Standing>) in.readObject();
            System.out.println("✅ Loaded " + standings.size() + " standings.");
            return standings;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to load standings.");
            return new ArrayList<>();
        }
    }

    public static void clear() {
        save(new ArrayList<>());
        System.out.println("🧹 Standings cleared.");
    }
}
