import java.io.Serializable;

/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Class for keeping track of a general entry and associated data into a
 * competition
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
     * Standard constructor to initialise important information
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

    // copy constructor
    public Entry(Entry entry) {
        this.entryId = entry.getEntryiD();
        this.billId = entry.getBillId();
        this.memberId = entry.getMemberId();
    }

    @Override // the default toString for printing according to spec
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
