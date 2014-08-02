package FinalProject;


/**
 * @author Casey Nordgran
 * @author Tom Pridham
 *
 * Timer class for TextProcessor used for analysis
 */
@SuppressWarnings("ALL")
public class TextProcessorTime {

    public static void main(String[] args) {
        problem7Timer();
    }

    /**
     * This timer runs experiments for problem 5.
     * Namely that of checking a word for accuracy of spelling with various tweaks
     */
    public static void problem5Timer() {
        // Timing experiment variables
        long startTime, midpointTime, stopTime;
        long averageTime;
        int timesToLoop = 10000;
        TextProcessor text = new TextProcessor();
//        text.initializeComponents("wordstats1.txt");

        String[] wordlist1 = {"a", "b", "v", "g", "g", "k", "e", "s"};
        String[] wordlist2 = {"ab", "an", "in", "on", "it", "et", "as", "mo"};
        String[] wordlist3 = {"cat", "dog", "bow", "tum", "hug", "pog", "jog", "poo"};
        String[] wordlist4 = {"junk", "what", "ever", "care", "pear", "butt", "peel", "done"};
        String[] wordlist5 = {"brown", "human", "camps", "start", "skunk", "lords", "sword", "funky"};
        String[] wordlist6 = {"dagger", "fights", "tigers", "homies", "bronies", "wizard", "lizard", "tricks"};
        String[] wordlist7 = {"fighter", "jealous", "zealots", "werecat", "dragons", "computes", "science", "million"};
        String[] wordlist8 = {"fighters", "charlie", "werewolf", "elephant", "bicycles", "spaceman", "traceman", "whatever"};
        String[] wordlist9 = {"octopuses", "pegasuses", "railroads", "whatevers", "junctions", "numerical", "scorpions", "overlords"};
        MyLinkedList<String[]> wordlist = new MyLinkedList<String[]>();
        wordlist.addFirst(wordlist9);
        wordlist.addFirst(wordlist8);
        wordlist.addFirst(wordlist7);
        wordlist.addFirst(wordlist6);
        wordlist.addFirst(wordlist5);
        wordlist.addFirst(wordlist4);
        wordlist.addFirst(wordlist3);
        wordlist.addFirst(wordlist2);
        wordlist.addFirst(wordlist1);
        int k = 1;


        // First, spin computing stuff until one second has gone by.
        // This allows this thread to stabilize.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] s;
        //used to iterate through list of String[] with varying length
        for (int l = 0; l < 9; l++) {

            s = wordlist.get(l);
            startTime = System.nanoTime();
            text.initializeComponents("wordstats1.txt");

            //do work
            for (int i = 0; i < timesToLoop; i++) {
                for (int j = 0; j < 8; j++) {
                    text.spellcheckWord(s[j], false);
                }

            }

            midpointTime = System.nanoTime();

            // Run a loop to capture the cost of running the loop.
            for (int i = 0; i < timesToLoop; i++) {
                for (int j = 0; j < 8; j++) {
                }
            }

            // Compute the time, subtract the cost of running the loop
            // from the cost of running the loop and computing square roots.
            // Average it over the number of runs.
            stopTime = System.nanoTime();
            averageTime = (((midpointTime - startTime) - (stopTime - midpointTime)) / (timesToLoop * 8));

            // Cases 1b, 2b, 3b
            System.out.println(k++ + "\t" + averageTime);
        }
    }


    /**
     * Used to do the timing required for problem 7 in the analysis.
     * Namely, that of the compression, decompression and checkFile.
     */
    public static void problem7Timer() {
        // Timing experiment variables
        long startTime, midpointTime, stopTime;
        long averageTime;
        int timesToLoop = 1000;
        TextProcessor text = new TextProcessor();
        text.initializeComponents("wordstats1.txt");



        // First, spin computing stuff until one second has gone by.
        // This allows this thread to stabilize.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //used to iterate through files
        for (int l = 0; l < 41; l+=5) {

            startTime = System.nanoTime();

            //do work
            for (int i = 0; i < timesToLoop; i++) {
                //text.spellcheckFile("goodluck"+l+".txt", "whatever" + l+".txt");
                //text.compressFile("goodluck"+l+".txt", "whatever" + l+".txt");
                text.decompressFile("whatever" + l+".txt", "junk"+l+".txt");
            }

            midpointTime = System.nanoTime();

            // Run a loop to capture the cost of running the loop.
            for (int i = 0; i < timesToLoop; i++) {
            }

            // Compute the time, subtract the cost of running the loop
            // from the cost of running the loop and computing square roots.
            // Average it over the number of runs.
            stopTime = System.nanoTime();
            averageTime = (((midpointTime - startTime) - (stopTime - midpointTime)) / (timesToLoop));

            //print time
            System.out.println(l + "\t" + averageTime);
        }
    }
}