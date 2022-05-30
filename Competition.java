/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public abstract class Competition {
    private String name; //competition name
    private int id; //competition identifier
    private String type;
    private DataProvider data;
    private static final String INVALID_INT_RESPONSE = "-1";
    //private ArrayList<Bill> bills;
    //private ArrayList<Member> members;

    // all comps get the same copy of members and bills form main!
    public Competition(String compName, int compID, String type, DataProvider data){
        id = compID;
        name = compName;
        this.type = type;
        this.data = data;
        //this.bills = bills;
        //this.members = members;
    }

    public abstract void addEntries();

    public abstract boolean drawWinners();

    public void report() {
    }

    public String toString(){
        return "Competition ID: " + id + ", Competition Name: " + name + ", Type: " + type ;    
    }
    public void setType(String type){
        this.type = type;
    }

    public int getCompId(){
        return id;
    }

    public String getBillIDFromInputForEntry(){
        Scanner scanner = SimpleCompetitions.getScanner();
        boolean validResponse = false;
        String billID=INVALID_INT_RESPONSE;
        //int int_billID=-1;

        while (!validResponse){
            System.out.println("Bill ID:");
            billID = (scanner.nextLine().trim());
            //System.out.println(billID);
            if(!Bill.validBillID(billID)){ // && billHasValidMemberID(response) going to chek in higher level. just want to get the valid numerical bill.
                continue;
            }
            
            if  (!data.billExists(billID)){
                System.out.println("This bill does not exist. Please try again.");
                continue;
            }
            if (!data.billHasMember(billID)){
                System.out.println("This bill has no member id. Please try again.");
                continue;
            }
            if (data.billHasBeenUsed(billID)){
                System.out.println("This bill has already been used for a competition. Please try again.");
                continue;
            }
            validResponse = true;
            data.setBillToUsed(billID);
        }
        if (billID==INVALID_INT_RESPONSE){
            System.out.print("FAILED TO GET PROPER BILL ID");
            System.exit(1);
        }
        return billID;
        
    }

    public boolean moreEntries(){
		System.out.println("Add more entries? (Y/N)?");
		Scanner sc = SimpleCompetitions.getScanner();
		String cmd = sc.nextLine();
		while(!SimpleCompetitions.validYesNoResponse(cmd)){
			System.out.println("valid responses: Y,y,N,n. Try again.");
            cmd = sc.nextLine(); // mebbe change to next.
        }
		return cmd.equalsIgnoreCase("Y");
	} 

    public boolean alreadyWinningMember(ArrayList<Winner> winners, String memberID){
		Iterator<Winner> winnerIter = winners.iterator();
		while (winnerIter.hasNext()){
			if(winnerIter.next().hasID(memberID)){
				return true;
			}
		}
		return false;
	}

    public ArrayList<Entry> duplicateEntries(ArrayList<Entry> entries){
        
        ArrayList<Entry> copyEntries =  new ArrayList<Entry>();

        for (Entry entry : entries){
            copyEntries.add(new Entry(entry));
        }
        return copyEntries;
    }
    

}
