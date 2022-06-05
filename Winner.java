
/**
 * Class used to facilitate storing of winners seperate to an Entry and a normal
 * Member.
 */
public class Winner extends Member implements Comparable<Winner> {

    private Entry winningEntry;
    private int points;
    private int entryID;

    /**
     * Constructor used during competition drawing for creating new found winners.
     * 
     * @param member       The winning member
     * @param winningEntry The winning entry this member entered
     * @param points       Points won
     * @param entryId      The id of the winning entry.
     */
    public Winner(Member member, Entry winningEntry, int points, int entryId) {
        super(member);
        this.winningEntry = winningEntry;
        this.points = points;
        this.entryID = entryId;
    }

    /**
     * Copy Constructor
     * 
     * @param winner The object to safely duplicate
     */
    public Winner(Winner winner) {
        super(winner.getMemberID(), winner.getMemberName(), winner.getAdress());
        this.points = winner.points;
        this.entryID = winner.entryID;
        this.winningEntry = winner.winningEntry;
    }

    /**
     * Overiding to default compareTo method to sort an Arraylist of Winners by
     * their entryId's
     */
    @Override
    public int compareTo(Winner otherWinner) {
        return otherWinner.entryID < this.entryID ? 1 : otherWinner.entryID > this.entryID ? -1 : 0;
    }

    /**
     * Set the winning entry of this winner
     * 
     * @param winningEntry
     */
    public void setWinningEntry(NumbersEntry winningEntry) {
        this.winningEntry = winningEntry;
    }

    /**
     * Update the prize associated with this winner. This is called if a winner has
     * won another prize, and will give this winner the better prize, or the one
     * with smaller id if they are the same.
     * 
     * @param winningEntry The new winning entry to check if its better and should
     *                     be updated
     * @param points       The points of the new entry
     * @param entryId      The entryId of the new prize
     */
    public void updatePrize(NumbersEntry winningEntry, int points, int entryId) {
        if (points > this.points || (points == this.points && entryId < this.entryID)) {
            // if more poitns or same points but earlier entryId, then update
            this.winningEntry = winningEntry;
            this.points = points;
            this.entryID = entryId;
        }
    }

    /**
     * Overiding of default toString for displaying winners.
     */
    @Override
    public String toString() {
        return "Member ID: " + getMemberID() + ", Member Name: " + getMemberName() +
                ", Prize: " + String.format("%-5d", points);
    }

    /**
     * Print a winner's winning entry
     */
    public void printEntry() {
        System.out.println(
                "--> Entry ID: " + entryID + ", Numbers:" + ((NumbersEntry) winningEntry).getEntriesString());
    }

    /**
     * Get winner's winning entry's Id.
     * 
     * @return the winning entry's id
     */
    public int getEntryId() {
        return entryID;
    }

    /**
     * 
     * @return Points won by this winner.
     */
    public int getPoints() {
        return points;
    }

}
