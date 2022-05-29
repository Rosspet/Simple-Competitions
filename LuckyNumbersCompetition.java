import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
	ArrayList<Winner> winners = new ArrayList<Winner>();
	// private ArrayList<Bill> bills;
	// private ArrayList<Member> members;
	private static final int MIN_NUM_MANUAL = 1;
	private static final int TWO_NUM_PRIZE = 50;
	private static final int THREE_NUM_PRIZE = 100;
    private static final int FOUR_NUM_PRIZE = 500;
    private static final int FIVE_NUM_PRIZE = 1000;
	private static final int SIX_NUM_PRIZE = 5000;
	private static final int SEVEN_NUM_PRIZE = 50000;
	private boolean testingMode;
	private Random random = new Random();


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

	public void drawWinners() { // use compID as seed for generating the lucky entry.
		int seed;
		if (testingMode){
			seed = getCompId();
		} 
		else {
			seed = random.nextInt();
		}

		AutoNumbersEntry luckyEntry = new AutoNumbersEntry(seed); // the entry to matching with.
		System.out.print("Lucky Numbers: ");
		System.out.println(luckyEntry.getEntriesString());
		System.out.println("Winning entries:");

		winners = getWinningEntries((NumbersEntry)luckyEntry);
		displayWinners(winners);
	}

	private ArrayList<Winner> getWinningEntries(NumbersEntry luckyEntry){
		ArrayList<Winner> winners = new ArrayList<Winner>();
		Iterator<NumbersEntry> entryIter = entries.iterator();
		NumbersEntry entry;
		Winner existingWinner;
		int points;
		while (entryIter.hasNext()){
			entry = entryIter.next();
			points = getPoints(entry, luckyEntry);

			// a customer can only win one prize each. 
			// This will the prize with the highest value. 
			// If theres multiple of these, choose the one with the smallest ID. 

			if (points!=0){
				// we have a winner.
				
				//check if already exist,
				if (alreadyWinningMember(winners, entry.getMemberId())){
					// update there entry
					existingWinner = getExistingWinner(winners, entry.getMemberId());
					existingWinner.updatePrize(entry, points, entry.getEntryiD());
				}
				else {
					// make new winner
					try{
						winners.add(new Winner(
							data.getMember(entry.getMemberId()), 
							entry, 
							points, 
							entry.getEntryiD()
							)
						);
					}
					catch (MemberDoesNotExist e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
		// SORT WINNERS BY ENTRY ID BEFORE RETURNING; (maybe already sorted.)

		return winners;
		
	}

	private Winner getExistingWinner(ArrayList<Winner> winners, String memberID){
		Iterator<Winner> winnerIter = winners.iterator();
		Winner thisWinner;
		while (winnerIter.hasNext()){
			thisWinner = winnerIter.next();
			if(thisWinner.hasID(memberID)){
				return thisWinner;
			}
		}
		return null;
	}
	


	private boolean alreadyWinningMember(ArrayList<Winner> winners, String memberID){
		Iterator<Winner> winnerIter = winners.iterator();
		while (winnerIter.hasNext()){
			if(winnerIter.next().hasID(memberID)){
				return true;
			}
		}
		return false;
	}

	
	private int getPoints(NumbersEntry entry, NumbersEntry luckyEntry){
		ArrayList<Integer> numbs = entry.getNumbers();
		ArrayList<Integer> luckyNumbs = luckyEntry.getNumbers();

		// first get the number of entries in common with each other
		int numInCommon=0;
		for (int num : numbs){
			if (luckyNumbs.contains(num)){
				numInCommon+=1;
			}
		}

		switch (numInCommon){
			case(2):
				return (TWO_NUM_PRIZE);
			case(3):
				return (THREE_NUM_PRIZE);
			case(4):
				return (FOUR_NUM_PRIZE);
			case(5):
				return (FIVE_NUM_PRIZE);
			case(6):
				return (SIX_NUM_PRIZE);
			case(7):
				return (SEVEN_NUM_PRIZE);
			default:
				return (0);
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


	private void displayWinners(ArrayList<Winner> winners){
		for (Winner winner : winners){
			System.out.print(winner);
		}
	}

	
}
