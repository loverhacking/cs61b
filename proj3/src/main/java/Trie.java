import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private TrieNode root;
    private String matchPrefex;

    public Trie() {
        root = new TrieNode();
        matchPrefex = "";
    }

    /** inset word into Trie */
    public void add(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsKey(ch)) {
                node.put(ch, new TrieNode());
            }
            node = node.get(ch);
        }
        node.setEnd();
    }

    /** check whether word in Trie */
    public boolean contains(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    /** Check for the presence of words with prefix */
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    /** return all words start with the given prefix */
    public List<String> getAllWordsWithPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = searchPrefix(prefix);
        if (node != null) {
            collectWords(node, matchPrefex, results);
        }
        return results;
    }

    private void collectWords(TrieNode node, String currentPrefix, List<String> results) {
        if (node.isEnd) {
            results.add(currentPrefix);
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            Character ch = entry.getKey();
            TrieNode child = entry.getValue();
            collectWords(child, currentPrefix + ch, results);

        }
    }

    /** Find the last node corresponding to the prefix */
    private TrieNode searchPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.containsKey(ch)
                    && !node.containsKey(Character.toUpperCase(ch))) {
                return null;
            }
            StringBuilder s = new StringBuilder();
            s.append(matchPrefex);

            if (node.containsKey(Character.toUpperCase(ch))) {
                node = node.get(Character.toUpperCase(ch));
                s.append(Character.toUpperCase(ch));
            } else {
                node = node.get(ch);
                s.append(ch);
            }
            matchPrefex = s.toString();

        }
        return node;
    }

    // Trie class
    private static class TrieNode {
        private final HashMap<Character, TrieNode> children;
        private boolean isEnd;

        public TrieNode() {
            children = new HashMap<>();
            isEnd = false;
        }

        public boolean containsKey(char ch) {
            return children.containsKey(ch);
        }

        public TrieNode get(char ch) {
            return children.get(ch);
        }

        public void put(char ch, TrieNode node) {
            children.put(ch, node);
        }

        public void setEnd() {
            isEnd = true;
        }

        public boolean isEnd() {
            return isEnd;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.add("App");
        trie.add("Apple");
        trie.add("Applica tion");
        trie.add("Apply");
        System.out.println(trie.getAllWordsWithPrefix("app"));
    }
}


