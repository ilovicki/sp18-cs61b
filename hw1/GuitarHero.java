import synthesizer.GuitarString;
public class GuitarHero {
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final double CONCERT_A = 440.0;




    public static void main(String[] args) {
        GuitarString[] strings = new GuitarString[37];
        for (int i = 0; i < 37; i += 1) {
            double concert = CONCERT_A * Math.pow(2, (i - 24.0) / 12.0);
            strings[i] = new GuitarString(concert);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);
                if (index > -1) {
                    strings[index].pluck();
                }

            }
            double sample = 0.0;
            for (int i = 0; i < 37; i += 1) {
                sample += strings[i].sample();
            }


            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i += 1) {
                strings[i].tic();
            }
        }
    }

}
