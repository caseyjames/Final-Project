package FinalProject;

import java.io.*;
import java.util.Comparator;

/**
 * @author Casey Nordgran
 * @author Tom Pridham
 *
 * Decompressor class used in the TextProcessor class.
 * Contains all the necessary methods for decompression of compressed text files.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class Decompressor {

    /**
     *
     * @param _srcFile - compressed file to be decompressed
     * @param _dstFile - file to write
     * @throws IOException
     */
    public void decompress(File _srcFile, File _dstFile) throws IOException {

        // attempt to open the file with FileInputStream then use DataInputStream
        FileInputStream inputStream;
        DataInputStream inFile;
        if (_srcFile == null || _dstFile == null) {
            System.err.println("Not valid files for decompression");
            return;
        }
        inputStream = new FileInputStream(_srcFile);
        inFile = new DataInputStream(inputStream);

        // read header portion of compressed file and add to pq
        byte nextByte;
        int freq;
        int byteCount = 0; // used to know array size for non-header portion of file
        PriorityQueueHEAP<CharNode> pq = new PriorityQueueHEAP<CharNode>(new CharNodeComparator());
        while (true) {
            nextByte = inFile.readByte();
            freq = inFile.readInt();
            byteCount += 5;
            if (freq == 0)
                break;
            pq.add(new CharNode((char) nextByte, freq));
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
        // assign final node in pq as the root node
        CharNode root = pq.deleteMin();

        // use file and PrintWriter to open file to print decompressed file to.
        PrintWriter outFile = new PrintWriter(_dstFile);

        // start reading file again to encode it from beginning
        String byteStr;
        String encoding = "";
        byte[] allBytes = new byte[((int) (_srcFile.length()) - byteCount)];
        // first build string 'encoding' of all the rest of the bits in the file
        inFile.read(allBytes);
        int bit;
        for (byte allByte : allBytes) {
            byteStr = "";
            for (int j = 0; j < 8; j++) {
                bit = Math.abs((allByte >>> j) % 2);
                if (bit > 0)
                    byteStr = "1" + byteStr;
                else
                    byteStr = "0" + byteStr;
            }
            encoding += byteStr;
        }

        // use encode string to begin decoding and printing decoded characters to outFile.
        CharNode currentNode = root;
        for (int i = 0; i < encoding.length(); i++) {
            if (encoding.charAt(i) == '0')
                currentNode = currentNode.getLeft();
            else
                currentNode = currentNode.getRight();

            if (currentNode.getLeft() == null) {
                if (currentNode.getChar() != '\0') {
                    outFile.print(currentNode.getChar());
                    currentNode = root;
                } else
                    break;
            }
        }

        // close files when all bytes are printed to file.
        outFile.close();
        inFile.close();
        inputStream.close();

        // check that the written file is still valid and print corresponding message.
        if (!_dstFile.isFile())
            System.out.println(_dstFile + " decompression was unsuccessful!\n");
        else
            System.out.println(_dstFile + " decompression was successful!\n");

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
