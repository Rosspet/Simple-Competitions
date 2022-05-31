import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;
/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

public class LuckyNumbersCompetition extends Competition {

	private static final int MIN_NUM_MANUAL = 0;
	private static final int TWO_NUM_PRIZE = 50;
	private static final int THREE_NUM_PRIZE = 100;
	private static final int FOUR_NUM_PRIZE = 500;
	private static final int FIVE_NUM_PRIZE = 1000;
	private static final int SIX_NUM_PRIZE = 5000;
	private static final int SEVEN_NUM_PRIZE = 50000;
	// private boolean testingMode;
	private Random random = new Random();

	public LuckyNumbersCompetition(String compName, int compID, boolean testingMode) {
		super(compName, compID, "LuckyNumbersCompetition", testingMode);
		// this.data = data;
		// this.testingMode = testingMode;
		// use compID as seed for generating lucky entry and the number of entries in
		// the currently active competition
		// to generate automated customers' entries.
		// this.bills = bills;
		// this.members = members;
	}

	public DataProvider addEntries(DataProvider data) { // can this be done in parent Competition class?

		boolean finishedAddingEntries = false;
		String billId;
		;
		Bill bill;
		String memberId;

		while (!finishedAddingEntries) {
			ArrayList<NumbersEntry> theseEntries = new ArrayList<NumbersEntry>(); // arrList for this batch of entrys
			billId = getBillIDFromInputForEntry();
			// have a bill id that is valid and in the list of bill_ids and has a member!.
			bill = data.getBillThatExists(billId); // returns a copy of the bill
			memberId = bill.getMemberId();
			data.setBillToUsed(billId);
			int numEntries = bill.getNumEntries();

			int numManualEntries = getNumManualEntries(bill);
			int numAutoEntires = numEntries - numManualEntries;
			// now do each manual entry
			for (int i = 0; i < numManualEntries; i++) {
				theseEntries.add(new NumbersEntry(getNumEntries() + theseEntries.size() + 1, billId, memberId, false));
			}

			for (int i = 0; i < numAutoEntires; i++) {
				if (getTestingMode()) {
					theseEntries.add(new AutoNumbersEntry(getNumEntries() + theseEntries.size() + 1, billId, memberId,
							getNumEntries() + theseEntries.size()));

				} else {
					theseEntries.add(new AutoNumbersEntry(getNumEntries() + theseEntries.size() + 1, billId, memberId,
							random.nextInt())); // random seed
				}
				// the number of entries in the currently active competition to generate
				// automated customers' entries.
			}
			// addEntries((ArrayList<Entry>)theseEntries); // added all entries
			for (NumbersEntry entry : theseEntries) {
				addEntry((Entry) entry);
			}

			System.out.println("The following entries have been added:");
			displayEntries(theseEntries);

			if (!moreEntries()) { // change to a doWhile
				finishedAddingEntries = true;
			}
		}
		return data; // return the updated data
	}

	public boolean drawWinners() { // use compID as seed for generating the lucky entry.

		if (getNumEntries() == 0) {
			System.out.println("The current competition has no entries yet!");
			return false;
		}

		int seed;
		if (getTestingMode()) {
			seed = getCompId();
		} else {
			seed = random.nextInt();
		}
		System.out.println(this);
		AutoNumbersEntry luckyEntry = new AutoNumbersEntry(seed); // the entry to matching with.
		System.out.print("Lucky Numbers:");
		System.out.println(luckyEntry.getEntriesString());
		System.out.println("Winning entries:");

		setWinningEntries((NumbersEntry)luckyEntry);
		displayWinners(getWinners());
		return true;
	}

