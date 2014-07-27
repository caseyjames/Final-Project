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


    public static void main(String[] args0) {
//        initializeComponents(args0[0]);
        initializeComponents("wordstats1.txt");
        if (dictionary == null)
            return;

        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Please choose from the following options:");
            System.out.println("1) Word spell check");
            System.out.println("2) File spell check");
            System.out.println("3) File compression");
            System.out.println("4) File decompression");
            System.out.println("5) File remote transfer\n");
            String input = scanner.nextLine();
            String input2 = null;

            if (input.equals("1")) {
                System.out.println("Please enter a text word: ");
                input = scanner.next();
                if( scanner.hasNext()){
                    input2 = scanner.next("-f");
                }
                if (input2 != null && input2.equals("-f"))
                    spellcheckWord(input, true);
                else
                    spellcheckWord(input, false);

            } else if (input.equals("2")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.nextLine();
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.nextLine();
                spellcheckFile(input, input2);
            } else if (input.equals("3")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.nextLine();
                File inputFile = new File(input);
                if (!inputFile.isFile()) {
                    System.out.println(input + "is invalid for spell correction!\n");
                    continue;
                }
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.nextLine();
                compressFile(input, input2);

            } else if (input.equals("4")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.nextLine();
                File inputFile = new File(input);
                if (!inputFile.isFile()) {
                    System.out.println(input + "is invalid for spell correction!\n");
                    continue;
                }
                System.out.println("Please enter the destination file path: ");
                input2 = scanner.nextLine();
                decompressFile(input, input2);

            } else if (input.equals("5")) {
                System.out.println("Please enter the source file path: ");
                input = scanner.nextLine();
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
        Scanner inputFileLine = null;
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
        }
        else if (message == 1)
            System.out.println(srcFile + " was corrected successfully!\n\n");
        else
            System.out.println(srcFile + " was corrected, but it contains unknown words!\n\n");
    }

    public static void compressFile(String srcFile, String dstFile) {
//            * This driver method should pass the user source and destination file to the file compression part of your program. The method should first check to see if the srcFile is a valid file before passing it to your compressor, if the file is invalid then it should print the following message and return:
//    <"user srcFile"> is invalid for compression!
//            * If the file is valid then it should pass the file to your compression program to be compressed in a new file dstFile given by the user.
//            * After the call for compression returns, this method should check to see if the now compressed dstFile is a valid file, if it is then you should print:
//    <"user srcFile"> was compressed successfully!
//            * If the dstFile is actually invalid, meaning that it was not generated by your program or similar problems then you should print:
//    <"user srcFile"> compression was unsuccessful!
    }

    public static void decompressFile(String srcFile, String dstFile) {
//            * This driver method should pass the user source and destination file to the file compression part of your program.
// The method should first check to see if the srcFile is a valid file before passing it to your compressor, if the file is invalid then it should print the following message and return:
//    <"user srcFile"> is invalid for compression!
//            * If the file is valid then it should pass the file to your compression program to be compressed in a new file dstFile given by the user.
//            * After the call for compression, this method should check to see if the now compressed dstFile is a valid file, if it is then you should print:
//    <"user srcFile"> was compressed successfully!
//            * If the dstFile is actually invalid, meaning that it was not generated by your program or similar problems then you should print:
//    <"user srcFile"> compression was unsuccessful!
    }

    public static void transmitFile(String srcFile, String statsFile) {
//            * Download the new DeviceManager.java and add it to the package.
//            * This driver method should pass the user source file to the transmitFile method of the DeviceManager class.
// First it should check to see if the srcFile is a valid file before passing, if the file is invalid then it should print the following message and return:
//    <"user srcFile"> is invalid for file transfer!
    }
}
