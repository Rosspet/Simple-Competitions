import java.util.ArrayList;
import java.util.Random;

/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class RandomPickCompetition extends Competition {
    private final int FIRST_PRIZE = 50000;
    private final int SECOND_PRIZE = 5000;
    private final int THIRD_PRIZE = 1000;
    private final int[] prizes = {FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE};
	DataProvider data;
    private final int MAX_WINNING_ENTRIES = 3;
    private ArrayList<Entry> entries = new ArrayList<Entry>();
    //private ArrayList<Entry> theseEntries = new ArrayList<Entry>();
	private ArrayList<Winner> winners = new ArrayList<Winner>();
    private boolean testingMode;
    private Random random = new Random();
    //private ArrayList<Bill> bills;
    //private ArrayList<Member> members;
	
    public RandomPickCompetition(String compName, int compID, DataProvider data, boolean testingMode){
        super(compName, compID, "RandomPickCompetition", data);
        this.data = data;
        this.testingMode = testingMode;
        //this.bills = bills;
        //this.members = members;    
    }

    public void addEntries(){
        boolean finishedAddingEntries = false;
		String billId;
		//int thisEntryId = 1;
		Bill bill;
		String memberId;

        while(!finishedAddingEntries){
            ArrayList<Entry> theseEntries = new ArrayList<Entry>(); // arrList for this batch of entrys
			billId = getBillIDFromInputForEntry();
			// have a bill id that is valid and in the list of bill_ids and has a member!.
			
			bill = data.getBillThatExists(billId); // returns a copy of the bill
            memberId = bill.getMemberId();
			int numEntries = bill.getNumEntries();
            System.out.println("This bill ($" + bill.getTotalAmount() + ") is elidgible for " +
            bill.getNumEntries() + " entires.");
            for (int i=0; i<numEntries; i++){
                theseEntries.add(new Entry(entries.size()+theseEntries.size()+1, billId, memberId));
            }
            System.out.println("The following entries have been automatically generated:");
            //print entrys in theseEntries (will maybe need there own toString.) which means the toStrings in Numbers/autoEntry will need to be decorated with @Overide
            
            // TO DO print these
            displayEntries(theseEntries);
            
            entries.addAll(theseEntries);

            if (!moreEntries()){ // change to a doWhile
				finishedAddingEntries=true;
			}
        }
    }

    private void displayEntries(ArrayList<Entry> theseEntries){
        for (Entry entry : theseEntries){
            System.out.println("Entry ID: "+entry.getEntryiD());
        }
    }

    public boolean drawWinners(){
        //System.out.println("current entries:");
        //displayEntries(entries);
        if (entries.size()==0){
            System.out.println("The current competition has no entries yet!");
            return false;
        }
        ArrayList<Entry> entriesForDrawing = duplicateEntries(entries);
        Random randomGenerator = null;
        if (testingMode) {
            randomGenerator = new Random(this.getCompId());
        } else {
            randomGenerator = new Random();
        }
		int numUniqueWinners=0;
        int winningEntryCount = 0;
        int numUniqueMembers = getNumUniqueMembers();
        System.out.println(numUniqueMembers);
        while (winningEntryCount < MAX_WINNING_ENTRIES && numUniqueWinners<numUniqueMembers) {
            int winningEntryIndex = randomGenerator.nextInt(entries.size());
	
            Entry winningEntry = entries.get(winningEntryIndex);
		    
            /*
             * Ensure that once an entry has been selected,
             * it will not be selected again.
             */
            String memberID = winningEntry.getMemberId();
            if (alreadyWinningMember(winners, memberID)){
                continue; // customer can only win 1 and entry cant be picked twice.
            }
            

            if (winningEntry.getPrize() == 0) {
                int currentPrize = prizes[winningEntryCount];
                winningEntry.setPrize(currentPrize);
                //winningEntryCount++; moved to after making winner
            } 
            else {
                // already picked this entry
                continue;
            }

            // make a winner to help keep track of things.
            try {
                Winner winner = new Winner(data.getMember(memberID), 
                                    winningEntry, 
                                    prizes[winningEntryCount],
                                    winningEntry.getEntryiD()
                                );
                winners.add(winner);
                winningEntryCount+=1;
                numUniqueWinners+=1;
            }
            catch (MemberDoesNotExist e) {
                System.out.println(e.getMessage());
            }
        }
		
        /*
         * Note that the above piece of code does not ensure that
         * one customer gets at most one winning entry. Add your code
         * to complete the logic.
         */
        
        winners.sort(null); // sort using overriden compareTo

        displayWinners();
        return true; 

    }

    private int getNumUniqueMembers(){
        int numUnique=0;
        ArrayList<String> memberIds = new ArrayList<String>();

        for (Entry entry : entries){
            if(!memberIds.contains(entry.getMemberId())){
                numUnique+=1;
                memberIds.add(entry.getMemberId()); // now add.
            }
        }
        return numUnique;
        
    }
        /*
        int winningEntryCount = 0;
        while (winningEntryCount < MAX_WINNING_ENTRIES && entriesForDrawing.size()>0) {
            
            int winningEntryIndex = randomGenerator.nextInt(entriesForDrawing.size());
            
            Entry winningEntry = entriesForDrawing.get(winningEntryIndex);
            //entriesForDrawing.remove(winningEntryIndex); // do for duplicate only as want to remeber all the entries for reporting.
            String memberID = winningEntry.getMemberId();

            if (alreadyWinningMember(winners, memberID) || alreadyPickedEntry(winningEntry.getEntryiD())){
                continue; // customer can only win 1 and entry cant be picked twice.
            }
            try {
                Winner winner = new Winner(data.getMember(memberID), 
                                    winningEntry, 
                                    prizes[winningEntryCount],
                                    winningEntry.getEntryiD()
                                );
                                winners.add(winner);
                                winningEntryCount+=1;
            }
            catch (MemberDoesNotExist e) {
                System.out.println(e.getMessage());
            }
            */

            //Ensure that once an entry has been selected,
            //it will not be selected again.
            /*
            if (winningEntry.getPrize() == 0) {
                int currentPrize = prizes[winningEntryCount];
                winningEntry.setPrize(currentPrize);
                winningEntryCount++;
            }
        }
        */

            // display winners
        
    
        
		
    private boolean alreadyPickedEntry(int entryId){
        for (Winner winner : winners){
            if (winner.getEntryId()==entryId){
                return true; // arleady been picked
            }
        }
        return false;
        //
    }
        /*
         * Note that the above piece of code does not ensure that
         * one customer gets at most one winning entry. Add your code
         * to complete the logic.
         */
    private void displayWinners(){
        for (Winner winner : winners){
            System.out.println(
                "Member ID: "+winner.getMemberID()+", Member Name: "+winner.getMemberName()+
                ", Entry ID: "+winner.getEntryId()+", Prize: "+winner.getPoints()
            );
        }
    }    
}
