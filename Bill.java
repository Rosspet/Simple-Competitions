/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Bill {
    private int billID;
    private String memberID; // by keeping string, can avoid errors of parsing into Int '""' and also might want t use chars in id also nothing in spec about memberID being on numerical.
    private float totalBillAmount;
    private boolean used;
    private static final int COST_PER_ENTRY = 50;
    private static final int BILL_ID_LENGTH = 6;

    public Bill(int billID, String memberID, Float totalBillAmount, boolean used){
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
        float billAmount = totalBillAmount;
        while (billAmount>COST_PER_ENTRY){
            numEntries+=1;
            billAmount-=COST_PER_ENTRY;
        } 
        return numEntries;
    }

    public String toString(){
        return ("BillID: "+billID+", memberID: "+memberID+", totalBillAmount: " + totalBillAmount +
            ", used: "+ used);
    }
    /**
    private boolean billHasValidMemberID(String resposne){
    }
    */

    public static boolean validBillID(String billID){
        if (billID.length()!=BILL_ID_LENGTH || !billID.matches("[0-9]+")){
            System.out.println("Invalid bill id! It must be a 6-digit number. Please try again.");
            return false;
        }
        return true;

    }

    public boolean hasID(int ID){
        return (billID == ID);
    }
}
