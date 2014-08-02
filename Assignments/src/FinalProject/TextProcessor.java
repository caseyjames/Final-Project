package FinalProject;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * This Project uses all of our own data structures for the extra credit.
 *
 * @author Casey Nordgran
 * @author Tom Pridham
 *         <p/>
 *         <p/>
 *         Text Processor class that has a spellchecker that can check individual words or entire files.
 *         Returning a file with all the incorrectly spelled words replaced with their correct spellings.
 *         It also has a file compressor and decompressor that will compress or decompress a passed file
 *         and write it to the specified output file.
 *         It can also simulate trasnmitting a file over a network.
 *         All interaction is done via commandline.
 */
public class TextProcessor {
    private static Dictionary dictionary;
    private static Compressor compressor;
    private static Decompressor decompressor;
    private static DeviceManager dev;
    private static String newLine = System.lineSeparator();

    /**
     * Main method of Text Processor.  Initializes all the components necessary
     * for the methods to function and then asks the user for input.
     *
     * @param args0 - dictionary file with word frequencies
     */
    public static void main(String[] args0) {
        // first one for testing only
//        initializeComponents("wordstats1.txt");

        initializeComponents(args0[0]);
        if (dictionary == null)
            return;

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input, input2;

        //main input loop
        while (true) {
            System.out.println("Please choose from the following options:");
            System.out.println("1) Word spell check");
            System.out.println("2) File spell check");
            System.out.println("3) File compression");
            System.out.println("4) File decompression");
            System.out.println("5) File remote transfer" + newLine);
            input = scanner.next();

            //spellcheckWord
            if (input.equals("1")) {
                System.out.println("Please enter a text word: ");
                input = scanner.next();
                input2 = scanner.nextLine();
                //checks whether verbose or not
                if (input2.equals(" -f"))
                    spellcheckWord(input, true);
                else
                    spellcheckWord(input, false);

                //spellcheckFile
            } else if (input.equals("2")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.next();
                spellcheckFile(input, input2);

                //compressFile
            } else if (input.equals("3")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.next();
                compressFile(input, input2);

                //decompressFile
            } else if (input.equals("4")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.next();
                decompressFile(input, input2);

                //transmitFile
            } else if (input.equals("5")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.next();
                transmitFile(input, "");

                //check exit or validity of entry
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

    /**
     * Sets up the necessary components for the rest of the program to run
     *
     * @param statsFile - dictionary file with word frequencies
     */
    public static void initializeComponents(String statsFile) {
        //check validity of file
        File inputFile = new File(statsFile);
        if (!inputFile.isFile()) {
            System.out.println("Invalid word stats file argument!" + newLine);
            return;
        }

        //create dictionary and compressors
        dictionary = new Dictionary(inputFile, 2000);
        compressor = new Compressor();
        decompressor = new Decompressor();
        dev = new DeviceManager();

    }

    /**
     * This method calls on the spellcheck method in dictionary. If the word is in the
     * dictionary, the same word is return. If not the new alternate word found in the
     * dictionary is returned. If there is not an alternate in the dictionary found than
     * an empty string is returned and the appropriate message is displayed.
     *
     * @param word
     * @param fileWrite
     */
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

    /**
     * Checks a passed file for misspelled words and returns a new file with all the
     * misspelled words replaced with their correct spelling.
     * @param srcFile - file to check, <infile>.ext
     * @param dstFile - file to write, <outfile>.ext
     */
    public static void spellcheckFile(String srcFile, String dstFile) {

        //check file for validity
        File inputFile = new File(srcFile);
        if (!inputFile.isFile()) {
            System.out.println(srcFile + "is invalid for spell correction!" + newLine);
            return;
        }

        Scanner inputFileLine;
        PrintWriter outputFile;
        try {
            inputFileLine = new Scanner(inputFile);
            outputFile = new PrintWriter(dstFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        int message = 0;

        //while there are still lines in the file to be read
        while (inputFileLine.hasNextLine()) {
            //get next line and split into tokens
            String[] tokens = inputFileLine.nextLine().split("\\b");
            String alternateWord;
            //for every token in the current line
            for (String token : tokens) {
                if (token.length() == 0)
                    continue;
                //if the token is a word
                if (Character.isAlphabetic((token.codePointAt(0))) || Character.isDigit(token.charAt(0))) {
                    //check spelling
                    alternateWord = dictionary.spellCheck(token, false);
                    //if no alternates, print original token
                    if (alternateWord.equals("")) {
                        message = 2;
                        outputFile.print(token);
                        //else print corrected word
                    } else {
                        if (message != 2)
                            message = 1;
                        outputFile.print(alternateWord);
                    }
                } else {
                    outputFile.print(token);
                }
            }
            outputFile.println();
        }
        outputFile.close();

        //print message declaring success
        if (message == 0) {
            System.out.println(srcFile + " contains words with correct spelling!" + newLine);
        } else if (message == 1)
            System.out.println(srcFile + " was corrected successfully!" + newLine);
        else
            System.out.println(srcFile + " was corrected, but it contains unknown words!" + newLine);
    }

    /**
     * Compresses a text file using the Huffman algorithm.
     * @param srcFile - file to check, <infile>.ext
     * @param dstFile - file to write, <outfile>.ext
     */
    public static void compressFile(String srcFile, String dstFile) {
        File inFile = new File(srcFile);
        File outFile = new File(dstFile);
        // test that srcFile is valid
        if (!inFile.isFile()) {
            System.out.println(srcFile + " is invalid for compression!");
            return;
        }
        // create new compressor class with inFile & outFile, then invoke compress(), throws IO exception
        try {
            compressor.compress(inFile, outFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Decompresses a text file using the Huffman algorithm.
     * @param srcFile - file to check, <infile>.ext
     * @param dstFile - file to write, <outfile>.ext
     */
    public static void decompressFile(String srcFile, String dstFile) {
        File inFile = new File(srcFile);
        File outFile = new File(dstFile);
        // test that srcFile is valid
        if (!inFile.isFile()) {
            System.out.println(srcFile + " is invalid for decompression!");
            return;
        }
        // create new compressor class with inFile & outFile, then invoke compress(), throws IO exception
        try {
            decompressor.decompress(inFile, outFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param srcFile - file to transfer, <infile>.ext
     * @param statsFile - unused
     */
    @SuppressWarnings("UnusedParameters")
    public static void transmitFile(String srcFile, String statsFile) {

        dev.initializeNetwork();
        dev.transmitFile(srcFile);
        dev.receiveFile(srcFile);
    }
}
