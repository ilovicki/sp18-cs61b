public class HorribleSteve {
    public static void main(String [] args) {
        int i = 0;
        for (int j = 0; j < 500; j += 1) {
            if (!Flik.isSameNumber(i, j)) {
                break; // break exits the for loop!
            }
            i += 1;
        }
        System.out.println("i is " + i);
    }
}
