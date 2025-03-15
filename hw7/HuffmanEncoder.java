import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {

        HashMap<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (frequencyTable.containsKey(c)) {
                frequencyTable.put(c, frequencyTable.get(c) + 1);
            } else {
                frequencyTable.put(c, 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {

        // Read the file as 8 bit symbols.
        char[] inputSymbols = FileUtils.readFile(args[0]);

        // Build frequency table.
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);

        // Use frequency table to construct a binary decoding trie.
        BinaryTrie bt = new BinaryTrie(frequencyTable);

        // Write the binary decoding trie to the .huf file.
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(bt);

        // write the number of symbols to the .huf file
        ow.writeObject(inputSymbols.length);

        // Use binary trie to create lookup table for encoding.
        Map<Character, BitSequence> lookUpTable = bt.buildLookupTable();

        // Create a list of bitsequences.
        LinkedList<BitSequence> list = new LinkedList<>();

        // For each 8 bit symbol:
        //    Lookup that symbol in the lookup table.
        //    Add the appropriate bit sequence to the list of bitsequences.
        for (Character c : inputSymbols) {
            BitSequence bs = lookUpTable.get(c);
            list.add(bs);
        }

        // Assemble all bit sequences into one huge bit sequence.
        BitSequence fileBS = BitSequence.assemble(list);

        // Write the huge bit sequence to the .huf file.
        ow.writeObject(fileBS);
    }



}
