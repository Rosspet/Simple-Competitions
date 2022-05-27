/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Competition {
    private String name; //competition name
    private int id; //competition identifier
    private String type;
    private DataProvider data;
    private static final int INVALID_INT_RESPONSE = -1;
    //private ArrayList<Bill> bills;
    //private ArrayList<Member> members;

    // all comps get the same copy of members and bills form main!
    public Competition(String compName, int compID, String type){
        id = compID;
        name = compName;
        this.type = type;
        //this.data = data;
        //this.bills = bills;
        //this.members = members;
    }

    public abstract void addEntries();

    public abstract void drawWinners();

    public void report() {
    }

    public String toString(){
        return "Competition ID: " + id + ", Competition Name: " + name + ", Type: " + type ;    
    }
    public void setType(String type){
        this.type = type;
    }



    public int getBillIDFromInputForEntry(){
        System.out.println("Bill ID:");
        Scanner scanner = SimpleCompetitions.getScanner();
        boolean validResponse = false;
        String billID=null;
        int int_billID=-1;

        while (!validResponse){
            billID = (scanner.nextLine());
            if(!Bill.validBillID(billID)){ // && billHasValidMemberID(response) going to chek in higher level. just want to get the valid numerical bill.
                continue;
            }
            int_billID = Integer.parseInt(billID);
            if  (!data.billExists(int_billID)){
                System.out.println("Bill does not exist. Enter a different Bill ID");
                continue;
            }
            if (!data.billHasMember(int_billID)){
                System.out.println("This bill has no member id. Please try again.");
                continue;
            }
            if (data.billHasBeenUsed(int_billID)){
                continue;
            }
            
            validResponse = true;
            data.setBillToUsed(int_billID);
        }
        if (int_billID==INVALID_INT_RESPONSE){
            System.out.print("FAILED TO GET PROPER BILL ID");
            System.exit(1);
        }
        return int_billID;
        
    }

    
    

}
