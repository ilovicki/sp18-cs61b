package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final long SEED = 20181124;
    private static final Random RANDOM = new Random(SEED);
    private static void fillWithNothing(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
    private static void addRow(TETile[][] tiles, TETile tile, int xOffset, int yOffset, int size) {
        for (int i = 0; i < size; i += 1) {
            tiles[xOffset + i][yOffset] = TETile.colorVariant(tile, 60, 50, 40, RANDOM);
        }
    }
    private static void addHexagon(TETile[][] tiles, TETile tile, int xOffset, int yOffset, int size) {
        for (int i = 0; i < size; i += 1) {
            addRow(tiles, tile, xOffset - i, yOffset - i, size + 2 * i);
            addRow(tiles, tile, xOffset - i, yOffset - (2 * size - 1 - i), size + 2 * i);
        }
    }
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }
    private static void addColumnHexagons(TETile[][] tiles, int xOffset, int yOffset, int number, int size) {
        for (int i = 0; i < number; i += 1) {
            addHexagon(tiles, randomTile(), xOffset, yOffset - 2 * size * i, size);
        }
    }
    private static void tessellateHexagons(TETile[][] tiles, int xOffset, int yOffset, int size) {
        for (int i = 0; i < size - 1; i += 1) {
            addColumnHexagons(tiles, xOffset + (2 * size - 1) * i,
                    yOffset + size * i, size + i, size);
            addColumnHexagons(tiles, xOffset + (2 * size - 1) * (2 * size - 2 - i),
                    yOffset + size * i, size + i, size);
        }
        addColumnHexagons(tiles, xOffset + (2 * size - 1) * (size - 1),
                yOffset + size * (size - 1), 2 * size - 1, size);

    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] hexTiles = new TETile[WIDTH][HEIGHT];
        fillWithNothing(hexTiles);
        tessellateHexagons(hexTiles, 7, 28, 3);
//        ter.renderFrame(hexTiles);
    }
}
