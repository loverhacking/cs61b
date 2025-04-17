import java.util.Comparator;
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


    /** a stack used to simulate DFS using iterative method */
    private static LinkedList<Direction> stack;

    // File path of dictionary file
    static String dictPath = "words.txt";


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
        Trie t = new Trie();
        In wordFile = new In(dictPath);
        while (wordFile.hasNextLine()) {
            String a = wordFile.readLine();
            t.add(a);
        }

        matchWordList = new LinkedList<>();

        stack = new LinkedList<>();
        visited = new boolean[M][N];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                String s = String.valueOf(boardArray[i][j]);
                if (t.startsWith(s)) {
                    Trie.TrieNode node = t.searchPrefix(s);
                    stack.add(new Direction(i, j, false, s, node));
                    solveHelper(k);
                }
            }
        }
        
        return matchWordList;
    }

    private static void solveHelper(int k) {

        while (!stack.isEmpty()) {
            Direction d = stack.removeLast();

            int dx = d.getX();
            int dy = d.getY();
            String value = d.getValue();
            Trie.TrieNode node = d.getNode();

            /**
             * divide the search into 2 phases:
             * search phase and backtracking phase
             * if it is in search phase, push it into stack immediately
             * if it is in backtracking phase, i.e. when all of its child node
             * finish searching,set it unvisited.
             * Note: consider stack First In Last Out to simulate DFS iteratively
             * and more importantly, to avoid the shared visited matrix messy.
             */
            if (d.isBacktrack()) {
                visited[dx][dy] = false;
                continue;
            }
            stack.addLast(new Direction(dx, dy, true, value, node));

            visited[dx][dy] = true;

            // exist matched word and record it into matchWordList
            if (value.length() >= 3 && node != null && node.isEnd()
                    && !matchWordList.contains(value)) {
                matchWordList.add(value);
                setWordList(k);
            }

            // search all the neighbors
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    // skip itself
                    if (i == 0 && j == 0) {
                        continue;
                    }

                    int ddx = dx + i;
                    int ddy = dy + j;

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
                    Trie.TrieNode nextNode = node.get(c);
                    if (nextNode == null) {
                        continue;
                    }

                    stack.addLast(new Direction(ddx, ddy,
                            false, addString(value, c), nextNode));
                }
            }
        }

    }

    private static void setWordList(int k) {
        matchWordList.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                if (o1.length() != o2.length()) {
                    return -(o1.length() - o2.length());
                }
                return o1.compareTo(o2);
            }
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



    private static String addString(String word, char c) {

        StringBuilder sb = new StringBuilder();
        sb.append(word);
        sb.append(c);
        return sb.toString();
    }

    private static boolean checkInArray(int i, int j) {
        return i >= 0 && i < M && j >= 0 && j < N;
    }

}



