/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

import java.util.ArrayList;
import java.util.Scanner;

public class SimpleCompetitions {

    private static Scanner sc = new Scanner(System.in);
    private boolean fromFile; // true if reading from file.
    private boolean normalMode; // true if running normal mode.
    private String loadFileName; // possibly ununsed. if not loading from a file  
    private String memberFileName; // maybe dont make varaibles like these ones global - just need this for reading in so make it local to that function.
    private String billFileName;
    private static final int INIT_COMP_ID = 1;
    private int nextCompID = INIT_COMP_ID; // to keep track of all the IDs - increment for each new comp!
    private static final int NUM_OPTIONS = 5;
    private static final int NEW_COMP = 1;
    private static final int NEW_ENTRIES = 2;
    private static final int DRAW_WINNERS = 3;
    private static final int SUMMARY = 4;
    private static final int EXIT = 5;
    private DataProvider data;
    private ArrayList<Bill> bills;
    private ArrayList<Member> members;
    private ArrayList<Competition> competitions; // list of all competitions. Only 1 can be active
    private int numCompletedComps=0;
    private Competition activeComp = null; // the 1 active competition.// for quick access, store here too.
    // We can have multiple competitions. But only 1 "active" competitions.




    /**
    * Main program that uses the main SimpleCompetitions class
    * @param args main program arguments
    */
    public static void main(String[] args) {
    //Create an object of the SimpleCompetitions class
    SimpleCompetitions simpleComp = new SimpleCompetitions();
    //Add your code to complete the task
    simpleComp.startApp();
}

    public Competition makeNewCompetition() { // call if 1 entered.
        //add a new competition using the given competition type and competition name. (these into constructors)
        // only gets called if their isnt already an active competition.
        // called after user enters 1. in main menu.

        String compType = getCompType();
        String compName = getCompName();
        int compID = nextCompID;
        nextCompID+=1;

        if (compType.equalsIgnoreCase("L")){
            return new LuckyNumbersCompetition(compName, compID, data, !normalMode);
        }
        else if (compType.equalsIgnoreCase("R")){
            return new RandomPickCompetition(compName, compID, data, !normalMode);
        }
        else {
            System.out.println("ERROR: should have made 1 of the two types!!!!!");
            return new RandomPickCompetition(compName, compID, data, !normalMode);
        }
        
    }
    
    private String getCompName(){
        System.out.println("Competition name:");
        return sc.nextLine(); // just a name bruv, can have numbers innit.
    }

    private String getCompType(){
        System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
        String cmd = sc.nextLine();
        while(!validCompTypeResponse(cmd)){
            System.out.println("Invalid competition type! Please choose again");
            System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
            //System.out.println("Valid responses: L,l,R,r");
            cmd = sc.nextLine();
        }
        return cmd; //cmd is one of the valid types. now return it.
    }
    private boolean validCompTypeResponse(String cmd) {
        return cmd.equalsIgnoreCase("R") || cmd.equalsIgnoreCase("L");
    }

/**
 *      System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
        String cmd = sc.nextLine();
        while(!validModeResponse(cmd)){
            System.out.print("valid responses: T,t,N,n"); 
        }
        return cmd.equals("N") || cmd.equals("n"); // return true if running normal mode. 
 */

