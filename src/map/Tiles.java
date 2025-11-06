package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Tiles {

    public BufferedImage tileImg;
    public boolean collision;

    public Tiles(BufferedImage tileImg, boolean collision) {
        this.tileImg = tileImg;
        this.collision = collision;
    }

    public static Tiles[] loadTilesImg() {
        Tiles[] tiles = new Tiles[10];
        try {

            tiles[0] = new Tiles(ImageIO.read(new File("img/Tiles/Grass.png")), false);
            tiles[1] = new Tiles(ImageIO.read(new File("img/Tiles/Wall.png")), true);
            tiles[2] = new Tiles(ImageIO.read(new File("img/Tiles/Water.png")), true);
        } catch (IOException e) {
            System.out.println("cant load tiles");
        }
        return tiles;
    }

}
