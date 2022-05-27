/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AutoNumbersEntry extends NumbersEntry {

    private int seed;

    public AutoNumbersEntry(int entryId, String billId, String memberId, int seed){
        super(entryId, billId, memberId, true);
        setNumbers(createNumbers(seed));
        this.seed = seed;

    }

    private final int NUMBER_COUNT = 7;
    private final int MAX_NUMBER = 35;
	
    public ArrayList<Integer> createNumbers (int seed) {
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
