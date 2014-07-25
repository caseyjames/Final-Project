package FinalProject;

import java.io.PrintWriter;

/**
 * Created by nordgran on 7/25/2014.
 */
@SuppressWarnings("UnusedDeclaration")
public class Dictionary {

    private ChainingHashTable<BinaryNode> dictionary;


    private static String spellCheck(String word, boolean fileWrite) {
        int alternateCount = 0;

        if (dictionary.contains(word))
            return word;
        PrintWriter writeFile = null;
        if (fileWrite) {
            try {
                writeFile = new PrintWriter("" + word + ".txt");
                writeFile.append("User string: ").append(word).append("\n\n");
            } catch (Exception e) {
                System.err.println("Unable to write file!");
                return"";
            }
        }

        // for loop for deletion alternative
        int i;
        for (i = 0; i < word.length(); i++) {
            StringBuilder temp = new StringBuilder(word);
            temp.deleteCharAt(i);
            String newWord = temp.toString();
            if (fileWrite) {
                writeFile.append("Deletion string: ").append(newWord).append("\n");
            }
            alternateCount++;
        }
        if (fileWrite) writeFile.append("Created ").append((char) i).append(" deletion alternatives\n\n");

        // for loop for transposition alternatives
        for (i = 0; i < word.length() - 1; i++) {
            StringBuilder temp = new StringBuilder(word);
            char tempChar = temp.charAt(i);

            temp.setCharAt(i, temp.charAt(i + 1));
            temp.setCharAt(i + 1, tempChar);

            String newWord = temp.toString();

            if (fileWrite) {
                writeFile.append("Transposition string: ").append(newWord).append("\n");
            }
            alternateCount++;
        }
        if (fileWrite)
            writeFile.print("Created " + i + " transposition alternatives\n\n");


        for (i = 0; i < word.length() - 1; i++) {
            StringBuilder temp = new StringBuilder(word);
            char tempChar = temp.charAt(i);

            temp.setCharAt(i, temp.charAt(i + 1));
            temp.setCharAt(i + 1, tempChar);

            String newWord = temp.toString();

            if (fileWrite) {
                writeFile.append("Transposition string: ").append(newWord).append("\n");
            }
            //TODO check frequency
            alternateCount++;
        }
        if (fileWrite)
            writeFile.print("Created "+i+" transposition alternatives\n\n");


        for (i = 0; i <= word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder temp = new StringBuilder(word);
                temp.setCharAt(i, (char) (i + 97));

                String newWord = temp.toString();

                if (fileWrite) {
                    writeFile.append("Transposition string: ").append(newWord).append("\n");
                }
                alternateCount++;
            }
        }
        if (fileWrite)
            writeFile.print("Created "+i+" transposition alternatives\n\n");

        for (i = 0; i <= word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder temp = new StringBuilder(word);
                temp.insert(i, (char) (i + 97));

                String newWord = temp.toString();

                if (fileWrite) {
                    writeFile.append("Transposition string: ").append(newWord).append("\n");
                }
                alternateCount++;
            }
        }
        if (fileWrite)
            writeFile.print("Created "+i+" transposition alternatives\n\n");


        return"";
    }

}
