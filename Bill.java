/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Bill {
    private int billID;
    private String memberID;
    private int totalBillAmount;
    private boolean used;
    private static final int COST_PER_ENTRY = 50;

    public Bill(int billID, String memberID, int totalBillAmount, boolean used){
        this.billID=billID;
        this.memberID=memberID;
        this.totalBillAmount=totalBillAmount;
        this.used=used;
    }

    public void useBill(){
        used=true;
    }

    public boolean hasBeenUsed(){
        if (used==true){
            return true;
        }
        return false; // has not been used.
    }

    public int getNumEntries(){
        int numEntries=0;
        int billAmount = totalBillAmount;
        while (billAmount>COST_PER_ENTRY){
            numEntries+=1;
            billAmount-=COST_PER_ENTRY;
        } 
        return numEntries;
    }
}
