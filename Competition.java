/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */
import java.util.Scanner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for fascilitating general logic and methodsologies of 
 * competitions broadly. This forms the foundation of lucky numbers and random pick competition.
 */
public abstract class Competition implements Serializable {
    private String name; //competition name
    private int id; //competition identifier
    private String type;
    private boolean isActive;
    private static final String INVALID_INT_RESPONSE = "-1";
    private int totalPrizesAwarded=0;
    private ArrayList<Entry> entries = new ArrayList<Entry>();
	private ArrayList<Winner> winners = new ArrayList<Winner>();
    private boolean testingMode;

    /**
     * Method to add entries to the given competition subtype which can vary
     * for different competitions
     * @param data Data holding information read from files
     * @return updated data.
     */
    public abstract DataProvider addEntries(DataProvider data);

    /**
     * Function contatining logic for drawing winners from a particular
     * competition according to its game rules
     * @return true iff the competition draw succesfully took place. (usually false
     *          if there were no entries.)
     */
    public abstract boolean drawWinners();

    /**
     * Default constructor to set important values of the competition 
     * @param compName The competition name
     * @param compID The competition ID
     * @param type The competition type, a string. Eithe "R" or "L"
     * @param testingMode Boolean, true iff this competition is in testMode in which case
     *                      comp ID will be used for generating entries.
     */
    public Competition(String compName, int compID, String type, boolean testingMode){
        id = compID;
        name = compName;
        this.type = type;
        this.isActive=true;
        this.testingMode = testingMode;
    }
    
    /**
     * 
     * @return true iff this competition is active
     */
    public boolean isActive(){
        return isActive;
    }
    
    /**
     * Used to set competition to non-active.
     */
    public void deactivate(){
        this.isActive=false;
    }

    /**
     * True iff this competition is in testing mode.
     * @return
     */
    public boolean getTestingMode(){
        return this.testingMode;
    }

    /**
     * Method to output report (key statistics) on this competition
     */
    public void report() {
        System.out.println("Competition ID: "+id+", name: "+name+", active: "+ (isActive ? "yes" : "no"));
        System.out.println("Number of entries: "+ entries.size());
        if (!isActive){
            System.out.println("Number of winning entries: "+ winners.size());
            System.out.println("Total awarded prizes: "+totalPrizesAwarded);
        }
    }

    /**
     * 
     * @return The name of this competition
     */
    public String getName(){
        return name;
    }

    /**
     * 
     * @return A copy of the entries currently in this competitions.
     */
    public ArrayList<Entry> getEntries(){
        ArrayList<Entry> entriesCopy = new ArrayList<Entry>();
        for (Entry entry : entries){
            entriesCopy.add(new Entry(entry));
        }
        return entriesCopy;
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

    public int getNumEntries(){
        return entries.size();
    }

    public void increasePrizesGiven(int amount){
        totalPrizesAwarded+=amount;
    }

    public void setWinners(ArrayList<Winner> winners){
        this.winners = winners;
    }

    public ArrayList<Winner> getWinners(){
        ArrayList<Winner> winnersCopy = new ArrayList<Winner>();
        for (Winner winner : winners){
            winnersCopy.add(new Winner(winner));
        }
        return winnersCopy;
    }

    public void addEntriesOfArralyList(ArrayList<Entry> entries){
        this.entries.addAll(entries);
    }

    public void addEntry(Entry entry){
        entries.add(entry);
    }

    public String getBillIDFromInputForEntry(){
        Scanner scanner = SimpleCompetitions.getScanner();
        boolean validResponse = false;
        String billID=INVALID_INT_RESPONSE;
        //int int_billID=-1;
        DataProvider data = SimpleCompetitions.getData() ;

        while (!validResponse){
            System.out.println("Bill ID: ");
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
            //data.setBillToUsed(billID);
        }
        if (billID==INVALID_INT_RESPONSE){
            System.out.print("FAILED TO GET PROPER BILL ID");
            System.exit(1);
        }

        //SimpleCompetitions.updateData(data);

        return billID;
        
    }

    public boolean moreEntries(){
        boolean validResponse=false;
        String cmd=null;
        Scanner sc = SimpleCompetitions.getScanner();
        while (!validResponse){
            System.out.println("Add more entries (Y/N)?");
            cmd = sc.nextLine();
            if (SimpleCompetitions.validYesNoResponse(cmd)){
                validResponse=true;
            } else {
                System.out.println("Unsupported option. Please try again!");
            }
        }
        return cmd.equalsIgnoreCase("Y");
        /*
		System.out.println("Add more entries (Y/N)?");
		Scanner sc = SimpleCompetitions.getScanner();
		String cmd = sc.nextLine();
		while(!SimpleCompetitions.validYesNoResponse(cmd)){
			System.out.println("Unsupported option. Please try again!");
            System.out.println("Add more entries (Y/N)?");
            cmd = sc.nextLine(); // mebbe change to next.
        }
		return cmd.equalsIgnoreCase("Y");
        */
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

    public void addWinner(Winner winner){
        winners.add(winner);
    }

    public ArrayList<Entry> duplicateEntries(ArrayList<Entry> entries){
        
        ArrayList<Entry> copyEntries =  new ArrayList<Entry>();

        for (Entry entry : entries){
            copyEntries.add(new Entry(entry));
        }
        return copyEntries;
    }

    

}
