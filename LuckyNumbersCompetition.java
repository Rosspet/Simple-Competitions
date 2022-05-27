import java.util.ArrayList;
import java.util.Scanner;

/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class LuckyNumbersCompetition extends Competition {

		private ArrayList<Integer> billIDs = new ArrayList<Integer>(); // list of bill IDs in this particular comp (not all bills ever which is in data)
		private DataProvider data;
		//private ArrayList<Bill> bills;
		//private ArrayList<Member> members;
		private static final int MIN_NUM_MANUAL=1;

		public LuckyNumbersCompetition(String compName, int compID, DataProvider data){
			super(compName, compID, "LuckyNumbersCompetition");
			this.data = data;
			//this.bills = bills;
			//this.members = members;    
		}

		public void addEntries(){ // can this be done in parent Competition class?
			
			boolean finishedAddingEntries = false;
			String billID;
			
			while(!finishedAddingEntries){
				billID = getBillIDFromInputForEntry();
				// have a bill id that is valid and in the list of bill_ids and has a member!.
				
				Bill bill = data.getBillThatExists(billID); // returns a copy of the bill
				int numEntries = bill.getNumEntries();
				int numManualEntries = getManualNumEntries(bill);
				
				// now do each manual entry
				for (int i=0; i<numManualEntries; i++){
					
				}

				// checks passed. now do....
				
				// else, this bill is OK.
				//billIDs.add(billID); // just adding the integer ID for now.
				
			}
		}

		private int getManualNumEntries(Bill bill){
			System.out.println("This bill ($"+bill.getTotalAmount()+") is elidgible for "+
				bill.getNumEntries()+"entires. How many manual entries did the customer fill up?:");
			Scanner scanner = SimpleCompetitions.getScanner();
			boolean validResponse = false;
			String inputErrMsg = "Please enter a number between 1 and "+bill.getNumEntries()+".";
			int intResp=-1;
			while(!validResponse){
				String resp = scanner.nextLine().trim();
				if (resp.matches("[0-9]+")){ // digits only
					intResp = Integer.parseInt(resp); 
					if(MIN_NUM_MANUAL<=intResp && intResp<=bill.getNumEntries()){ // digits in acceptable range.
						validResponse=true; // can return anyway.
						
					}
				}
			}
			return intResp;
		
		}

		
		public void drawWinners(){

		}
}
