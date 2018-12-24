package byog.lab6;

import byog.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(50, 40, seed);
        game.startGame();

    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String out = "";
        for (int i = 0; i < n; i += 1) {
            int index = RandomUtils.uniform(rand, 0, 26);
            out += CHARACTERS[index];
        }
        return out;
    }

    public void drawFrame(String s, int ms) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        if (!gameOver) {
            StdDraw.textLeft(0, height - 1, "Round: " + round);
            String tempStr;
            if (!playerTurn) {
                tempStr = "Watch!";
            } else {
                tempStr = "Type!";
            }
            StdDraw.text(width / 2, height - 1, tempStr);
            int index = RandomUtils.uniform(rand, 0, 7);
            StdDraw.textRight(width, height - 1, ENCOURAGEMENT[index]);
            StdDraw.line(0, height - 2, width, height - 2);
        }
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
        StdDraw.pause(ms);
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        int n = letters.length();
        for (int i = 0; i < n; i += 1) {
            drawFrame("", 500);
            drawFrame(String.valueOf(letters.charAt(i)), 1000);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String out = "";
        int i = 0;
        while (i < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char curChar = StdDraw.nextKeyTyped();
                out += curChar;
                drawFrame(out, 1000);
                i += 1;
            }
        }
        return out;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        //TODO: Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round, 1000);
            String randStr =  generateRandomString(round);
            flashSequence(randStr);
            playerTurn = true;
            String inputStr = solicitNCharsInput(round);
            if (inputStr.equals(randStr)) {
                round += 1;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round, 1000);
            }
        }

    }

}
