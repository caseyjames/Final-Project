package FinalProject;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Tom on 7/21/2014.
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

            if (input.equals("1")) {
                System.out.println("Please enter a text word: ");
                input = scanner.nextLine();
                spellcheckWord(input, false);
                continue;
            }
            if (input.equals("exit")) {
                break;
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
        String returnedWord = dictionary.spellCheck(word, false);
        if (returnedWord != null) {
            if (returnedWord.equals(word))
                System.out.println("" + word + " is a known word!\n");
            else if (returnedWord.equals(""))
                System.out.println("" + word + " is an unknown word!\n");
            else
                System.out.println("" + word + " is an unknown word! "+ returnedWord+" is a known word!\n");
        }
        return;
// This driver method should pass the user word to your spell checker part of your program. First it should check to see
// if it is a known word, then it should print the following message and return: <"user word"> is a known word!
// If the word is not a known word then your program should generate all possible alternates described above in the background section.
// Then it should check to see if any of  the generated alternates exist in the word library, if so then it should choose the most
// frequent word and print the following message and return: <"user word"> is an unknown word, <"highest frequency alternate"> is a known word!
// If none of the generated alternates exist in the word library then it should print the following message and return: <"user word"> is an unknown word!
//  Please note that if the user had entered "-f" along with the word at the time of entering their word, then your program should create a new file for the word entered by the user if it is unknown while the program is running. This file should contain the list of all alternatives found by your program EXACTLY in the format seen in this sample output file that was generated for the word "devic". Please note that the file has to be a .txt file and has to have the same name as the user word, such as devic.txt, and of course this excludes the word exit, and any known words (the words that exist in the word library).
//  Keep in mind that in the output file you need to report the correct number of alternatives found by each of four techniques described in the background section above and it has to report the total number of alternatives found, which should always be (53 * N) + 25 for any word of length N entered by the user at the minimum. Please see the statements printed after each section and towards the bottom of the sample file given above.
    }

    public static void spellcheckFile(String srcFile, String dstFile) {
//            * This driver method should pass the user source and destination file to the spell checker part of your program. This method should first check to see if the srcFile is a valid file before passing it to your spell checker, if the file is invalid then it should print the following message and return:
//    <"user srcFile"> is invalid for spell correction!
//            * If the file is valid then it should pass the file to your spell checker program to check it and correct any misspelled words in a new file dstFile given by the user.
//            * If the file did not contain any misspelled words then it should print the following message and return:
//    <"user srcFile"> contains words with correct spelling!
//            * If the file contains misspelled words then your program should generate a new file with the correct spelling of the words, please note that you must preserve only the following characters and ignore all else, "." period, "," comma,  "!"exclamation, " " space and "\n" the newline. If the file contained no unknown words then it should print the following message and return:
//    <"user srcFile"> was corrected successfully!
//            * If the file did contain unknown words also, then your program should still correct the misspelled words that it can and leave the unknown words as they are in the new file, then it should print the following message and return:
//    <"user srcFile"> was corrected, but it contains unknown words!
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
