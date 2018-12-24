package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 85;
    public static final int HEIGHT = 47;
    private TETile[][] tiles = new TETile[Map.WIDTH][Map.HEIGHT];
    private int pX;
    private int pY;
    private int mX;
    private int mY;
    private int moveNum;
    private boolean gameOn;
    private String hud;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    private void drawFrame(String s) {
        StdDraw.clear(Color.black);
        Font font = new Font("Monaco", Font.BOLD, 32);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }
    private void drawStartPage() {
        StdDraw.clear(Color.black);
        Font font = new Font("Monaco", Font.BOLD, 32);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 4, "CS61B: THE GAME");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }

    private long solicitSeed() {
        String tipStr = "Please enter a seed: ";
        String seedStr = "";
        long seed;
        drawFrame(tipStr + seedStr);
        while (moveNum < 1000) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = StdDraw.nextKeyTyped();
            if (c == 'S' || c == 's') {
                break;
            }
            if (c - '0' < 0 || c - '0' > 9) {
                throw new IllegalArgumentException("Illegal seed.");
            }
            seedStr += c;
            drawFrame(tipStr + seedStr);
        }
        // if no seed typed, use 0 instead.
        if (seedStr.length() == 0) {
            seed = 0;
        } else {
            seed = Long.parseLong(seedStr);
        }
        return seed;
    }

    private void gameStart() {
        gameOn = true;
        mX = WIDTH;
        mY = HEIGHT;
        hud = "";
    }

    private void initializeGame(long seed) {
        Map world = new Map(seed);
        world.start();
        tiles = world.tiles;
        pX = world.playerX;
        pY = world.playerY;
        moveNum = 0;
        gameStart();
    }

    public void moveUp() {
        if (tiles[pX][pY + 1].equals(Tileset.FLOOR)) {
            tiles[pX][pY + 1] = Tileset.PLAYER;
            tiles[pX][pY] = Tileset.FLOOR;
            pY += 1;
            moveNum += 1;
        }
    }

    public void moveDown() {
        if (tiles[pX][pY - 1].equals(Tileset.FLOOR)) {
            tiles[pX][pY - 1] = Tileset.PLAYER;
            tiles[pX][pY] = Tileset.FLOOR;
            pY -= 1;
            moveNum += 1;
        }
    }
    public void moveRight() {
        if (tiles[pX + 1][pY].equals(Tileset.FLOOR)) {
            tiles[pX + 1][pY] = Tileset.PLAYER;
            tiles[pX][pY] = Tileset.FLOOR;
            pX += 1;
            moveNum += 1;
        }
    }

    public void moveLeft() {
        if (tiles[pX - 1][pY].equals(Tileset.FLOOR)) {
            tiles[pX - 1][pY] = Tileset.PLAYER;
            tiles[pX][pY] = Tileset.FLOOR;
            pX -= 1;
            moveNum += 1;
        }
    }

    private void saveWorld() {
        try {
            FileOutputStream fileOut = new FileOutputStream(".\\byog\\Core\\savedWorld.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tiles);
            out.writeInt(pX);
            out.writeInt(pY);
            out.writeInt(moveNum);
            out.close();
            fileOut.close();
            System.out.println("The world has been saved.");

        } catch (IOException e) {
            System.out.println("The world cannot be saved.");
        }
    }

    private void loadWorld() {
        try {
            FileInputStream fileIn = new FileInputStream(".\\byog\\Core\\savedWorld.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tiles = (TETile[][]) in.readObject();
            pX = in.readInt();
            pY = in.readInt();
            moveNum = in.readInt();
            in.close();
            fileIn.close();
            System.out.println("The world has been loaded.");

        } catch (IOException e) {
            System.out.println("The world cannot be loaded.");

        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException is caught.");
        }
    }

    private void move(char c) {
        if (c == 'W' || c == 'w') {
            moveUp();
        }
        if (c == 'S' || c == 's') {
            moveDown();
        }
        if (c == 'D' || c == 'd') {
            moveRight();
        }
        if (c == 'A' || c == 'a') {
            moveLeft();
        }
    }

    private void seriesMove(String s) {
        if (s.length() > 0) {
            for (char c : s.toCharArray()) {
                move(c);
            }
        }
    }

    private String getMotions(String input, int start) {
        String motion = "";
        for (int j = start; j < input.length(); j += 1) {
            char c = input.charAt(j);
            if (c == ':') {
                break;
            }
            motion += c;
        }
        return motion;

    }

    private void updateHudStr() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if ((!(x == mX && y == mY)) && x < tiles.length && y < tiles[0].length) {
            hud = tiles[x][y].description();
        }
        mX = x;
        mY = y;
    }


    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        drawStartPage();
        while (moveNum < 1000) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char selection = StdDraw.nextKeyTyped();
            if (selection == 'N' || selection == 'n') {
                Long seed = solicitSeed();
                initializeGame(seed);
            }
            if (selection == 'L' || selection == 'l') {
                loadWorld();
                gameStart();
            }
            ter.renderFrame(tiles, "", pX, pY);
            while (moveNum < 1000) {
                updateHudStr();
                if (hud.length() > 0 && gameOn) {
                    ter.renderFrame(tiles, hud, pX, pY);
                }
                if (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                char c = StdDraw.nextKeyTyped();
                if (c == ':') {
                    continue;
                }
                if (c == 'Q' || c == 'q') {
                    saveWorld();
                    gameOn = false;
                    drawFrame("Quit and save!");
                    break;

                }
                move(c);
                ter.renderFrame(tiles, hud, pX, pY);
            }
        }
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        String newRegex = "(?i)n[\\d]+s[wsad]*(:q)?";
        String loadRegex = "(?i)l[wsad]*(:q)?";
        String motion;
        if (!(input.matches(newRegex) || input.matches(loadRegex))) {
            throw new IllegalArgumentException("The input string is illegal.");
        }
        if (input.matches(newRegex)) {
            String seedStr = "";
            int seedEndId = 1;
            for (int i = 1; i < input.length(); i += 1) {
                char cur = input.charAt(i);
                if (cur - '0' < 0 || cur - '0' > 9) {
                    break;
                }
                seedEndId = i;
                seedStr += cur;

            }
            long seed = Long.parseLong(seedStr);
            initializeGame(seed);
            motion = getMotions(input, seedEndId + 2);
        } else {
            motion = getMotions(input, 1);
            loadWorld();
        }
        seriesMove(motion);
        if (motion.charAt(motion.length() - 1) != input.charAt(input.length() - 1)) {
            saveWorld();
        }
        return tiles;
    }
}
