import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class LuckyNumbersCompetition extends Competition {

	private ArrayList<Integer> billIDs = new ArrayList<Integer>(); // list of bill IDs in this particular comp (not all
																	// bills ever which is in data)
	private DataProvider data;
	private ArrayList<NumbersEntry> entries;
	// private ArrayList<Bill> bills;
	// private ArrayList<Member> members;
	private static final int MIN_NUM_MANUAL = 1;
	private static final int NUM_ALLOWED_ENTRIES = 7;
	private static final int MAX_RANGE = 35;
	private static final int MIN_RANGE = 1;

	public LuckyNumbersCompetition(String compName, int compID, DataProvider data) {
		super(compName, compID, "LuckyNumbersCompetition");
		this.data = data;
		// this.bills = bills;
		// this.members = members;
	}

	public void addEntries() { // can this be done in parent Competition class?

		boolean finishedAddingEntries = false;
		String billId;
		int entryID = 1;
		Bill bill;
		String memberId;
		while (!finishedAddingEntries) {

			billId = getBillIDFromInputForEntry();
			// have a bill id that is valid and in the list of bill_ids and has a member!.
			
			bill = data.getBillThatExists(billId); // returns a copy of the bill
			memberId = bill.getMemberId();
			int numEntries = bill.getNumEntries();
			int numManualEntries = getManualNumEntries(bill);
			int numAutoEntires = numEntries - numManualEntries;
			// now do each manual entry
			for (int i = 0; i < numManualEntries; i++) {
				entries.add(new NumbersEntry(entryID, billId, memberId, getManualEntryNumbers(), false)); 
			}

			for (int i = 0; i <numAutoEntires; i++){
				//entries.add(new AutoNumbersEntry());
			}
			// now do automatic entries.

		}
	}

	private ArrayList<Integer> getManualEntryNumbers(){
			
			boolean validResponse = false;
			String entryNumbersStr;
			String[] entryNumbersStrArr;
			int[] entryNumbers=null;
			Scanner scanner = SimpleCompetitions.getScanner();

			while(!validResponse){
				System.out.println("Please enter 7 different numbers (from the range 1 to" +
				" "+ MAX_RANGE+ ") separated by whitespace.");
				entryNumbersStr = scanner.nextLine().trim();
				if (!entryNumbersStr.matches("[0-9 ]+")){ //numbers seperate by white space
					System.out.println("Invalid input! Non numerical input detected!");
					continue;
				}
				entryNumbersStrArr = entryNumbersStr.split("\\s+"); // split by white space.
				if (entryNumbersStrArr.length<NUM_ALLOWED_ENTRIES){
					System.out.println("Invalid Iput! Fewer than 7 number are provided. Please try again!");
					continue;
				}
				if (entryNumbersStrArr.length>NUM_ALLOWED_ENTRIES){
					System.out.println("Invalid Iput! More than 7 numbers are provided. Please try again!");
					continue;
				} 
				
				entryNumbers = convertStringIntArrayToIntArray(entryNumbersStrArr);
				
				if (notAllDifferent(entryNumbers)){
					System.out.println("Invalid input! All numbers must be different!");
					continue;
				}
				if (notAllInRange(entryNumbers, MIN_RANGE, MAX_RANGE)){
					System.out.println("Invalid input! All numbers must be in the range from 1 to 35!");
					continue;
				}
				// done all checks
				validResponse=true;
			}
			return arrayToArrayList(entryNumbers);

		}

		private ArrayList<Integer> arrayToArrayList(int[] arr){
			ArrayList<Integer> arrList = new ArrayList<Integer>();
			
			for(int i : arr){
				arrList.add(i);
			}
			return arrList;
		}

	private boolean notAllInRange(int[] numbers, int min, int max){
		for (int num : numbers){
			if(num<min || num > max){
				return false; // a number not in range
			}
		}
		return true;

	}

	private boolean notAllDifferent(int[] numbers){
		ArrayList<Integer> currentNumbers = new ArrayList<Integer>();

		for (int num : numbers){
			if (currentNumbers.contains(num)){
				return false;
			}
			currentNumbers.add(num);
		}
		return true;

	}

	private int[] convertStringIntArrayToIntArray(String[] strArr){
		int[] intArr = new int[strArr.length];
		for (int i=0; i<strArr.length; i++){
			intArr[i] = Integer.parseInt(strArr[i]);
		}
		return intArr;
	}

	private int getManualNumEntries(Bill bill) {
		System.out.println("This bill ($" + bill.getTotalAmount() + ") is elidgible for " +
				bill.getNumEntries() + "entires. How many manual entries did the customer fill up?:");
		Scanner scanner = SimpleCompetitions.getScanner();
		boolean validResponse = false;
		String inputErrMsg = "Please enter a number between 1 and " + bill.getNumEntries() + ".";
		int intResp = -1;
		while (!validResponse) {
			String resp = scanner.nextLine().trim();
			if (resp.matches("[0-9]+")) { // digits only
				intResp = Integer.parseInt(resp);
				if (MIN_NUM_MANUAL <= intResp && intResp <= bill.getNumEntries()) { // digits in acceptable range.
					validResponse = true; // can return anyway.
				}
			}
		}
		return intResp;

	}

	public void drawWinners() {

	}
}
