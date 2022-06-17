
/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */
import java.util.Scanner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for fascilitating general logic and methodologies of
 * competitions broadly. This forms the foundation of lucky numbers and random
 * pick competition which must each employ there own addEntries() and
 * drawWinners() functionalities according to their own competition rules.
 * 
 * Many methods are public as they are required to be directly accessible in the
 * subclasses, this is was not done at the expense of a data leak as the
 * functions mostly handle internal data and return a statement about the
 * internal data which is never exposed.
 * 
 * @author Ross Petridis
 */
public abstract class Competition implements Serializable {

    private String name; // competition name
    private String type;
    private int id; // competition identifier
    private int totalPrizesAwarded = 0;
    private boolean isActive;
    private boolean testingMode;
    private ArrayList<Entry> entries = new ArrayList<Entry>();
    private ArrayList<Winner> winners = new ArrayList<Winner>();

    public static final int COST_PER_ENTRY = 50; // public as used by other classes

    /**
     * Method to add entries to the given competition subtype which can vary
     * for different competitions
     * 
     * @param data Data holding information read from files
     * @return updated data. (Justification: it was decided to return the updated
     *         data by the AddEntries to avoid passing the actual data object into
     *         any instatiation of a competition. This allows the data management to
     *         be exclusively handled by the DataProvider class which improves
     *         security as there are less places for data to be changed.)
     */
    public abstract DataProvider addEntries(DataProvider data);

    /**
     * Function contatining logic for drawing winners from a particular
     * competition according to its game rules
     * 
     * @return true iff the competition draw succesfully took place. (usually false
     *         if there were no entries.)
     */
    public abstract boolean drawWinners();

    /**
     * Default constructor to set important values of the competition
     * 
     * @param compName    The competition name
     * @param compID      The competition ID
     * @param type        The competition type, a string. Eithe "R" or "L"
     * @param testingMode Boolean, true iff this competition is in testMode in which
     *                    case
     *                    comp ID will be used for generating entries.
     */
    public Competition(String compName, int compID, String type, boolean testingMode) {
        id = compID;
        name = compName;
        this.type = type;
        this.isActive = true;
        this.testingMode = testingMode;
    }

    /**
     * Functions that facilitates the prompting and parsing of userinput for
     * a bill ID to be entered into this competition. Checks if the bill is
     * valid format, has an associated member, and has not been used before.
     * 
     * @return A bill ID of valid format.
     */
    public String getBillIDFromInputForEntry() {
        Scanner scanner = SimpleCompetitions.getScanner();
        boolean validResponse = false;
        String billID = null;
        DataProvider data = SimpleCompetitions.getData();

        while (!validResponse) {
            System.out.println("Bill ID: ");
            billID = (scanner.nextLine().trim());

            if (!Bill.validBillID(billID)) {
                continue; // not valid format for a bill ID
            }

            if (!data.billExists(billID)) {
                System.out.println("This bill does not exist. Please try again.");
                continue;
            }
            if (!data.billHasMember(billID)) {
                System.out.println("This bill has no member id. Please try again.");
                continue;
            }
            if (data.billHasBeenUsed(billID)) {
                System.out.println("This bill has already been used for a competition. Please try again.");
                continue;
            }

            validResponse = true;
        }

        return billID;

    }

    /**
     * Asks and actions user request to continue adding more entries to competition
     * or not
     * 
     * @return true iff user wants to keep adding entriesd
     */
    public boolean userWantsMoreEntries() {

        boolean validResponse = false;
        String cmd = null;
        Scanner sc = SimpleCompetitions.getScanner();

        while (!validResponse) {
            System.out.println("Add more entries (Y/N)?");
            cmd = sc.nextLine();
            if (SimpleCompetitions.validYesNoResponse(cmd)) {
                validResponse = true; // valid response format. i.e. recieved Y/N
            } else {
                System.out.println("Unsupported option. Please try again!");
            }
        }

        return cmd.equalsIgnoreCase("Y");
    }

    /**
     * Checks if a member ID is a winner already. USed to prevent awarding multiple
     * prizes
     * to the same member.
     * 
     * @param winners  The current list of winners
     * @param memberID member id to check if they are already a winner
     * @return true iff member with memberID is already a winner.
     */
    public boolean alreadyWinningMember(ArrayList<Winner> winners, String memberID) {
        Iterator<Winner> winnerIter = winners.iterator();

        while (winnerIter.hasNext()) {
            if (winnerIter.next().hasID(memberID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to output report (key statistics) on this competition
     */
    public void report() {

        System.out.println("Competition ID: " + id + ", name: " + name + ", active: " + (isActive ? "yes" : "no"));
        System.out.println("Number of entries: " + entries.size());

        if (!isActive) {
            System.out.println("Number of winning entries: " + winners.size());
            System.out.println("Total awarded prizes: " + totalPrizesAwarded);
        }
    }

    /**
     * 
     * @return A copy of the entries currently in this competitions.
     */
    public ArrayList<Entry> getEntries() {

        ArrayList<Entry> entriesCopy = new ArrayList<Entry>();
        for (Entry entry : entries) {
            entriesCopy.add(new Entry(entry));
        }

        return entriesCopy;
    }

    /**
     * 
     * @return A copy of the entrys in this competition, casted to NumbersEntry
     *         objects.
     */
    public ArrayList<NumbersEntry> getNumbersEntries() {

        ArrayList<NumbersEntry> entriesCopy = new ArrayList<NumbersEntry>();
        for (Entry entry : entries) {
            entriesCopy.add(new NumbersEntry((NumbersEntry) entry));
        }

        return entriesCopy;
    }

    /**
     * Increases variable storing the amount of prizes given out in this competition
     * 
     * @param amount the amount to increase it by
     */
    public void increasePrizesGiven(int amount) {
        totalPrizesAwarded += amount;
    }

    /**
     * Recieves a list of winners and sets them.
     * 
     * @param winners The winners of this competition
     */
    public void setWinners(ArrayList<Winner> winners) {
        this.winners = winners;
    }

    /**
     * 
     * @return The winners of this competition
     */
    public ArrayList<Winner> getWinners() {
        ArrayList<Winner> winnersCopy = new ArrayList<Winner>();
        for (Winner winner : winners) {
            winnersCopy.add(new Winner(winner));
        }
        return winnersCopy;
    }

    /**
     * Recieves a set of entries and adds the to the current set
     * 
     * @param entries The entries to be added to the current set.
     */
    public void addEntriesOfArralyList(ArrayList<Entry> entries) {
        this.entries.addAll(entries);
    }

    /**
     * Recieves 1 entry and adds this to the current set
     * 
     * @param entry The entry to be added
     */
    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    /**
     * Adds a Winner to list of winners for this competition
     * 
     * @param winner the winner to be added.
     */
    public void addWinner(Winner winner) {
        winners.add(winner);
    }

    /**
     * 
     * @return true iff this competition is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Used to set competition to non-active.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * True iff this competition is in testing mode.
     * 
     * @return
     */
    public boolean getTestingMode() {
        return this.testingMode;
    }

    /**
     * 
     * @return The name of this competition
     */
    public String getName() {
        return name;
    }

    /**
     * Overide default toString for more appropriate printing
     */
    public String toString() {
        return "Competition ID: " + id + ", Competition Name: " + name + ", Type: " + type;
    }

    /**
     * Set the type of this competition "R" or "L"
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return the competition ID of this competition
     */
    public int getCompId() {
        return id;
    }

    /**
     * @return The number of entries in this competition
     */
    public int getNumEntries() {
        return entries.size();
    }

}
