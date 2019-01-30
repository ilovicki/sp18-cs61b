import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freqTable = new HashMap<>();
        for (char c: inputSymbols) {
            if (!freqTable.containsKey(c)) {
                freqTable.put(c, 1);
            } else {
                freqTable.put(c, freqTable.get(c) + 1);
            }
        }
        return freqTable;
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Found no argument.");
        }
        char[] input = FileUtils.readFile(args[0]);
        Map<Character, Integer> freTable = buildFrequencyTable(input);
        BinaryTrie binaryTrie = new BinaryTrie(freTable);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(binaryTrie);
        ow.writeObject(freTable.size());
        Map<Character, BitSequence> lookupTable = binaryTrie.buildLookupTable();
        List<BitSequence> bitSequences = new ArrayList<>();
        for (char c: input) {
            bitSequences.add(lookupTable.get(c));
        }
        BitSequence encoded = BitSequence.assemble(bitSequences);
        ow.writeObject(encoded);
    }
}
