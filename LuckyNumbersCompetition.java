import java.util.ArrayList;

/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class LuckyNumbersCompetition extends Competition {

        private ArrayList<Integer> billIDs = new ArrayList<Integer>(); // list of bill IDs in this particular comp (not all bills ever which is in data)
        private DataProvider data;

        public LuckyNumbersCompetition(String compName, int compID, DataProvider data){
            super(compName, compID, "LuckyNumbersCompetition");
            this.data = data;
            
        }

        public void addEntries(){ // can this be done in parent Competition class?
            
            boolean finishedAddingEntries = false;
            int billID;
            
            while(!finishedAddingEntries){
                billID = getBillIDFromInput();
                if  (!data.billExists(billID)){
                    System.out.println("Bill does not exist. Enter a different Bill ID");
                    continue;
                }
                if (!data.billHasMember(billID)){
                    System.out.println("This bill has no member id. Please try again.");
                    continue;
                }
                // checks passed. now do....

                // else, this bill is OK.
                //billIDs.add(billID); // just adding the integer ID for now.
                
            }
        }
        
        public void drawWinners(){

        }
}
