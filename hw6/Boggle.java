
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Boggle {

    /** change board to char[][] array with size M * N */
    private static char[][] boardArray;

    /** the length of boardArray */
    private static int M;

    /** the width of boardArray */
    private static int N;

    /** record the char in board whether be visited */
    private static boolean[][] visited;

    /** record the desired k word */
    private static LinkedList<String> matchWordList;

    private static int uid = 0;

    /** a stack used to simulate DFS using iterative method */
    private static LinkedList<Node> stack;

    // File path of dictionary file
    static String dictPath = "words.txt";

    private static class TrieNode {
        // notice that the alphabet consists of only the 26 letters A through Z
        final HashMap<Character, TrieNode> children = new HashMap<>();
        // represent the current getAllValidWords() call id to find whether the word has been found
        int uid;
        // build string in trie node to avoid frequently building strings in dfs
        String word;
    }

    private static class Node {
        int x;
        int y;

        /** whether the node is in backtracking stage */
        boolean isBacktrack;

        /** record the location node in Trie */
        TrieNode node;

        Node(int x, int y, boolean backtrack, TrieNode node) {
            this.x = x;
            this.y = y;
            this.isBacktrack = backtrack;
            this.node = node;
        }
    }

    /**
     * Solves a Boggle puzzle.
     *
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {

        uid++;

        TrieNode root = new TrieNode();
        In inputFile = new In(boardFilePath);
        if (k < 0 || !inputFile.exists()) {
            throw new IllegalArgumentException();
        }

        // change every line in txt to a element in String[]
        String[] strings = inputFile.readAllStrings();
        checkIsRectangular(strings);

        M = strings.length;
        N = strings[0].length();

        // change board to char[][] array
        boardArray = new char[M][N];

        for (int i = 0; i < M; i++) {
            boardArray[i] = strings[i].toCharArray();
        }

        /** a Trie to recode WordDict */
        In wordFile = new In(dictPath);
        while (wordFile.hasNextLine()) {
            String a = wordFile.readLine();
            if (a.length() >= 3) {
                insert(root, a);
            }
        }

        matchWordList = new LinkedList<>();

        stack = new LinkedList<>();
        visited = new boolean[M][N];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                TrieNode node = root.children.get(boardArray[i][j]);
                if (node != null) {
                    stack.add(new Node(i, j, false, node));
                    solveHelper(k);
                }
            }
        }
        return matchWordList;
    }

    private static void insert(TrieNode root, String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.word = word;
    }

    private static void solveHelper(int k) {

        while (!stack.isEmpty()) {
            Node d = stack.removeLast();
            /**
             * divide the search into 2 phases:
             * search phase and backtracking phase
             * if it is in search phase, push it into stack immediately
             * if it is in backtracking phase, i.e. when all of its child node
             * finish searching,set it unvisited.
             * Note: consider stack First In Last Out to simulate DFS iteratively
             * and more importantly, to avoid the shared visited matrix messy.
             */
            if (d.isBacktrack) {
                visited[d.x][d.y] = false;
                continue;
            }
            stack.addLast(new Node(d.x, d.y, true, d.node));

            visited[d.x][d.y] = true;

            // exist matched word and record it into matchWordList
            if (d.node.word != null && d.node.uid != uid) {
                matchWordList.add(d.node.word);
                d.node.uid = uid;
                setWordList(k);
            }

            // search all the neighbors
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    // skip itself
                    if (i == 0 && j == 0) {
                        continue;
                    }

                    int ddx = d.x + i;
                    int ddy = d.y + j;

                    // check neighbors in boardArray
                    if (!checkInArray(ddx, ddy)) {
                        continue;
                    }

                    if (visited[ddx][ddy]) {
                        continue;
                    }

                    /**
                     * pruning: if no matched prefix of given wordDict,
                     * then stop exploring the neighbor nodes.
                     */
                    char c = boardArray[ddx][ddy];
                    TrieNode nextNode = d.node.children.get(c);
                    if (nextNode == null) {
                        continue;
                    }

                    stack.addLast(new Node(ddx, ddy, false, nextNode));
                }
            }
        }

    }

    private static void setWordList(int k) {
        matchWordList.sort((o1, o2) -> {
            if (o1.length() != o2.length()) {
                return -(o1.length() - o2.length());
            }
            return o1.compareTo(o2);
        });

        if (matchWordList.size() > k) {
            matchWordList.removeLast();
        }

    }

    /** check the given board is rectangular */
    private static void checkIsRectangular(String[] strings) {
        if (strings.length < 2) {
            return;
        }
        int length = strings[0].length();
        for (int i = 1; i < strings.length; i++) {
            if (length != strings[i].length()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static boolean checkInArray(int i, int j) {
        return i >= 0 && i < M && j >= 0 && j < N;
    }

    public static void main(String[] args) {
        System.out.println(Boggle.solve(7, "exampleBoard.txt"));
    }

}



