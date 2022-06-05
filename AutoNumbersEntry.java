
/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */
/**
 * This class fascilitates automatic generation of customer entries to
 * LuckyNumbers games, as well as the lucky entry for choosing winners.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Custom class for the automatic generation of entry to the lucky numbers
 * competition.
 */
public class AutoNumbersEntry extends NumbersEntry {

    private final int NUMBER_COUNT = 7;
    private final int MAX_NUMBER = 35;

    /**
     * Constructor used for Lucky Entry.
     * 
     * @param seed The seed used to generated this "random" entry.
     */
    public AutoNumbersEntry(int seed) {
        super();
        setNumbers(createNumbers(seed));
        setAuto(true);
    }

    /**
     * Standard constructor for making automated customer entries.
     * 
     * @param entryId  The customers entry id
     * @param billId   The customers bill id
     * @param memberId The customers members.
     * @param seed     Seed to generate the numbers with
     */
    public AutoNumbersEntry(int entryId, String billId, String memberId, int seed) {
        super(entryId, billId, memberId, true);
        setNumbers(createNumbers(seed));

    }

    /**
     * Function that generates the numbers for this entry
     * 
     * @param seed seed for the auto number genrator
     * @return the automatically generated numbers.
     */
    public ArrayList<Integer> createNumbers(int seed) {
        // new list to add generated numbers to.
        ArrayList<Integer> validList = new ArrayList<Integer>();
        int[] tempNumbers = new int[NUMBER_COUNT];
        for (int i = 1; i <= MAX_NUMBER; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random(seed));
        for (int i = 0; i < NUMBER_COUNT; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        return arrayToArrayList(tempNumbers);
    }
}
