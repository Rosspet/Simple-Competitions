/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Entry {
    private int entryId;
    private String billId;
    private String memberId;
    
    // null constructor
    public Entry(){

    }

    public Entry(int entryId, String billId, String memberId){
        this.entryId = entryId;
        this.billId = billId;
        this.memberId = memberId;

    }

    public int getEntryiD(){
        return entryId;
    }
    public String getBillId(){
        return billId;
    }
    public String getMemberId(){
        return memberId;
    }


}
