package FinalProject;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * @author Casey Nordgran
 * @author Tom Pridham
 */
public class TextProcessorTester extends TestCase{
    public void testDictionary() {
        // test that adding wordstats1.txt adds 2002 words to dictionary
        File dictFile = new File("wordstats1.txt");
        Dictionary testDictionary = new Dictionary(dictFile, 2002);
        assertEquals(2002, testDictionary.size);
        // also assert that table length is next prime up from 2002
        assertEquals(2003, testDictionary.capacity);

        // test that some known words in the dictionary return the same word
        assertEquals("the", testDictionary.spellCheck("the", false));
        assertEquals("because", testDictionary.spellCheck("because", false));
        assertEquals("government", testDictionary.spellCheck("government", false));
        assertEquals("program", testDictionary.spellCheck("program", false));

        // test unknown words return known words
        assertEquals("the", testDictionary.spellCheck("teh", false));
        assertEquals("because", testDictionary.spellCheck("bcause", false));
        assertEquals("government", testDictionary.spellCheck("gavernment", false));
        assertEquals("program", testDictionary.spellCheck("progrem", false));

        // test that passing unknown word not in dictionary returns empty string
        assertEquals("", testDictionary.spellCheck("falaksjdf", false));
        assertEquals("", testDictionary.spellCheck("utoiamiem", false));
        assertEquals("", testDictionary.spellCheck("kamvmiean", false));
        assertEquals("", testDictionary.spellCheck("vnimoqiwerf", false));

        // test that passing unknown word with print file actually creates file
        assertEquals("the", testDictionary.spellCheck("teh", true));
        File testFile = new File("teh.txt");
        assertTrue(testFile.isFile());
    }

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
