/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Entry {
    private int entryId;
    private String billId;
    private String memberId;
    private int prize=0;
    
    // null constructor
    public Entry(){

    }

    public Entry(int entryId, String billId, String memberId){
        this.entryId = entryId;
        this.billId = billId;
        this.memberId = memberId;

    }

    public int getPrize(){
        return prize;
    }

    public void setPrize(int prize){
        this.prize=prize;
    }
    // copy constructor
    public Entry(Entry entry){
        this.entryId = entry.getEntryiD();
        this.billId = entry.getBillId();
        this.memberId = entry.getMemberId();
    }

    @Override // the default toString
    public String toString(){
        return "Entry: ID: " + entryId;
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
