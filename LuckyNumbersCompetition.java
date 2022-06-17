import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;

/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * This class implements the logic required for instatiating and running
 * LuckyNumbersCompetion's.
 * It is an extension of the base competition and allows for manual and auto
 * generation of entrys to the competition.
 * 
 * @author Ross Petridis
 */
public class LuckyNumbersCompetition extends Competition {

	private Random random = new Random();

	// Constants
	private static final int MIN_NUM_MANUAL_ALLOWED = 0;
	private static final int TWO_NUM_PRIZE = 50;
	private static final int THREE_NUM_PRIZE = 100;
	private static final int FOUR_NUM_PRIZE = 500;
	private static final int FIVE_NUM_PRIZE = 1000;
	private static final int SIX_NUM_PRIZE = 5000;
	private static final int SEVEN_NUM_PRIZE = 50000;

	private static final int TWO_IN_COMMON = 2;
	private static final int THREE_IN_COMMON = 3;
	private static final int FOUR_IN_COMMON = 4;
	private static final int FIVE_IN_COMMON = 5;
	private static final int SIX_IN_COMMON = 6;
	private static final int SEVEN_IN_COMMON = 7;

	/**
	 * Standard constructor from user input
	 * 
	 * @param compName    competition's name
	 * @param compID      competiton ID
	 * @param testingMode true iff competition should be run in testing mode i.e,
	 *                    (deterministic randomness)
	 */
	public LuckyNumbersCompetition(String compName, int compID, boolean testingMode) {
		super(compName, compID, "LuckyNumbersCompetition", testingMode);
	}

	/**
	 * Facilitates the adding of entries into this competition by prompting users
	 * for input bill and checking the validity of the bills.
	 * 
	 * @param data The data storing information on bills and members which is used
	 *             to add entries from
	 */
	public DataProvider addEntries(DataProvider data) { // can this be done in parent Competition class?

		String billId;
		Bill bill;
		String memberId;

		do {
			// get bill and member data
			ArrayList<NumbersEntry> theseEntries = new ArrayList<NumbersEntry>(); // arrList for this batch of entrys
			billId = getBillIDFromInputForEntry(data); // get valid bill ID
			bill = data.getBillThatExists(billId); // returns a copy of the bill
			memberId = bill.getMemberId();

			// check number of elidgible entries and ensure atleast 1.
			int numEntries = bill.getNumEntries();
			if (numEntries == 0) { // check this here as thread 450 says to.
				System.out.println(String.format(
						"This bill ($%.1f) is not eligible for an entry. The total amount is smaller than $%d",
						bill.getTotalAmount(), Competition.COST_PER_ENTRY));
				continue;
			}

			data.setBillToUsed(billId);
			int numManualEntries = getNumManualEntries(bill);
			int numAutoEntires = numEntries - numManualEntries;

			// Fill manual entries
			for (int i = 0; i < numManualEntries; i++) {
				theseEntries.add(new NumbersEntry(getNumEntries() + theseEntries.size() + 1, billId, memberId, false));
			}

			// Fill auto entries accoridng to testing mode.
			for (int i = 0; i < numAutoEntires; i++) {
				if (getTestingMode()) {
					theseEntries.add(new AutoNumbersEntry(getNumEntries() + theseEntries.size() + 1, billId, memberId,
							getNumEntries() + theseEntries.size()));
				} else {
					theseEntries.add(new AutoNumbersEntry(getNumEntries() + theseEntries.size() + 1, billId, memberId,
							random.nextInt())); // random seed
				}
			}

			// add new entries
			for (NumbersEntry entry : theseEntries) {
				addEntry((Entry) entry);
			}

			// display new entries
			System.out.println("The following entries have been added:");
			displayEntries(theseEntries);

		} while (userWantsMoreEntries()); // leep going while user wants to add more!
		return data; // return the updated data.
	}

	/**
	 * Facilitates the high level drawing of winners accoridng to the specificaiton
	 * and whether or not this competition is done in testing or normal mode.
	 * 
	 * @return true iff winners are drawn. False otherwise (if no entries)
	 */
	public boolean drawWinners(DataProvider data) { // use compID as seed for generating the lucky entry.

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

		System.out.println(this); // print this comp data
		AutoNumbersEntry luckyEntry = new AutoNumbersEntry(seed); // generate lucky entry to match with.
		System.out.print("Lucky Numbers:");
		System.out.println(luckyEntry.getEntriesString());

		System.out.println("Winning entries:");
		setWinningEntries((NumbersEntry) luckyEntry, data);
		displayWinners(getWinners());

		return true; // winners drawn successfully
	}

