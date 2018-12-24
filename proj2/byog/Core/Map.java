package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;

import java.util.Random;
import java.lang.Math;
public class Map {
    static final int WIDTH = 80;
    static final int HEIGHT = 45;
    static Random random;
    public TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    private int numRooms;
    private Room[] rooms;
    private Hall[] halls;
    private int roomCount;
    private int hallCount;
    public int playerX;
    public int playerY;
    public Map(long seed) {
        random = new Random(seed);
        initialize();
        numRooms = RandomUtils.uniform(random, 40, 60);
        rooms = new Room[numRooms];
        halls = new Hall[numRooms - 1];
        roomCount = 0;
        hallCount = 0;
        playerX = 0;
        playerY = 0;

    }
    private void initialize() {
        fillWithTiles(0, 0, WIDTH, HEIGHT, Tileset.NOTHING);
    }
    private void fillWithTiles(int blx, int bly, int w, int h, TETile tile) {
        for (int i = blx; i < blx + w; i += 1) {
            for (int j = bly; j < bly + h; j += 1) {
                tiles[i][j] = tile;
            }

        }
    }

    public void addRoomsAndHalls() {
        rooms[0] = new Room();
        roomCount += 1;
        halls[0] = new Hall(rooms[0]);
        hallCount += 1;
        for (int i = 1; i < numRooms; i += 1) {
            Room room = new Room(halls[hallCount - 1]);
            boolean interfere = false;
            for (int j = 0; j < roomCount; j += 1) {
                if (room.interfere(rooms[j])) {
                    interfere = true;
                    break;
                }
            }
            if (room.success && !interfere) {
                rooms[roomCount] = room;
                roomCount += 1;
                if (i < numRooms - 1) {
                    Hall hall = new Hall(rooms[roomCount - 1]);
                    if (hall.success) {
                        halls[hallCount] = hall;
                        hallCount += 1;
                    }
                }
            }
            if (i < numRooms - 1 && hallCount < roomCount) {
                roomCount -= 1;
            }
        }
//        hallCount -= 1;
    }

    private void tileRooms() {
        for (int i = 0; i < roomCount; i += 1) {
            fillWithTiles(rooms[i].blX, rooms[i].blY, rooms[i].roomWidth, rooms[i].roomHeight, Tileset.FLOOR );
        }
    }

    private void tileHalls() {
        for (int i = 0; i < hallCount; i += 1) {
            int blX = Math.min(halls[i].startX, halls[i].endX());
            int blY = Math.min(halls[i].startY, halls[i].endY());
            int trX = Math.max(halls[i].startX, halls[i].endX());
            int trY = Math.max(halls[i].startY, halls[i].endY());
            fillWithTiles(blX, blY, trX - blX + 1, trY - blY + 1, Tileset.FLOOR );
        }
    }

    private void addWalls() {
        for (int i = 1; i < WIDTH - 1; i += 1) {
            for (int j = 1; j < HEIGHT - 1; j += 1) {
                if (tiles[i][j].equals(Tileset.FLOOR)) {
                    if (tiles[i - 1][j].equals(Tileset.NOTHING)) {
                        tiles[i - 1][j] = Tileset.WALL;
                    }
                    if (tiles[i + 1][j].equals(Tileset.NOTHING)) {
                        tiles[i + 1][j] = Tileset.WALL;
                    }
                    if (tiles[i][j - 1].equals(Tileset.NOTHING)) {
                        tiles[i][j - 1] = Tileset.WALL;
                    }
                    if (tiles[i][j + 1].equals(Tileset.NOTHING)) {
                        tiles[i][j + 1] = Tileset.WALL;
                    }
                }
            }
        }
        for (int i = 1; i < WIDTH; i += 1) {
            for (int j = 1; j < HEIGHT; j += 1) {
                if (tiles[i - 1][j - 1].equals(Tileset.WALL) && tiles[i][j].equals(Tileset.WALL)) {
                    if (tiles[i][j - 1].equals(Tileset.FLOOR) && tiles[i - 1][j].equals(Tileset.NOTHING)) {
                        tiles[i - 1][j] = Tileset.WALL;
                    }
                    if (tiles[i][j - 1].equals(Tileset.NOTHING) && tiles[i - 1][j].equals(Tileset.FLOOR)) {
                        tiles[i][j - 1] = Tileset.WALL;
                    }
                }
                if (tiles[i - 1][j].equals(Tileset.WALL) && tiles[i][j - 1].equals(Tileset.WALL)) {
                    if (tiles[i - 1][j - 1].equals(Tileset.FLOOR) && tiles[i][j].equals(Tileset.NOTHING)) {
                        tiles[i][j] = Tileset.WALL;
                    }
                    if (tiles[i - 1][j - 1].equals(Tileset.NOTHING) && tiles[i][j].equals(Tileset.FLOOR)) {
                        tiles[i - 1][j - 1] = Tileset.WALL;
                    }
                }

            }
        }
    }

    private void addPlayer() {
        while (!tiles[playerX][playerY].equals(Tileset.FLOOR)) {
            playerX = RandomUtils.uniform(random, 0, WIDTH);
            playerY = RandomUtils.uniform(random, 0, HEIGHT);
        }
        tiles[playerX][playerY] = Tileset.PLAYER;
    }

    private void addLockedDoor() {
        int x = 0;
        int y = 0;
        while (!tiles[x][y].equals(Tileset.WALL)) {
            x = RandomUtils.uniform(random, 0, WIDTH);
            y = RandomUtils.uniform(random, 0, HEIGHT);
        }
        tiles[x][y] = Tileset.LOCKED_DOOR;
    }


    public void start() {
        addRoomsAndHalls();
        tileRooms();
        tileHalls();
        addWalls();
        addPlayer();
        addLockedDoor();
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        ter.renderFrame(tiles);
    }
}
