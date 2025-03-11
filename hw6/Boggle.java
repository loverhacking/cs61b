import java.util.*;

public class Boggle {


    /** change board to char[][] array with size M * N */
    private static char[][] boardArray;


    /** the length of boardArray */
    private static int M;

    /** the width of boardArray */
    private static int N;

    private static boolean[][] visited;

    private static LinkedList<String> matchWordSet;




    private static LinkedList<Direction> list;

    private static Trie t;

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
        // YOUR CODE HERE


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

        t = new Trie();
        In wordFile = new In(dictPath);
        while (wordFile.hasNextLine()) {
            String a = wordFile.readLine();
            t.add(a);
        }

        matchWordSet = new LinkedList<>();

        list = new LinkedList<>();
        visited = new boolean[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                String s = String.valueOf(boardArray[i][j]);
                if (t.startsWith(s)) {
                    Trie.TrieNode node = t.searchPrefix(s);
                    list.add(new Direction(i, j, false, s, node));
                    solveHelper(k);
                }
            }
        }


        return matchWordSet;
    }

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
        return;
    }

    private static void solveHelper(int k) {


        while (!list.isEmpty()) {
            Direction d = list.removeLast();

            int dx = d.getX();
            int dy = d.getY();
            String value = d.getValue();
            Trie.TrieNode node = d.getNode();

            if (d.isBacktrack()) {
                visited[dx][dy] = false;
                continue;
            }

            visited[dx][dy] = true;


            if (value.length() >= 3 && node != null && node.isEnd()
                    && !matchWordSet.contains(value)) {
                matchWordSet.addLast(value);
                maintainSet(k);
            }

            list.addLast(new Direction(dx, dy, true, value, node));

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    if (i == 0 && j == 0) {
                        continue;
                    }

                    int ddx = dx + i;
                    int ddy = dy + j;



                    if (!checkInArray(ddx, ddy)) {
                        continue;
                    }

                    if (visited[ddx][ddy]) {
                        continue;
                    }

                    char c = boardArray[ddx][ddy];
                    Trie.TrieNode nextNode = node.get(c);
                    if (nextNode == null) {
                        continue;
                    }

                    list.addLast(new Direction(ddx, ddy,
                            false, addString(value, c), nextNode));
                }
            }
        }

    }

    private static void maintainSet(int k) {
        matchWordSet.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() != o2.length()) {
                    return -(o1.length() - o2.length());
                }
                return o1.compareTo(o2);
            }
        });
        if (matchWordSet.size() > k) {
            matchWordSet.removeLast();
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



