

public class HuffmanDecoder {
    public static void main(String[] args) {

        // Read the Huffman coding trie.
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie bt = (BinaryTrie) or.readObject();

        int numSymbols = (int) or.readObject();
        char[] symbols = new char[numSymbols];

        // Read the massive bit sequence corresponding to the original txt.
        BitSequence fileBS = (BitSequence) or.readObject();



        for (int i = 0; i < numSymbols; i++) {
            Match temp = bt.longestPrefixMatch(fileBS);

            BitSequence tempBS = temp.getSequence();
            symbols[i] = temp.getSymbol();

            BitSequence newBS = fileBS.allButFirstNBits(tempBS.length());
            fileBS = newBS;
        }

        FileUtils.writeCharArray(args[1], symbols);


    }


}