	private void setWinningEntries(NumbersEntry luckyEntry) {
		ArrayList<Winner> winners = new ArrayList<Winner>();
		// ArrayList<Entry> entries = getEntries();
		ArrayList<NumbersEntry> entries = (getNumbersEntries());
		Iterator<NumbersEntry> entryIter = entries.iterator();
		NumbersEntry entry;
		Winner existingWinner;
		int points;
		while (entryIter.hasNext()) {
			entry = entryIter.next();
			points = getPoints(entry, luckyEntry);

			// a customer can only win one prize each.
			// This will the prize with the highest value.
			// If theres multiple of these, choose the one with the smallest ID.

			if (points != 0) {
				// we have a winner.

				// check if already exist,
				if (alreadyWinningMember(winners, entry.getMemberId())) {
					// update there entry
					existingWinner = getExistingWinner(winners, entry.getMemberId());
					existingWinner.updatePrize(entry, points, entry.getEntryiD());
				} else {
					// make new winner
					try {
						DataProvider data = SimpleCompetitions.getData();
						winners.add(new Winner(
								data.getMember(entry.getMemberId()),
								(Entry)entry, // added cast here.
								points,
								entry.getEntryiD()));
					} catch (MemberDoesNotExist e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		// SORT WINNERS BY ENTRY ID BEFORE RETURNING; (maybe already sorted since
		// created in sorted orda.)
		setWinners(winners);

		// set awardPrize total now at the end as it could have changed and dont need to
		// worry about updating if doing at end.
		for (Winner winner : winners) {
			increasePrizesGiven(winner.getPoints());
		}
		return;
	}
	/*
	 * private ArrayList<NumbersEntry> castToNumbersEntries(ArrayList<Entry>
	 * entries){
	 * 
	 * ArrayList<NumbersEntry> numbersEntries = new ArrayList<NumbersEntry>();
	 * 
	 * for(Entry entry : entries){
	 * if (entry instanceof NumbersEntry){
	 * System.out.println("ye");
	 * numbersEntries.add((NumbersEntry)entry);
	 * }
	 * else {
	 * System.out.println("no");
	 * }
	 * }
	 * return numbersEntries;
	 * }
	 */

	private Winner getExistingWinner(ArrayList<Winner> winners, String memberID) {
		Iterator<Winner> winnerIter = winners.iterator();
		Winner thisWinner;
		while (winnerIter.hasNext()) {
			thisWinner = winnerIter.next();
			if (thisWinner.hasID(memberID)) {
				return thisWinner;
			}
		}
		return null;
	}

	private int getPoints(NumbersEntry entry, NumbersEntry luckyEntry) {
		ArrayList<Integer> numbs = entry.getNumbers();
		ArrayList<Integer> luckyNumbs = luckyEntry.getNumbers();

		// first get the number of entries in common with each other
		int numInCommon = 0;
		for (int num : numbs) {
			if (luckyNumbs.contains(num)) {
				numInCommon += 1;
			}
		}

		switch (numInCommon) {
			case (2):
				return (TWO_NUM_PRIZE);
			case (3):
				return (THREE_NUM_PRIZE);
			case (4):
				return (FOUR_NUM_PRIZE);
			case (5):
				return (FIVE_NUM_PRIZE);
			case (6):
				return (SIX_NUM_PRIZE);
			case (7):
				return (SEVEN_NUM_PRIZE);
			default:
				return (0);
		}
	}

	private void displayEntries(ArrayList<NumbersEntry> entries) {
		for (NumbersEntry entry : entries) {
			System.out.println(entry);
		}
	}

	private int getNumManualEntries(Bill bill) {
		System.out.println("This bill ($" + bill.getTotalAmount() + ") is eligible for " +
				bill.getNumEntries() + " entries. How many manual entries did the customer fill up?: ");
		Scanner scanner = SimpleCompetitions.getScanner();
		boolean validResponse = false;
		String inputErrMsg = "The number must be in the range from " + MIN_NUM_MANUAL + " to " +
				bill.getNumEntries() + ". Please try again.";
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

	private void displayWinners(ArrayList<Winner> winners) {
		for (Winner winner : winners) {
			System.out.println(winner);
			winner.printEntry();
		}
	}

}