    private void startApp(){
        competitions = new ArrayList<Competition>();
        fromFile = obtainFilePrefernce();

        if (fromFile){ // only loading from file if atleast one game has gone which would mean the mode has been selected.
            System.out.println("@@@loading from file@@@");
            System.out.println("File name:");
            loadFileName = sc.nextLine();
            //The program will try to load that file and if there is an exception, 
            //it should display an error message and terminate. 
            // TO DO: this loading, once we know how the files are saved.
        } 
        else { // not necessarily using previous setting. re obtain.
            normalMode = obtainModePreference();
        }
        // get remainding mandatory files.
        getMemberAndBillFileNames();
        // read data from mandatory files.
        readData();
        /* not sure if we are meant to read in the contents of the member and bill file
         just yet */
        int selectedOption;
        do {
            displayMenu();
            selectedOption = getSelectedOption();
            /*Note that the user should not be able to 1) create a new competition if there is already an active one, or 
            2) add new entries or draw winners if there is no active competition. 
            Error messages should be displayed if users try to do so. Following are some sample error messages. 
            See all error messages in the visible test cases.*/
            
            switch (selectedOption){
                case NEW_COMP:
                    if (activeComp==null){ // make sure to reset to null once done.
                        activeComp = makeNewCompetition();
                        competitions.add(activeComp);
                        System.out.println("A new competition has been created!");
                        System.out.println(activeComp);
                    }
                    else {
                        System.out.println("There is an active competition. SimpleCompetitions does not support concurrent competitions!");
                    }
                    break;
                case NEW_ENTRIES:
                    if (activeComp==null){
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    // else have active competition.
                    activeComp.addEntries();
                    break;  
                case DRAW_WINNERS:
                    if (activeComp==null){
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    
                    // maybe add check for having added any entries first?
                    boolean success = activeComp.drawWinners();
                    if (success){
                        activeComp.deactivate();
                        activeComp = null;
                        numCompletedComps++;
                    } // else. didnt have any entries and nothing was changed.
                    break;
                case SUMMARY:
                    if (competitions.isEmpty()){
                        System.out.println("No competition has been created yet!");
                        break;
                    }
                    summaryReport();

                    break;
                case EXIT:
                    
                    if (getSavePreference()){
                        saveCompetitions();
                    }
                    
                    System.out.println("Goodbye!");
                    break; // maybe print exit messages?
                default:
                    System.out.println("--this should be handled in getSelectedOption--"+"Unkown command: "+selectedOption); // this is meant to be handled in getSelectedOption. SO if this prints then something is wrong.
            }

        } while(selectedOption!=EXIT); // come back and change this
    }


    private void saveCompetitions(){
        System.out.println("File name:");
        data.updateBillsFile();
        String saveFileName = sc.nextLine(); 
    }


    private void readData(){
        data = new DataProvider(memberFileName, billFileName);
        bills = data.getBills(); // main game engine gets a copy of bills which is now the truth.
        members = data.getMembers(); // same as above
        return;
    }

    private void summaryReport(){
        
        System.out.println("----SUMMARY REPORT----");
        System.out.println("+Number of completed competitions: "+ numCompletedComps);
        System.out.println("+Number of active competitions: "+ (activeComp==null ? "0" : "1"));
        for (Competition comp : competitions){
            System.out.println();
            comp.report();
            
        }
    }


    /**
     * Returns only once valid integer command has been entered.
     * @return the selected integer input command
     */
    private int getSelectedOption(){
        int option=-1;
        String input;
        boolean validInput=false;
         // System.out.println("Please enter an integer number between 1 and "+ NUM_OPTIONS);

        while (!validInput)  {
            input = sc.nextLine().trim();
            if (!input.matches("[0-9]+")){
                System.out.println("A number is expected. Please try again.");
                displayMenu();
                continue;
            }
            option = Integer.parseInt(input);
            if (option>=1 && option <= NUM_OPTIONS){
                validInput=true;
                break; // not required but include for extra safety.
            } 
            else {
                System.out.println("Unsupported option. Please try again!");
                displayMenu();
                continue;
            }
            
            // not broken, invalid INput
            //System.out.println("Please enter an integer number between 1 and "+ NUM_OPTIONS);
        }
        return option;
    }
    

    private void displayMenu(){
        System.out.println(
            "Please select an option. Type 5 to exit.\n" + 
            "1. Create a new competition\n" +
            "2. Add new entries\n" +
            "3. Draw winners\n" +
            "4. Get a summary report\n" +
            "5. Exit"
        );
    }

    private void getMemberAndBillFileNames(){
        System.out.println("Member file:");
        memberFileName = sc.nextLine();
        System.out.println("Bill file:");
        billFileName = sc.nextLine();
    }

    private boolean obtainModePreference(){
        System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
        String cmd = sc.nextLine();
        while(!validModeResponse(cmd)){
            //System.out.println("valid responses: T,t,N,n"); 
            System.out.println("Invalid mode! Please choose again.");
            System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
            cmd = sc.nextLine();
        }
        return cmd.equalsIgnoreCase("N"); // return true if running normal mode. 
    }

    private boolean obtainFilePrefernce(){
        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----\n"+
        "Load competitions from file? (Y/N)?");
        
        return getYesNoResponse();
    }

    private boolean getSavePreference(){
        System.out.println("Save competitions to file? (Y/N)?");
        return getYesNoResponse();
    }

    private boolean getYesNoResponse(){
        String cmd = sc.nextLine();
        
        // check y,Y,n,N were entered
        while (!validYesNoResponse(cmd)){
            //System.out.println("valid responses: Y,y,N,n. Try again.");
            System.out.println("Unsupported option. Please try again!");
            cmd = sc.nextLine(); // mebbe change to next.
        }
        return cmd.equalsIgnoreCase("Y");
        // else n or N was entered as we already check for garbage values.
    }

    public static boolean validYesNoResponse(String cmd){
        return cmd.equalsIgnoreCase("Y") || cmd.equalsIgnoreCase("n");
    } 

    private boolean validModeResponse(String cmd){
        return cmd.equalsIgnoreCase("N") || cmd.equalsIgnoreCase("T"); 
    } 

    public void report() {
    	
    }

    public static Scanner getScanner(){
        return sc;
    }


}
