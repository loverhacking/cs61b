public class Palindrome {

    /** convert String word to Deque */
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> wordDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            wordDeque.addLast(ch);
        }
        return wordDeque;
    }

    private boolean isPalindromeHelper(Deque<Character> wordDeque) {

        if (wordDeque.isEmpty() || wordDeque.size() == 1) {
            return true;
        }

        char first = wordDeque.removeFirst();
        char last = wordDeque.removeLast();

        if (first != last) {
            return false;
        }
        return isPalindromeHelper(wordDeque);

    }
    /** decide if the given word is palindrome. */
    public boolean isPalindrome(String word) {

        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindromeHelper(wordDeque);
    }

    private boolean isPalindromeHelper(Deque<Character> wordDeque, CharacterComparator cc) {
        if (wordDeque.isEmpty() || wordDeque.size() == 1) {
            return true;
        }

        char first = wordDeque.removeFirst();
        char last = wordDeque.removeLast();

        if (!cc.equalChars(first, last)) {
            return false;
        }
        return isPalindromeHelper(wordDeque, cc);
    }

    /** overload isPalindrome if the word is a palindrome according to the character comparison test
     * provided by the CharacterComparator passed in as argument cc
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindromeHelper(wordDeque, cc);
    }

}
