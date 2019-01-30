import java.util.ArrayList;

public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Not enough arguments.");
        }
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie binaryTrie = (BinaryTrie) or.readObject();
        int numOfSymbols = (int) or.readObject();
        BitSequence bs = (BitSequence) or.readObject();
        ArrayList<Character> chars = new ArrayList<>();
        while (bs.length() > 0) {
            Match match = binaryTrie.longestPrefixMatch(bs);
            chars.add(match.getSymbol());
            bs = bs.allButFirstNBits(match.getSequence().length());
        }
        int len = chars.size();
        char[] charArray = new char[len];
        for (int i = 0; i < len; i += 1) {
            charArray[i] = chars.get(i);
        }
        FileUtils.writeCharArray(args[1], charArray);
    }
}
