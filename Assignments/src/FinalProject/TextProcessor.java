package FinalProject;

import java.io.*;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 *
 *
 *
 */
@SuppressWarnings("UnusedDeclaration")
public class TextProcessor {

    private static Dictionary dictionary;

    public static void main(String[] args0) {
//        initializeComponents(args0[0]);
        initializeComponents("wordstats1.txt");
        if (dictionary == null)
            return;

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        Scanner lineParse;
        String input, input2;

        while (true) {
            System.out.println("Please choose from the following options:");
            System.out.println("1) Word spell check");
            System.out.println("2) File spell check");
            System.out.println("3) File compression");
            System.out.println("4) File decompression");
            System.out.println("5) File remote transfer\n");
            input = scanner.next();
            input2 = null;

            if (input.equals("1")) {
                System.out.println("Please enter a text word: ");
                input = scanner.next();
                if (scanner.hasNext()) {
                    input2 = scanner.next();
                }
                if (input2 != null && input2.equals("-f"))
                    spellcheckWord(input, true);
                else
                    spellcheckWord(input, false);

            } else if (input.equals("2")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.next();
                spellcheckFile(input, input2);
            } else if (input.equals("3")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                File inputFile = new File(input);
                if (!inputFile.isFile()) {
                    System.out.println(input + "is invalid for compression!\n");
                    continue;
                }
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.next();
                compressFile(input, input2);

            } else if (input.equals("4")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                File inputFile = new File(input);
                if (!inputFile.isFile()) {
                    System.out.println(input + "is invalid for decompression!\n");
                    continue;
                }
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.next();
                decompressFile(input, input2);

            } else if (input.equals("5")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                transmitFile(input, args0[0]);

            } else {
                if (input.equals("exit")) {
                    System.out.println("Thanks for using the text processor...");
                    return;
                } else {
                    System.out.println("Invalid option, please choose again:");
                }
            }
        }

    }

    public static void initializeComponents(String statsFile) {
        File inputFile = new File(statsFile);
        if (!inputFile.isFile()) {
            System.out.println("Invalid word stats file argument!\n");
            return;
        }

        dictionary = new Dictionary(inputFile, 2000);
// This driver method should first create a file from statsFile and check it for validity, then it should instantiates
// your spell checker component using the file, your compression component, and the device manager that is newly given below.
// Since your program is interactive, it is best to initialize all three in this method once for the remainder of the life of the program.
// If the statsFile is invalid then it should print the following message and return: Invalid word stats file argument!
// Please note that the above method should only be called once for the remainder of the life of the program. The rest of
// these following methods below will be called as many times as the user asks for with their respective option.
    }

    public static void spellcheckWord(String word, boolean fileWrite) {
        String returnedWord = dictionary.spellCheck(word, fileWrite);
        if (returnedWord != null) {
            if (returnedWord.equals(word))
                System.out.println("" + word + " is a known word!\n");
            else if (returnedWord.equals(""))
                System.out.println("" + word + " is an unknown word!\n");
            else
                System.out.println("" + word + " is an unknown word! " + returnedWord + " is a known word!\n");
        }
    }

