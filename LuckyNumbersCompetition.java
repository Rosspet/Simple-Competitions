import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class LuckyNumbersCompetition extends Competition {

	private ArrayList<Integer> billIDs = new ArrayList<Integer>(); // list of bill IDs in this particular comp (not all
																	// bills ever which is in data)
	private DataProvider data;
	private ArrayList<NumbersEntry> entries = new ArrayList<NumbersEntry>();
	// private ArrayList<Bill> bills;
	// private ArrayList<Member> members;
	private static final int MIN_NUM_MANUAL = 1;
	private boolean testingMode;


	public LuckyNumbersCompetition(String compName, int compID, DataProvider data, boolean testingMode) {
		super(compName, compID, "LuckyNumbersCompetition", data);
		this.data = data;
		this.testingMode = testingMode;
		 // use compID as seed for generating lucky entry and the number of entries in the currently active competition 
		// to generate automated customers' entries.
		// this.bills = bills;
		// this.members = members;
	}

	public void addEntries() { // can this be done in parent Competition class?

		boolean finishedAddingEntries = false;
		String billId;
		int entryId = 1;
		Bill bill;
		String memberId;
		Random random = new Random();
		while (!finishedAddingEntries) {
			ArrayList<NumbersEntry> theseEntries = new ArrayList<NumbersEntry>(); // arrList for this batch of entrys
			billId = getBillIDFromInputForEntry();
			// have a bill id that is valid and in the list of bill_ids and has a member!.
			
			bill = data.getBillThatExists(billId); // returns a copy of the bill
			memberId = bill.getMemberId();
			int numEntries = bill.getNumEntries();
			
			int numManualEntries = getNumManualEntries(bill);
			int numAutoEntires = numEntries - numManualEntries;
			// now do each manual entry
			for (int i = 0; i < numManualEntries; i++) {
				theseEntries.add(new NumbersEntry(entryId, billId, memberId, false)); 
				entryId+=1;
			}

			for (int i = 0; i <numAutoEntires; i++){
				if (testingMode){
					theseEntries.add(new AutoNumbersEntry(entryId, billId, memberId, entries.size()+theseEntries.size())); 
					
				}
				else {
					theseEntries.add(new AutoNumbersEntry(entryId, billId, memberId, random.nextInt())); // random seed 
				}
				entryId+=1;
				//  the number of entries in the currently active competition to generate automated customers' entries.
			}
			entries.addAll(theseEntries); // added all entries
			System.out.println("The following entries have been added:");
			displayEntries(theseEntries);

			if (!moreEntries()){
				finishedAddingEntries=true;
			}
			

		}
	}

	

	private boolean moreEntries(){
		System.out.println("Add more entries? (Y/N)?");
		Scanner sc = SimpleCompetitions.getScanner();
		String cmd = sc.nextLine();
		while(!SimpleCompetitions.validYesNoResponse(cmd)){
			System.out.println("valid responses: Y,y,N,n. Try again.");
            cmd = sc.nextLine(); // mebbe change to next.
        }
		return cmd.equalsIgnoreCase("Y");
	} 

	


	private void displayEntries(ArrayList<NumbersEntry> entries){
		for(NumbersEntry entry : entries){
			System.out.println(entry);
		}
	}

	private int getNumManualEntries(Bill bill) {
		System.out.println("This bill ($" + bill.getTotalAmount() + ") is elidgible for " +
				bill.getNumEntries() + " entires. How many manual entries did the customer fill up?:");
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
					return intResp;
				}
			}
			System.out.println(inputErrMsg);
		}
		return -1; // should never get here.
	}

	public void drawWinners() { // use compID as seed for generating the lucky entry.

	}
}
