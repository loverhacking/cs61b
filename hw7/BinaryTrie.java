import java.io.Serializable;
import java.util.*;



public class BinaryTrie implements Serializable {

    /**
     * Given a frequency table which maps symbols of type V
     * to their relative frequencies, build a Huffman decoding trie
     */

    private static Node root;
    private static Set<Character> charSet;

    private static class Node implements Comparable<Node> {

        private final char c;
        private final int freq;
        private final Node left, right;

        Node(char c, int freq, Node left, Node right) {
            this.c = c;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node o) {
            return this.freq - o.freq;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            //assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {

        PriorityQueue<Node> pq = new PriorityQueue<>();
        charSet = frequencyTable.keySet();
        for (Character c : charSet) {
            pq.add(new Node(c, frequencyTable.get(c), null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        root = pq.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node copy = root;

        if (copy == null) {
            return null;
        }

        int i;
        for (i = 0; i < querySequence.length(); i++) {

            if (copy.isLeaf()) {
                break;
            }

            if (querySequence.bitAt(i) == 0) {
                copy = copy.left;
            } else {
                copy = copy.right;
            }
        }
        return new Match(querySequence.firstNBits(i), copy.c);
    }

    public Map<Character, BitSequence> buildLookupTable() {

        HashMap<Character, BitSequence> lookupTable = new HashMap<>();
        Node copy = root;
        for (char c: charSet) {
            BitSequence b = new BitSequence(find(c, copy, ""));
            lookupTable.put(c, b);
        }

        return lookupTable;
    }

    private static String find(Character c, Node node, String s) {
        if (node.c == c) {
            return s;
        }

        if (node.isLeaf()) {
            return add(s, 2);
        }

        String bLeft = find(c, node.left, add(s, 0));
        String bRight = find(c, node.right, add(s, 1));

        if (bLeft.charAt(bLeft.length() - 1) == '2') {
            return bRight;
        } else {
            return bLeft;
        }
    }

    private static String add(String s, int a) {
        StringBuilder result = new StringBuilder();
        result.append(s);
        result.append(a);
        return result.toString();
    }
    
}
