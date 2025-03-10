import java.util.TreeMap;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // 插入单词到Trie中
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

    // 检查单词是否存在于Trie中
    public boolean contains(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    // 检查是否存在以prefix为前缀的单词
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    // 辅助方法：查找前缀对应的最后一个节点
    public TrieNode searchPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.containsKey(ch)) {
                return null;
            }
            node = node.get(ch);
        }
        return node;
    }

    // Trie节点内部类
    public static class TrieNode {
        private final TreeMap<Character, TrieNode> children;
        private boolean isEnd;

        public TrieNode() {
            children = new TreeMap<>();
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
}


