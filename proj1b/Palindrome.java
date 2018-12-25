public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    private String dequeToWord(Deque<Character> wordDeque) {
        String word = "";
        while (!wordDeque.isEmpty()) {
            word += wordDeque.removeFirst();
        }
        return word;
    }
    public boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        if (wordDeque.isEmpty() || wordDeque.size() == 1) {
            return true;
        } else {
            return wordDeque.removeFirst() == wordDeque.removeLast()
                    && isPalindrome(dequeToWord(wordDeque));
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);
        if (wordDeque.isEmpty() || wordDeque.size() == 1) {
            return true;
        } else {
            return cc.equalChars(wordDeque.removeFirst(), wordDeque.removeLast())
                    && isPalindrome(dequeToWord(wordDeque), cc);
        }
    }


}
