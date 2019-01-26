package lab14;
import lab14lib.Generator;
import lab14lib.MultiGenerator;
import lab14lib.GeneratorAudioVisualizer;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
		/** Your code here. */
        Generator generator = new StrangeBitwiseGenerator(1024);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(128000, 1000000);
	}
} 