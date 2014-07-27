package FinalProject;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 *
 *
 *
 */
@SuppressWarnings("ConstantConditions")
public class Dictionary {
    private Node[] dictionary;

    public Dictionary(File inputFile, int tableSize) {
        dictionary = new Node[tableSize];
        Scanner inputStats = new Scanner("");
        try {
            inputStats = new Scanner(inputFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Scanner lineParse;
        String nextName;
        int nextFreq;

        while (inputStats.hasNextLine()) {
            lineParse = new Scanner(inputStats.nextLine());
            nextName = lineParse.next().toLowerCase();
            nextFreq = Integer.parseInt(lineParse.next());
            add(nextName, nextFreq);
        }
    }

    public String spellCheck(String word, boolean fileWrite) {
        if (word == null)
            return "";
        word = word.toLowerCase();
        int alternateCount = 0;
        String maxFreq = "";
        int i, j = 0;

        if (this.contains(word))
            return word;
        PrintWriter writeFile = null;
        if (fileWrite) {
            try {
                writeFile = new PrintWriter("" + word + ".txt");
                writeFile.print("User string: " + word + "\n");
            } catch (Exception e) {
                System.err.println("Unable to write file!");
                return "";
            }
        }

        // for loop for deletion alternative
        for (i = 0; i < word.length(); i++) {
            StringBuilder temp = new StringBuilder(word);
            temp.deleteCharAt(i);
            String newWord = temp.toString();
            if (fileWrite)
                writeFile.println("Deletion string: " + newWord);
            if (this.frequency(newWord) > this.frequency(maxFreq))
                maxFreq = newWord;
            alternateCount++;
        }
        if (fileWrite)
            writeFile.println("Created " + i + " deletion alternatives\n");

        // for loop for transposition alternatives
        for (i = 0; i < word.length() - 1; i++) {
            StringBuilder temp = new StringBuilder(word);
            char tempChar = temp.charAt(i);
            temp.setCharAt(i, temp.charAt(i + 1));
            temp.setCharAt(i + 1, tempChar);
            String newWord = temp.toString();
            if (fileWrite)
                writeFile.println("Transposition string: " + newWord);
            if (this.frequency(newWord) > this.frequency(maxFreq))
                maxFreq = newWord;
            alternateCount++;
        }
        if (fileWrite)
            writeFile.println("Created " + i + " transposition alternatives\n");
        
        // for loop for substitution alternatives
        for (i = 0; i < word.length(); i++) {
            for (j = 0; j < 26; j++) {
                StringBuilder temp = new StringBuilder(word);
                temp.setCharAt(i, (char) (i + 97));
                String newWord = temp.toString();
                if (fileWrite) {
                    writeFile.println("Substitution string: " + newWord);
                }
                if (this.frequency(newWord) > this.frequency(maxFreq))
                    maxFreq = newWord;
                alternateCount++;
            }
        }
        if (fileWrite)
            writeFile.println("Created " + (i * j) + " substitution alternatives\n");

        // for loops for insertion alternatives
        for (i = 0; i <= word.length(); i++) {
            for (j = 0; j < 26; j++) {
                StringBuilder temp = new StringBuilder(word);
                temp.insert(i, (char) (i + 97));
                String newWord = temp.toString();
                if (fileWrite)
                    writeFile.println("Insertion string: " + newWord);
                if (this.frequency(newWord) > this.frequency(maxFreq))
                    maxFreq = newWord;
                alternateCount++;
            }
        }
        if (fileWrite) {
            writeFile.println("Created " + (i * j) + " insertion alternatives\n");
            writeFile.println("TOTAL: generated " + alternateCount + " alternative spellings!");
            writeFile.close();
        }
        return maxFreq;
    }

    private void add(String newName, int newFreq) {
        int listIndex = Math.abs(newName.hashCode() % dictionary.length);
        if (dictionary[listIndex] == null)
            dictionary[listIndex] = new Node(newName, newFreq);
        else {
            Node current = dictionary[listIndex];
            while (current.next != null)
                current = current.next;
            current.next = new Node(newName, newFreq);
        }
    }

    private int frequency(String word) {
        int listIndex = Math.abs(word.hashCode() % dictionary.length);
        Node current = dictionary[listIndex];
        while (current != null) {
            if (current.getName().equals(word))
                return current.getFrequency();
            current = current.next;
        }
        return 0;
    }

    private boolean contains(String word) {
        int listIndex = Math.abs(word.hashCode() % dictionary.length);
        Node current = dictionary[listIndex];
        while (current != null) {
            if (current.getName().equals(word))
                return true;
            current = current.next;
        }
        return false;
    }

    private class Node {
        String name;
        int frequency;
        Node next;

        public Node(String _name, int _frequency) {
            name = _name;
            frequency = _frequency;
        }

        public String getName() {
            return name;
        }

        public int getFrequency() {
            return frequency;
        }
    }
}
