/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class NumbersEntry extends Entry {

    private ArrayList<Integer> numbers;
    private boolean auto;
    private static final int NUM_ALLOWED_ENTRIES = 7;
	private static final int MAX_RANGE = 35;
	private static final int MIN_RANGE = 1;

    // null constructor
    public NumbersEntry(){
        super();
    }

    public NumbersEntry(
            int entryId,
            String billId,
            String memberId,
            //ArrayList<Integer> numbers,
            boolean auto) {
                
        // can this be same method for the auto LuckyNumbers and then the Auto class is
        // for the random nu bers comp?
        super(entryId, billId, memberId);
        if(!auto){
            this.numbers = getManualEntryNumbers();
        } // else, numbers will be set in the constructor for AutoNumbersEntry
        //this.numbers = numbers;
        this.auto = auto;
                    
    }

    public void setNumbers(ArrayList<Integer> numbers){
        this.numbers = numbers;
    }

    @Override // the default toString in Entry.
    public String toString() {
        String returnString = String.format("Entry ID: %-6d Numbers: ", getEntryiD()) + getEntriesString();
        
        return returnString;
    }

    public String getEntriesString() {
        String numbs = "";
        Iterator<Integer> iter = getNumbers().iterator();
        while (iter.hasNext()) {
            numbs += String.format("%2d ", iter.next());
        }
        
        if (auto){
            numbs += "[Auto]";
        }
        
        return numbs;


    }

    public ArrayList<Integer> getNumbers() {
        return new ArrayList<Integer>(numbers);
    }

    private ArrayList<Integer> getManualEntryNumbers() {

        boolean validResponse = false;
        String entryNumbersStr;
        String[] entryNumbersStrArr;
        int[] entryNumbers = null;
        Scanner scanner = SimpleCompetitions.getScanner();

        while (!validResponse) {
            System.out.println("Please enter 7 different numbers (from the range 1 to" +
                    " " + MAX_RANGE + ") separated by whitespace.");
            entryNumbersStr = scanner.nextLine().trim();
            if (!entryNumbersStr.matches("[0-9 ]+")) { // numbers seperate by white space
                System.out.println("Invalid input! Non numerical input detected!");
                continue;
            }
            entryNumbersStrArr = entryNumbersStr.split("\\s+"); // split by white space.
            if (entryNumbersStrArr.length < NUM_ALLOWED_ENTRIES) {
                System.out.println("Invalid Iput! Fewer than 7 number are provided. Please try again!");
                continue;
            }
            if (entryNumbersStrArr.length > NUM_ALLOWED_ENTRIES) {
                System.out.println("Invalid Iput! More than 7 numbers are provided. Please try again!");
                continue;
            }

            entryNumbers = convertStringIntArrayToIntArray(entryNumbersStrArr);
            //printArray(entryNumbers);

            if (notAllDifferent(entryNumbers)) {
                System.out.println("Invalid input! All numbers must be different!");
                continue;
            }
            if (notAllInRange(entryNumbers, MIN_RANGE, MAX_RANGE)) {
                System.out.println("Invalid input! All numbers must be in the range from 1 to 35!");
                continue;
            }
            // done all checks
            validResponse = true;
        }
        ArrayList<Integer> arrList = arrayToArrayList(entryNumbers);
        Collections.sort(arrList); // inplace sorting.
        return arrList;

    }

    public ArrayList<Integer> arrayToArrayList(int[] arr) {
        ArrayList<Integer> arrList = new ArrayList<Integer>();

        for (int i : arr) {
            arrList.add(i);
        }
        return arrList;
    }

    private boolean notAllInRange(int[] numbers, int min, int max) {
        for (int num : numbers) {
            if (num < min || num > max) {
                return true; // a number not in range, i.e. they are not all in range.
            }
        }
        return false;

    }

    private boolean notAllDifferent(int[] numbers) {
        ArrayList<Integer> currentNumbers = new ArrayList<Integer>();

        for (int num : numbers) {
            if (currentNumbers.contains(num)) {
                return true; // found a duplciate num. i.e., they are not all different.
            }
            currentNumbers.add(num);
        }
        return false;

    }

    private int[] convertStringIntArrayToIntArray(String[] strArr) {
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        return intArr;
    }

    private void printArray(int[] entryNumbers){
        System.out.println("------ARRAY------");
        for (int i : entryNumbers){
            System.out.println(i);
        }
        System.out.println("------ARRAY DONE------");
    }
}
