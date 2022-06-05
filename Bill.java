/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Bill class for facilitated actions and method surrounding a 'bill'
 * in the competition. Keeps track of bill uses and member associated with
 * bill.
 */
public class Bill {

    private String billId;
    private String memberId;
    private float totalBillAmount;
    private boolean used;
    private static final int COST_PER_ENTRY = 50;
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

    // copy constructor
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
        while (billAmount >= COST_PER_ENTRY) {
            numEntries += 1;
            billAmount -= COST_PER_ENTRY;
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
     * For determining whether or not a string is of valid bill ID form.
     * Must match a 6 digit number.
     * 
     * @param billId
     * @return
     */
    public static boolean validBillID(String billId) {
        if (billId.length() != BILL_ID_LENGTH || !billId.matches("[0-9]+")) {
            System.out.println("Invalid bill id! It must be a 6-digit number. Please try again.");
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
