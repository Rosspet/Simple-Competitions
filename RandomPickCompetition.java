import java.util.ArrayList;
import java.util.Random;

/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * This class implements the additional logic upon a general competition
 * required to implement a RandomPickCompetition. It mainly addes functionality
 * for automatically adding entries and drawing winners according to this type
 * of competition.
 * 
 * @author Ross petridis
 */
public class RandomPickCompetition extends Competition {

    private static final int FIRST_PRIZE = 50000;
    private static final int SECOND_PRIZE = 5000;
    private static final int THIRD_PRIZE = 1000;
    private static final int[] prizes = { FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE };
    private static final int MAX_WINNING_ENTRIES = 3;

    /**
     * Standard constructor for building a competition form user input
     * 
     * @param compName    This competitions given name
     * @param compID      This competitions ID
     * @param testingMode true iff this competition is to be executed in competition
     *                    mode or not
     */
    public RandomPickCompetition(String compName, int compID, boolean testingMode) {
        super(compName, compID, "RandomPickCompetition", testingMode);
    }

    /**
     * High level logic for facilitated the adding of entries to this competition,
     * controlled by user input
     * 
     * @param data : game data to read from containing bill and member information
     */
    public DataProvider addEntries(DataProvider data) {

        String billId;
        Bill bill;
        String memberId;

        do {
            ArrayList<Entry> theseEntries = new ArrayList<Entry>(); // arrList for this batch of entrys
            billId = getBillIDFromInputForEntry(data); // retirve valid bill ID from user

            bill = data.getBillThatExists(billId); // returns a copy of the bill
            memberId = bill.getMemberId();
            
            int numEntries = bill.getNumEntries();
            if (numEntries == 0) { // check this here as thread 450 says to.
                System.out.println(String.format(
                    "This bill ($%.1f) is not eligible for an entry. The total amount is smaller than $%d",
                    bill.getTotalAmount(), Competition.COST_PER_ENTRY
                    ));
                    continue;
                }
            data.setBillToUsed(billId);
                
            System.out.println("This bill ($" + bill.getTotalAmount() + ") is eligible for " +
                    bill.getNumEntries() + " entries.");

            // generate entries
            for (int i = 0; i < numEntries; i++) {
                theseEntries.add(new Entry(getNumEntries() + theseEntries.size() + 1, billId, memberId));
            }
            System.out.println("The following entries have been automatically generated:");

            displayEntries(theseEntries); // display entries added in this batch
            addEntriesOfArralyList(theseEntries); // add this batch to total batch of entries.

        } while (userWantsMoreEntries()); 
        return data;
    }

    /**
     * Display given entries
     * 
     * @param theseEntries the entries to display
     */
    private void displayEntries(ArrayList<Entry> theseEntries) {
        for (Entry entry : theseEntries) {
            System.out.println(entry);
        }
    }

    /**
     * Logic for drawing the winners of this competition. Only give up to one prize
     * per person
     * 
     * @return true iff the competition draw was successfully executed. (False if
     *         there were no entries to begin with.)
     */
    public boolean drawWinners(DataProvider data) {

        if (getNumEntries() == 0) {
            System.out.println("The current competition has no entries yet!");
            return false;
        }
        System.out.println(this); // display details on this competition

        ArrayList<Entry> entriesForDrawing = getEntries(); // duplicate
        Random randomGenerator = null;
        if (getTestingMode()) {
            randomGenerator = new Random(this.getCompId());
        } else {
            randomGenerator = new Random();
        }

        int winningEntryCount = 0;
        // draww while still prizes to award
        while (winningEntryCount < MAX_WINNING_ENTRIES) { // draw while still prizes left!

            int winningEntryIndex = randomGenerator.nextInt(getNumEntries());

            Entry winningEntry = entriesForDrawing.get(winningEntryIndex);

            /*
             * Ensure that once an entry has been selected,
             * it will not be selected again.
             */
            String memberID = winningEntry.getMemberId();
            if (alreadyWinningMember(getWinners(), memberID)) {
                winningEntryCount += 1;
                continue; // customer can only win 1 and already getting the better entry.
            }

            int currentPrize = prizes[winningEntryCount];
            winningEntry.setPrize(currentPrize);
            increasePrizesGiven(currentPrize);

            // make a winner to help keep track of things.
            try {
                //DataProvider data = SimpleCompetitions.getData();
                Winner winner = new Winner(data.getMember(memberID),
                        winningEntry,
                        prizes[winningEntryCount],
                        winningEntry.getEntryiD());
                addWinner(winner);
                winningEntryCount += 1;
            } catch (MemberDoesNotExist e) {
                System.out.println(e.getMessage());
            }
        }

        ArrayList<Winner> winners = getWinners();
        winners.sort(null); // sort using overriden compareTo

        displayWinners(winners);
        return true;

    }

    /**
     * Display winners out System.out
     * 
     * @param winners the winners to display the statistics of.
     */
    private void displayWinners(ArrayList<Winner> winners) {
        System.out.println("Winning entries:");
        for (Winner winner : winners) {
            System.out.println(
                    "Member ID: " + winner.getMemberID() + ", Member Name: " + winner.getMemberName() +
                            ", Entry ID: " + winner.getEntryId() + ", Prize: "
                            + String.format("%-5d", winner.getPoints()) // winner.getPoints()
            );
        }
    }
}
