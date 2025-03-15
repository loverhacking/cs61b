

public class HuffmanDecoder {
    public static void main(String[] args) {

        // Read the Huffman coding trie.
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie bt = (BinaryTrie) or.readObject();

        // read the number of symbols.
        int numSymbols = (int) or.readObject();
        char[] symbols = new char[numSymbols];

        // Read the massive bit sequence corresponding to the original txt.
        BitSequence fileBS = (BitSequence) or.readObject();

        // Repeat until there are no more symbols:
        //    4a: Perform a longest prefix match on the massive sequence.
        //    4b: Record the symbol in some data structure.
        //    4c: Create a new bit sequence containing the remaining unmatched bits.
        for (int i = 0; i < numSymbols; i++) {
            Match temp = bt.longestPrefixMatch(fileBS);

            BitSequence tempBS = temp.getSequence();
            symbols[i] = temp.getSymbol();

            fileBS = fileBS.allButFirstNBits(tempBS.length());
        }

        // Write the symbols in some data structure to the specified file.
        FileUtils.writeCharArray(args[1], symbols);


    }


}
