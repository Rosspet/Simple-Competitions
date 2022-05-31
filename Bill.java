/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Bill {
    private String billId;
    private String memberId; // by keeping string, can avoid errors of parsing into Int '""' and also might want t use chars in id also nothing in spec about memberID being on numerical.
    private float totalBillAmount;
    private boolean used;
    private static final int COST_PER_ENTRY = 50;
    private static final int BILL_ID_LENGTH = 6;

    public Bill(String billID, String memberId, float totalBillAmount, boolean used){
        this.billId=billID;
        this.memberId=memberId;
        this.totalBillAmount=totalBillAmount;
        this.used=used;
    }

    // copy constructor
    public Bill(Bill bill){
        this.billId = bill.billId;
        this.memberId = bill.memberId;
        this.totalBillAmount = bill.totalBillAmount;
        this.used = bill.used;
    }

    public String getMemberId(){
        return memberId;
    }

    public void useBill(){
        used=true;
    }

    public float getTotalAmount(){
        return totalBillAmount;
    }

    public boolean hasBeenUsed(){
        return used;
    }

    public int getNumEntries(){
        int numEntries=0;
        float billAmount = totalBillAmount;
        while (billAmount>=COST_PER_ENTRY){
            numEntries+=1;
            billAmount-=COST_PER_ENTRY;
        } 
        return numEntries;
    }

    public String toString(){
        return ("BillID: "+billId+", memberID: "+memberId+", totalBillAmount: " + totalBillAmount +
            ", used: "+ used);
    }
    /**
    private boolean billHasValidMemberID(String resposne){
    }
    */

    public static boolean validBillID(String billId){
        if (billId.length()!=BILL_ID_LENGTH || !billId.matches("[0-9]+")){
            System.out.println("Invalid bill id! It must be a 6-digit number. Please try again.");
            return false;
        }
        return true;

    }

    public String getId(){
        return billId;
    }

    public boolean hasMember(){
        return (!memberId.equals(""));
    }

    public boolean hasId(String Id){
        return (billId.equals(Id));
    }
}
