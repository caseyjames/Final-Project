package FinalProject;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * @author Casey Nordgran
 * @author Tom Pridham
 */
public class TextProcessorTester extends TestCase{
    public void testCompressor() {
        Compressor comp = new Compressor();
        File test = new File("wordstats1.txt");
        File out = new File("out.txt");
        try {
            comp.compress(test, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert (out.exists());
        assert (test.length() > out.length());

        File junk = null;
        File outJunk = null;
        try {
            comp.compress(junk, outJunk);
        } catch (IOException e) {
            e.printStackTrace();

            fail();
        }
    }

    public void testDecompressor() {
        Decompressor comp = new Decompressor();
        File test = new File("out.txt");
        File out = new File("check.txt");
        try {
            comp.decompress(test, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert (out.exists());
        assert (test.length() < out.length());

        File junk = null;
        File outJunk = null;
        try {
            comp.decompress(junk, outJunk);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
