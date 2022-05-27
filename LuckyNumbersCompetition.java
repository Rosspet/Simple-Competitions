import java.util.ArrayList;

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

        public LuckyNumbersCompetition(String compName, int compID, DataProvider data){
            super(compName, compID, "LuckyNumbersCompetition");
            this.data = data;
            //this.bills = bills;
            //this.members = members;    
        }

        public void addEntries(){ // can this be done in parent Competition class?
            
            boolean finishedAddingEntries = false;
            int billID;
            
            while(!finishedAddingEntries){
                billID = getBillIDFromInputForEntry();
                // have a bill id that is valid and in the list of bill_ids and has a member!.
                


                // checks passed. now do....
                
                // else, this bill is OK.
                //billIDs.add(billID); // just adding the integer ID for now.
                
            }
        }
        
        public void drawWinners(){

        }
}
