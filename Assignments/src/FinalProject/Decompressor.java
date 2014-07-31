package FinalProject;

import java.io.*;
import java.util.Comparator;

/**
 * Created by Casey on 7/28/2014.
 */
public class Decompressor {
    private File srcFile;
    private File dstFile;

    public void decompress(File _srcFile, File _dstFile) throws IOException{
        srcFile = _srcFile;
        dstFile = _dstFile;


        // attempt to open the file with FileInputStream then use DataInputStream
        FileInputStream inputStream;
        DataInputStream inFile;
        inputStream = new FileInputStream(srcFile);
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
        PrintWriter outFile = new PrintWriter(dstFile);

        // start reading file again to encode it from beginning
        String byteStr;
        String encoding = "";
        byte[] allBytes = new byte[((int) (srcFile.length()) - byteCount)];
        // first build string 'encoding' of all the rest of the bits in the file
        inFile.read(allBytes);
        int bit;
        for (int i = 0; i < allBytes.length; i++) {
            byteStr = "";
            for (int j = 0; j < 8; j++) {
                bit = Math.abs((allBytes[i] >>> j) % 2);
                if (bit > 0)
                    byteStr = "1" + byteStr;
                else
                    byteStr = "0" + byteStr;
            }
            encoding += byteStr;
        }

        // use encode string to begin decoding and printing decoded characters to outFile.
        CharNode currentNode = new CharNode(root);
        for (int i = 0; i < encoding.length(); i++) {
            if (encoding.charAt(i) == '0')
                currentNode = currentNode.getLeft();
            else
                currentNode = currentNode.getRight();

            if (currentNode.getLeft() == null) {
                if (currentNode.getChar() != '\0') {
                    outFile.print(currentNode.getChar());
                    currentNode = new CharNode(root);
                } else
                    break;
            }
        }

        // close files when all bytes are printed to file.
        outFile.close();
        inFile.close();
        inputStream.close();

        // check that the written file is still valid and print corresponding message.
        if (!dstFile.isFile())
            System.out.println(dstFile + " decompression was unsuccessful!\n");
        else
            System.out.println(dstFile + " decompression was successful!\n");

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

        public CharNode(CharNode node) {
            data = node.getChar();
            freq = node.getFreq();
            left = node.getLeft();
            right = node.getRight();
            parent = node.getParent();
            encoding = node.getEncoding();
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
                return -1;
            else if (o1.getChar() > o2.getChar())
                return 1;
            else
                return 0;
        }
    }
}
