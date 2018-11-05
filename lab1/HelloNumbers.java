public class HelloNumbers {
    public static void main(String[] args) {
       for(int i = 0; i < 10; i += 1){
           int sum = 0;
           for (int j = 0; j <= i; j += 1){
               sum = sum + j;
           }
           System.out.print(sum + " ");
       }
    }
}
