/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/**
 * An entry to the LuckyNumbers competition.
 * This class is an extension to a standard Entry
 * by including a list of numbers and a boolean
 * to keep track of whether this entry was automatically
 * generated or not.
 */
public class NumbersEntry extends Entry {

    private ArrayList<Integer> numbers; // Entered number for entry
    private boolean auto; // true iff auto generatd the entry.

    private static final int NUM_ALLOWED_ENTRIES = 7;
	private static final int MAX_RANGE = 35;
	private static final int MIN_RANGE = 1;

    /**
     * Null constructor
     */
    public NumbersEntry(){
        super();
    }

    /**
     * Sets the value of auto
     * @param bool true iff this entry was auto generated.
     */
    public void setAuto(boolean bool){
        this.auto = bool;
    }

    /**
     * Copy constructor
     * @param entry An object of NumberEntry to make a copy of.
     */
    public NumbersEntry(NumbersEntry entry){
        super((Entry)entry);
        this.auto = entry.auto;
        this.numbers = entry.getNumbers();
    }

    /**
     * Constructor for making a new NumberEntry from 
     * @param entryId Entry ID for this new entry
     * @param billId Bill associated with this entry
     * @param memberId Member associated with this entry
     * @param auto Whether or not to auto generate the numbers for this entry.
     */
    public NumbersEntry(
            int entryId,
            String billId,
            String memberId,
            boolean auto) {
                
        super(entryId, billId, memberId);

        if(!auto){
            this.numbers = getManualEntryNumbers();
        } // else, numbers are generated and set in subclass for AutoNumbersEntry.
        this.auto = auto;
    }

    /**
     * Set this objects arraylist of numbers. Useful if using an 
     * external tool to calculate them.
     * @param numbers The numbers to set this.numbers to.
     */
    public void setNumbers(ArrayList<Integer> numbers){
        this.numbers = numbers;
    }

    /**
     * Overriding of default toString method to print according to
     * design spec.
     * @return The string mostly sued for printing.
     */
    @Override // the default toString in Entry.
    public String toString() {
        String returnString = String.format("Entry ID: %-6d Numbers:", getEntryiD()) + getEntriesString();

        return returnString;
    }

    /**
     * Return the numbers used to make this entry in print format.
     * @return The numbers
     */
    public String getEntriesString() {
        String numbs = "";
        Iterator<Integer> iter = getNumbers().iterator();
        
        while (iter.hasNext()) {
            numbs += String.format(" %2d", iter.next());
        }
        if (auto){
            numbs += " [Auto]";
        }
        return numbs;
    }

    /**
     * 
     * @return a copy of the numbers of this entry.
     */
    public ArrayList<Integer> getNumbers() {
        return new ArrayList<Integer>(numbers);
    }

    /**
     * contains logic for setting numbers from user input.
     * checkcs the formatting of input to be correct.
     * @return The inputted numbers, sorted.
     */
    private ArrayList<Integer> getManualEntryNumbers() {

        boolean validResponse = false;
        String entryNumbersStr;
        String[] entryNumbersStrArr;
        int[] entryNumbers = null;
        Scanner scanner = SimpleCompetitions.getScanner();

        while (!validResponse) {

            // prompt and read from input.
            System.out.println("Please enter 7 different numbers (from the range 1 to" +
                    " " + MAX_RANGE + ") separated by whitespace.");
            entryNumbersStr = scanner.nextLine().trim();

            // Need numbers seperated by white space only
            if (!entryNumbersStr.matches("[0-9 ]+")) {
                System.out.println("Invalid input! Numbers are expected. Please try again!");
                continue;
            }

            // we have a string of numbers seperated by white space. 
            //convert to arr to check size.
            entryNumbersStrArr = entryNumbersStr.split("\\s+"); // split by white space.

            if (entryNumbersStrArr.length < NUM_ALLOWED_ENTRIES) { // not enough numbers
                System.out.println("Invalid input! Fewer than 7 numbers are provided. Please try again!");
                continue;
            }
            if (entryNumbersStrArr.length > NUM_ALLOWED_ENTRIES) { // too many.
                System.out.println("Invalid input! More than 7 numbers are provided. Please try again!");
                continue;
            }

            //convert to int array to check values.
            entryNumbers = convertStringIntArrayToIntArray(entryNumbersStrArr);
            
            if (notAllDifferent(entryNumbers)) { // duplicates
                System.out.println("Invalid input! All numbers must be different!");
                continue;
            }

            if (notAllInRange(entryNumbers, MIN_RANGE, MAX_RANGE)) { //out of range.
                System.out.println("Invalid input! All numbers must be in the range from 1 to 35!");
                continue;
            }
            // done all checks - OK
            validResponse = true;
        }
        ArrayList<Integer> arrList = arrayToArrayList(entryNumbers);
        Collections.sort(arrList); // inplace sorting.
        return arrList;

    }

    /**
     * convert array of int to arrayList of int.
     * @param arr input array to be converted
     * @return the converted array.
     */
    public ArrayList<Integer> arrayToArrayList(int[] arr) {
        ArrayList<Integer> arrList = new ArrayList<Integer>();

        for (int i : arr) {
            arrList.add(i);
        }
        return arrList;
    }

     /**
      * checks if an inputted array of numbers are all within a specified range.
      * @param numbers Numbers to checks the values of.
      * @param min min value allowed.
      * @param max max value allowed.
      * @return true iff all in range.
      */
    private boolean notAllInRange(int[] numbers, int min, int max) {
        for (int num : numbers) {
            if (num < min || num > max) {
                return true; // a number not in range, i.e. they are not all in range.
            }
        }
        return false;
    }

    /**
     * returns true if array contains unique set of numbers.
     * @param numbers
     * @return
     */
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

    /**
     * converts array of strings representing digits to actual integers.
     * @param strArr array to convert
     * @return int array.
     */
    private int[] convertStringIntArrayToIntArray(String[] strArr) {
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        return intArr;
    }
}