    public static void spellcheckFile(String srcFile, String dstFile) {
        int message = 0;
        File inputFile = new File(srcFile);
        if (!inputFile.isFile()) {
            System.out.println(srcFile + "is invalid for spell correction!\n");
            return;
        }
        Scanner inputFileLine;
        try {
            inputFileLine = new Scanner(inputFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
//        File inputF = new File(dstFile);
        PrintWriter outputFile;
        try {
            outputFile = new PrintWriter(dstFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        while (inputFileLine.hasNextLine()) {
            String currentLine = inputFileLine.nextLine();
            String[] tokens = currentLine.split("\\b");
            String alternateWord;
            for (int i = 1; i < tokens.length; i++) {
                if (Character.isAlphabetic((tokens[i].codePointAt(0))) || Character.isDigit(tokens[i].charAt(0))) {
                    alternateWord = dictionary.spellCheck(tokens[i], false);
                    if (alternateWord != null) {
                        if (alternateWord.equals(tokens[i]))
                            outputFile.print(alternateWord);
                        else if (alternateWord.equals("")) {
                            message = 2;
                            outputFile.print(tokens[i]);
                        } else {
                            if (message != 2)
                                message = 1;
                            outputFile.print(alternateWord);
                        }
                    }
                } else {
                    outputFile.print(tokens[i]);
                }
            }
            outputFile.print("\n");
        }
        outputFile.close();

        if (message == 0) {
            System.out.println(srcFile + " contains words with correct spelling!\n\n");
//            inputF.delete();
        } else if (message == 1)
            System.out.println(srcFile + " was corrected successfully!\n\n");
        else
            System.out.println(srcFile + " was corrected, but it contains unknown words!\n\n");
    }

    public static void compressFile(String srcFile, String dstFile) {
        // declare & instantiate input file source
        File inputFile = new File(srcFile);
        if (!inputFile.isFile()) {
            System.out.println(srcFile + "is invalid for compression!\n");
            return;
        }
        // attempt to open the file with FileReader then use BufferedReader
        FileReader inputStream;
        try {
            inputStream = new FileReader(inputFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        BufferedReader reader = new BufferedReader(inputStream);

        // array to count char frequencies in file, charNumbers index is the char code
        int[] charNumbers = new int[256];
        int nextByte;

        // try block increments frequency for charNumbers index equal to char code
        try {
            while ((nextByte = reader.read()) != -1) {
                charNumbers[nextByte]++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // priority queue to make binary trie
        PriorityQueueHEAP<CharNode> pq = new PriorityQueueHEAP<CharNode>(new CharNodeComparator());
        CharNode currentChar;
        // array to hold CharNodes that represent data for each character in the input file.
        CharNode[] charArray = new CharNode[256];
        // loading the priority queue
        for (int i = 0; i < 256; i++) {
            if (charNumbers[i] != 0) {
                currentChar = new CharNode((char) i, charNumbers[i]);
                charArray[i] = currentChar;
                pq.add(currentChar);
            }
        }
        // add EOF char
        pq.add(new CharNode((char) -1, 1));

        // building the binary trie
        CharNode left;
        CharNode right;
        CharNode parent;
        while (pq.size() > 1) {
            left = pq.deleteMin();
            right = pq.deleteMin();
            parent = new CharNode(left, right, (left.getFreq() + right.getFreq()));
            left.setParent(parent);
            right.setParent(parent);
            pq.add(parent);
        }

        // use file and PrintWriter to open file to print compressed file to.
        File outputFile = new File(dstFile);
        PrintWriter outFile;
        try {
            outFile = new PrintWriter(outputFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        // determine each characters encoding & set the value in its corresponding CharNode
        String encoding;
        for (int i = 0; i < 256; i++) {
            encoding = "";
            if (charArray[i] != null) {
                currentChar = charArray[i];
                outFile.print(currentChar.getChar());
                outFile.print(currentChar.getFreq());
                while (currentChar.getParent() != null) {
                    if (currentChar.getParent().getLeft().getChar() == currentChar.getChar())
                        encoding = "0" + encoding;
                    else
                        encoding = "1" + encoding;
                    currentChar = currentChar.getParent();
                }
                charArray[i].setEncoding(encoding);
            }
        }
        // print end of header characters
        outFile.print((byte) 0);
        outFile.print(0);

        // attempt to re-open the file with FileReader then use BufferedReader
        try {
            inputStream = new FileReader(inputFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        reader = new BufferedReader(inputStream);

        // start reading file again to encode it from beginning
        String bitString = "";
        int addZeros;
        try {
            while ((nextByte = reader.read()) != -1) {
                bitString += charArray[nextByte].getEncoding();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add remaining bits to make file of complete bytes
        addZeros = 8 - bitString.length() % 8;
        while (addZeros > 0) {
            bitString = bitString + "0";
            addZeros--;
        }

        int bitIndex = 0;
        byte toPrint;
        // traverse each character in bitString and print bytes to file
        while (bitIndex < bitString.length()) {
            toPrint = 0;
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(bitIndex) == '1') {
                    toPrint = (byte) (toPrint >>> 1);
                    toPrint += (byte) 128;
                } else
                    toPrint = (byte) (toPrint >>> 1);
                bitIndex++;
            }
            outFile.print((char) toPrint);
        }

        // close file when all bytes are printed to file.
        outFile.close();

        // re initialize outputFile to check that it is valid after printed to and closed.
        outputFile = new File(dstFile);

        if (!outputFile.isFile())
            System.out.println(dstFile + " compression was unsuccessful!");
        else
            System.out.println(dstFile + " compression was successful!");
    }

    public static void decompressFile(String srcFile, String dstFile) {

        // declare & instantiate input file source
        File inputFile = new File(srcFile);
        if (!inputFile.isFile()) {
            System.out.println(srcFile + "is invalid for decompression!\n");
            return;
        }
        // attempt to open the file with FileReader then use BufferedReader
        FileReader inputStream;
        try {
            inputStream = new FileReader(inputFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        BufferedReader reader = new BufferedReader(inputStream);

        int nextByte;
        int freq;
        PriorityQueueHEAP<CharNode> pq = new PriorityQueueHEAP<CharNode>(new CharNodeComparator());
        // try block increments frequency for charNumbers index equal to char code
        try {
            while (true) {
                nextByte = reader.read();

                //TODO read frequency
                freq = 0;
                if (nextByte == 0 && freq == 0)
                    break;
                pq.add(new CharNode((char) nextByte, freq));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // building the binary trie
        CharNode left;
        CharNode right;
        CharNode parent;
        while (pq.size() > 1) {
            left = pq.deleteMin();
            right = pq.deleteMin();
            parent = new CharNode(left, right, (left.getFreq() + right.getFreq()));
            left.setParent(parent);
            right.setParent(parent);
            pq.add(parent);
        }

        // use file and PrintWriter to open file to print compressed file to.
        File outputFile = new File(dstFile);
        PrintWriter outFile;
        try {
            outFile = new PrintWriter(outputFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        // start reading file again to encode it from beginning
        String bitString = "";
        int addZeros;
        CharNode root = pq.deleteMin();
        CharNode currentNode = root;
        try {
            while ((nextByte = reader.read()) != -1) {
                for (int i = 0; i < 8; i++) {
                    if ((nextByte & (1 << i)) == 1) {
                        if (currentNode.getLeft() != null)
                            currentNode = currentNode.getLeft();
                        else {
                            outFile.print(currentNode.getParent().getChar());
                            currentNode = root;
                        }
                    } else {
                        if (currentNode.getRight() != null)
                            currentNode = currentNode.getRight();
                        else {
                            outFile.print(currentNode.getParent().getChar());
                            currentNode = root;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add remaining bits to make file of complete bytes
        addZeros = 8 - bitString.length() % 8;
        while (addZeros > 0) {
            bitString = bitString + "0";
            addZeros--;
        }

        // close file when all bytes are printed to file.
        outFile.close();

        // re initialize outputFile to check that it is valid after printed to and closed.
        outputFile = new File(dstFile);

        if (!outputFile.isFile())
            System.out.println(dstFile + " compression was unsuccessful!");
        else
            System.out.println(dstFile + " compression was successful!");

    }

    public static void transmitFile(String srcFile, String statsFile) {
//            * Download the new DeviceManager.java and add it to the package.
//            * This driver method should pass the user source file to the transmitFile method of the DeviceManager class.
// First it should check to see if the srcFile is a valid file before passing, if the file is invalid then it should print the following message and return:
//    <"user srcFile"> is invalid for file transfer!
    }

    private static class CharNode {
        private char data;
        private int freq;
        private CharNode left;
        private CharNode right;
        private CharNode parent;
        private String encoding;

        public CharNode(char _data, int _freq) {
            data = _data;
            freq = _freq;
        }

        public CharNode(CharNode _left, CharNode _right, int _freq) {
            freq = _freq;
            left = _left;
            right = _right;
        }

        public char getChar() {
            return data;
        }

        public int getFreq() {
            return freq;
        }

        public void setParent(CharNode _parent) {
            parent = _parent;
        }

        public CharNode getParent() {
            return parent;
        }

        public void setEncoding(String str) {
            encoding = str;
        }

        public String getEncoding() {
            return encoding;
        }

        public CharNode getLeft() {
            return left;
        }

        public CharNode getRight() {
            return right;
        }
    }

    private static class CharNodeComparator implements Comparator<CharNode> {
        public int compare(CharNode o1, CharNode o2) {
            if (o1.getFreq() > o2.getFreq())
                return 1;
            else if (o1.getFreq() < o2.getFreq())
                return -1;
            else if (o1.getChar() < o2.getChar())
                return 1;
            else if (o1.getChar() > o2.getChar())
                return -1;
            else
                return 0;
        }
    }

}
