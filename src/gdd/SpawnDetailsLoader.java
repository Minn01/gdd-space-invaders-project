package gdd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SpawnDetailsLoader {

    /**
     * Loads SpawnDetails from a CSV file into a HashMap with frame numbers as keys
     *
     * @param filename Path to the CSV file
     * @return HashMap<Integer, SpawnDetails> where key is frame number
     * @throws IOException if file reading fails
     */
    public static HashMap<Integer, SpawnDetails> loadFromCSV(String filename) throws IOException {
        HashMap<Integer, SpawnDetails> spawnMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split(",");

                    if (parts.length != 6) {
                        System.err.println("Invalid line format (expected 6 columns): " + line);
                        continue;
                    }

                    // Parse each field: frame,type,x,y,count,spacing
                    int frame = Integer.parseInt(parts[0].trim());
                    SpawnType type = SpawnType.valueOf(parts[1].trim().toUpperCase());
                    int x = Integer.parseInt(parts[2].trim());
                    int y = Integer.parseInt(parts[3].trim());
                    int count = Integer.parseInt(parts[4].trim());
                    int spacing = Integer.parseInt(parts[5].trim());

                    SpawnDetails spawn = new SpawnDetails(type, x, y, count, spacing);
                    spawnMap.put(frame, spawn);

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing numbers in line: " + line);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid enemy type in line: " + line);
                }
            }
        }

        return spawnMap;
    }
}