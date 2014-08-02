package FinalProject;

import java.io.*;
import java.util.Comparator;

/**
 * @author Casey Nordgran
 * @author Tom Pridham
 *
 * Compressor class used in the TextProcessor class.
 * Contains all the necessary methods for compression of text files.
 */
public class Compressor {

    public void compress(File srcFile, File dstFile) throws IOException {

        if (srcFile == null || dstFile == null) {
            System.err.println("Not valid files for decompression");
            return;
        }

        // output stream to open file to print compressed file to.
        FileOutputStream oFile;
        oFile = new FileOutputStream(dstFile);
        DataOutputStream outFile = new DataOutputStream(oFile);

        // attempt to open the file with FileReader then use BufferedReader
        FileReader fileIn;
        fileIn = new FileReader(srcFile);
        BufferedReader reader = new BufferedReader(fileIn);

        // array to count char frequencies in file, charNumbers index is the char code
        int[] charNumbers = new int[256];
        int nextByte;

        // try block increments frequency for charNumbers index equal to char code
        while ((nextByte = reader.read()) != -1)
            charNumbers[nextByte]++;

        // close the input file
        reader.close();
        fileIn.close();

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
        charArray[0] = new CharNode('\0', 1);
        pq.add(charArray[0]);

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

        // determine each characters encoding & set the value in its corresponding CharNode
        String encoding;
        for (int i = 0; i < 256; i++) {
            if (charArray[i] != null) {
                currentChar = charArray[i];
                // this portion prints the file header
                outFile.writeByte(currentChar.getChar());
                outFile.writeInt(currentChar.getFreq());
                // determine and set char encodings
                encoding = "";
                while (currentChar.getParent() != null) {
                    if (currentChar.getParent().getLeft() == currentChar)
                        encoding = "0" + encoding;
                    else
                        encoding = "1" + encoding;
                    currentChar = currentChar.getParent();
                }
                charArray[i].setEncoding(encoding);
            }
        }

        // print end of header characters
        outFile.writeByte(0);
        outFile.writeInt(0);

        // attempt to re-open the file with FileReader then use BufferedReader
        fileIn = new FileReader(srcFile);
        reader = new BufferedReader(fileIn);

        // start reading file again to encode it from beginning
        encoding = "";
        while ((nextByte = reader.read()) != -1)
            encoding += charArray[nextByte].getEncoding();
        // add end of file character
        encoding += charArray[0].getEncoding();

        // add remaining bits to make file of complete bytes
        for (int i = 0; i < (8 - (encoding.length() % 8)); i++)
            encoding = encoding + "0";

        byte toPrint;
        // traverse each 8 characters in bitString and print byte to file
        for (int i = 0; i < encoding.length()-8; i += 8) {
            toPrint = (byte) Integer.parseInt(encoding.substring(i, i + 8), 2);
            outFile.writeByte(toPrint);
        }

        // close file when all bytes are printed to file.
        outFile.close();

        // check that outfile is still a valid file then print corresponding message
        if (!dstFile.isFile())
            System.out.println(dstFile + " compression was unsuccessful!\n");
        else
            System.out.println(dstFile + " compression was successful!\n");
    }

    /**
     * Custom data structure used to create the binary trie
     */
    @SuppressWarnings("UnusedDeclaration")
    private static class CharNode {
        private char data;
        private int freq;
        private CharNode left;
        private CharNode right;
        private CharNode parent;
        private String encoding;

        /**
         * Constructor of leaf nodes in the trie
         * @param _data - data of node, a char
         * @param _freq - freq of data, int
         */
        public CharNode(char _data, int _freq) {
            data = _data;
            freq = _freq;
        }

        /**
         * Used for creation of parent nodes in the trie
         * @param _left - left child
         * @param _right - right child
         * @param _freq - freq of data, int
         */
        public CharNode(CharNode _left, CharNode _right, int _freq) {
            freq = _freq;
            left = _left;
            right = _right;
        }

        /**
         * Returns the data of the node
         * @return - data of node, char
         */
        public char getChar() {
            return data;
        }

        /**
         * Returns the frequency of the node
         * @return - frequency of node, int
         */
        public int getFreq() {
            return freq;
        }

        /**
         * Sets the parent to the passed node
         * @param _parent - node to be set as parent of this node
         */
        public void setParent(CharNode _parent) {
            parent = _parent;
        }

        /**
         * Gets the parent of the node
         * @return - parent, CharNode
         */
        public CharNode getParent() {
            return parent;
        }

        /**
         * Sets the encoding of the node.
         * @param str - encoding, String
         */
        public void setEncoding(String str) {
            encoding = str;
        }

        /**
         * Gets the encoding of the node
         * @return - encoding, String
         */
        public String getEncoding() {
            return encoding;
        }

        /**
         * Gets the left child of the node
         * @return left child, Char Node
         */
        public CharNode getLeft() {
            return left;
        }

        /**
         * Gets the right child of the node
         * @return right child, Char Node
         */
        public CharNode getRight() {
            return right;
        }
    }

    /**
     * Used to compare CharNodes.  Uses frequency and then ascii value as a tiebreaker
     */
    private static class CharNodeComparator implements Comparator<CharNode> {
        public int compare(CharNode o1, CharNode o2) {
            //check frequency
            if (o1.getFreq() > o2.getFreq())
                return 1;
            else if (o1.getFreq() < o2.getFreq())
                return -1;
                //check ascii value
            else if (o1.getChar() < o2.getChar())
                return -1;
            else if (o1.getChar() > o2.getChar())
                return 1;
                //equal
            else
                return 0;
        }
    }
}
