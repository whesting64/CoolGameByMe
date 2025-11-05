package map;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;


public class Map {

    public static int[][] loadMap(String filePath, int maxRows, int maxCols) {

        int[][] map = new int[maxRows][maxCols];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < maxRows) {

                String[] rows = line.split(" ");
                for (int col = 0; col < maxCols; col++) {
                    map[row][col] = Integer.parseInt(rows[col]);
                }
                row++;
            }

        } catch (Exception e) {
            System.out.println("cant find map file");
        }

        return map;
    }

}
