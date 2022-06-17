/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Bill class for facilitated actions and method surrounding a 'bill'
 * in the competition. Keeps track of bill uses and member associated with
 * bill.
 * In addition to being their own entity holding important data about themselves
 * and their ability to affect entries in competions and thus competitions,
 * bills also acts as a link to members via the memberID and game data.
 * 
 * @author Ross Petridis
 */
public class Bill {

    private String billId;
    private String memberId;
    private float totalBillAmount;
    private boolean used;
    private static final int BILL_ID_LENGTH = 6;

    /**
     * Standard constructor used for reading bill entry from file
     * 
     * @param billID          This bills ID
     * @param memberId        The member ID used/owner of bill.
     * @param totalBillAmount money spent on this bill
     * @param used            true iff this bill has been used in a competition
     */
    public Bill(String billID, String memberId, float totalBillAmount, boolean used) {
        this.billId = billID;
        this.memberId = memberId;
        this.totalBillAmount = totalBillAmount;
        this.used = used;
    }

    /**
     * Safe copy constuctor
     * 
     * @param bill The bill to safely duplicate
     */
    public Bill(Bill bill) {
        this.billId = bill.billId;
        this.memberId = bill.memberId;
        this.totalBillAmount = bill.totalBillAmount;
        this.used = bill.used;
    }

    /**
     * Calculates the number of competition entrys this bill is
     * eligible for given the total cost and price per entry.
     * 
     * @return the number of entries this bill is eligible for
     */
    public int getNumEntries() {
        int numEntries = 0;
        float billAmount = totalBillAmount;
        while (billAmount >= Competition.COST_PER_ENTRY) {
            numEntries += 1;
            billAmount -= Competition.COST_PER_ENTRY;
        }
        return numEntries;
    }

    /**
     * For printing bill data to output more appropriately.
     */
    @Override
    public String toString() {
        return ("BillID: " + billId + ", memberID: " + memberId + ", totalBillAmount: " + totalBillAmount +
                ", used: " + used);
    }

    /**
     * This method determines whether or not a string is of valid bill ID form.
     * The string must match a 6 digit number format.
     * 
     * This method is static as it is called in higher level logic of the
     * SimpleCompetitions program when a bill is in the process of being
     * instatiated. Ultimately, using public static enables simpler class structure and
     * information flow only up and down the class hierarchy and not across the
     * class hiearchy, this is explained further below.
     * 
     * There are many checks for when a bill is being entered which are dependant on
     * other objects, such as the data class. I did not want the data class and bill
     * class to be directly interacting, but rather, interacting through a
     * hiearchical structure through a competition which has both a bill and data
     * object.
     * Hence, this method had to be public to be availabe, and static as it is not
     * for a particular bill and infact is used before a bill object has been
     * instatiated.
     * 
     * @param billId
     * @return
     */
    public static boolean validBillID(String billId) {
        if (billId.length() != BILL_ID_LENGTH || !billId.matches(SimpleCompetitions.DIGITS_ONLY_REGEX)) {
            System.out.println("Invalid bill id! It must be a "+BILL_ID_LENGTH+"-digit number. Please try again.");
            return false;
        }
        return true;
    }

    /**
     * 
     * @return iff this bill has a member associated with it and can
     *         therefor be entered into a competition
     */
    public boolean hasMember() {
        return (!memberId.equals(""));
    }

    /**
     * 
     * @param Id the id to compare with this.id
     * @return true iff this bill has id equal to inputted id.
     */
    public boolean hasId(String Id) {
        return (billId.equals(Id));
    }

    /**
     * 
     * @return iff bill has been used in a competition.
     */
    public boolean hasBeenUsed() {
        return used;
    }

    public String getMemberId() {
        return memberId;
    }

    public void useBill() {
        used = true;
    }

    public float getTotalAmount() {
        return totalBillAmount;
    }

    public String getId() {
        return billId;
    }

}