	/**
	 * Given the lucky entry, will find and set the winners of this competition
	 * 
	 * @param luckyEntry The entry to match players entry with.
	 */
	private void setWinningEntries(NumbersEntry luckyEntry, DataProvider data) {

		ArrayList<Winner> winners = new ArrayList<Winner>();
		ArrayList<NumbersEntry> entries = (getNumbersEntries());
		Iterator<NumbersEntry> entryIter = entries.iterator();

		NumbersEntry entry;
		Winner existingWinner;
		int points;

		while (entryIter.hasNext()) { // for each entry in this competition
			entry = entryIter.next();
			points = getPoints(entry, luckyEntry); // find points awarded for this entry

			if (points != 0) {
				// we have a winner - check if already exist,
				if (alreadyWinningMember(winners, entry.getMemberId())) {
					// update there entry incase they win more now.
					existingWinner = getExistingWinner(winners, entry.getMemberId());
					existingWinner.updatePrize(entry, points, entry.getEntryiD());
				} else {
					// make new winner
					try {
						//DataProvider data = SimpleCompetitions.getData();
						winners.add(new Winner(
								data.getMember(entry.getMemberId()),
								(Entry) entry, // added cast here.
								points,
								entry.getEntryiD()));
					} catch (MemberDoesNotExist e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		winners.sort(null); // sort using overriden compareTo (already in sorted unless overwriting?)
		setWinners(winners);

		for (Winner winner : winners) {
			// count number of points given at the end as some winners
			// may have had there prizes updated.
			increasePrizesGiven(winner.getPoints());
		}
		return;
	}

	/**
	 * Given a list of winners and a particular winner, return the associated winner
	 * object
	 * 
	 * @param winners  The list of winners to search in
	 * @param memberID The member ID of the winner to find
	 * @return
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

	/**
	 * Calculate and return the number of points this entry deserves
	 * 
	 * @param entry      the entry to find points for
	 * @param luckyEntry the lucky entry to compare this entry with
	 * @return int - the number of points for this entry
	 */
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
			case (TWO_IN_COMMON):
				return (TWO_NUM_PRIZE);
			case (THREE_IN_COMMON):
				return (THREE_NUM_PRIZE);
			case (FOUR_IN_COMMON):
				return (FOUR_NUM_PRIZE);
			case (FIVE_IN_COMMON):
				return (FIVE_NUM_PRIZE);
			case (SIX_IN_COMMON):
				return (SIX_NUM_PRIZE);
			case (SEVEN_IN_COMMON):
				return (SEVEN_NUM_PRIZE);
			default:
				return (0);
		}
	}

	/**
	 * Displays all entries in this competitions
	 * 
	 * @param entries
	 */
	private void displayEntries(ArrayList<NumbersEntry> entries) {
		for (NumbersEntry entry : entries) {
			System.out.println(entry);
		}
	}

	/**
	 * Retrieves the number of entries the user wishes to enter manually
	 * 
	 * @param bill The user's bill
	 * @return the number of entrys desired to be entered manually
	 */
	private int getNumManualEntries(Bill bill) {

		Scanner scanner = SimpleCompetitions.getScanner();
		boolean validResponse = false;
		String inputErrMsg = "The number must be in the range from " + MIN_NUM_MANUAL_ALLOWED + " to " +
				bill.getNumEntries() + ". Please try again.";
		int intResp = -1;

		System.out.println("This bill ($" + bill.getTotalAmount() + ") is eligible for " +
				bill.getNumEntries() + " entries. How many manual entries did the customer fill up?: ");

		while (!validResponse) {
			String resp = scanner.nextLine().trim();
			if (resp.matches(SimpleCompetitions.DIGITS_ONLY_REGEX)) { // digits only!
				intResp = Integer.parseInt(resp);
				if (MIN_NUM_MANUAL_ALLOWED <= intResp && intResp <= bill.getNumEntries()) { // digits in acceptable
																							// range.
					validResponse = true; // can return anyway.
					return intResp;
				}
			}
			System.out.println(inputErrMsg);
		}
		return -1; // should never get here.
	}

	/**
	 * Displalys winners of this competition to system.out
	 * 
	 * @param winners
	 */
	private void displayWinners(ArrayList<Winner> winners) {
		for (Winner winner : winners) {
			System.out.println(winner);
			winner.printEntry();
		}
	}
}
