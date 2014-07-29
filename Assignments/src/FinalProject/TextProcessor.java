package FinalProject;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private static Compressor compressor;
    private static Decompressor decompressor;

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
        compressor = new Compressor();
        decompressor = new Decompressor();
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
        File inFile = new File(srcFile);
        File outFile = new File(dstFile);
        // test that srcFile is valid
        if (!inFile.isFile()) {
            System.out.println(srcFile + " is invalid for compression!");
            return;
        }
        // create new compressor class with inFile & outFile, then invoke compress()
        compressor.compress(inFile, outFile);
    }

    public static void decompressFile(String srcFile, String dstFile) {
        File inFile = new File(srcFile);
        File outFile = new File(dstFile);
        // test that srcFile is valid
        if (!inFile.isFile()) {
            System.out.println(srcFile + " is invalid for decompression!");
            return;
        }
        // create new compressor class with inFile & outFile, then invoke compress()
        decompressor.decompress(inFile, outFile);
    }

    public static void transmitFile(String srcFile, String statsFile) {

    }
}
