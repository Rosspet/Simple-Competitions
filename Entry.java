import java.io.Serializable;

/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Class for keeping track of a general entry and associated data into a
 * competition. Forms the basis of an entry and is used to create more
 * specialised entries for other specialised types of comeptitions
 * 
 * @author Ross Petridis
 */
public class Entry implements Serializable {
    private int entryId;
    private String billId;
    private String memberId;
    private int prize = 0;

    // null constructor
    public Entry() {
    }

    /**
     * Standard constructor. Mostly used to retrieve safe copies of entry where only
     * general information is needed.
     * Typically instatiate entry's by using the subclasses.
     * 
     * @param entryId  This entry's ID
     * @param billId   The bill used to make this entry
     * @param memberId member ID of person who this entry belongs to
     */
    public Entry(int entryId, String billId, String memberId) {
        this.entryId = entryId;
        this.billId = billId;
        this.memberId = memberId;

    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    /**
     * Return safe copy of this entry
     * 
     * @param entry Copy of the inputted entry.
     */
    public Entry(Entry entry) {
        this.entryId = entry.getEntryiD();
        this.billId = entry.getBillId();
        this.memberId = entry.getMemberId();
    }

    /**
     * More useful toString() method to match printing needed as per specificaiton.
     */
    @Override
    public String toString() {
        return "Entry ID: " + String.format("%-6d", entryId);
    }

    public int getEntryiD() {
        return entryId;
    }

    public String getBillId() {
        return billId;
    }

    public String getMemberId() {
        return memberId;
    }

}
