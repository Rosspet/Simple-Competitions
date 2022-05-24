/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

import java.util.ArrayList;
import java.util.Scanner;

public class SimpleCompetitions {

    private Scanner sc = new Scanner(System.in);
    private boolean fromFile; // true if reading from file.
    private boolean normalMode; // true if running normal mode.
    private String loadFileName; // possibly ununsed. if not loading from a file  
    private String memberFileName;
    private String billFileName;
    private static final int NUM_OPTIONS = 5;
    private static final int NEW_COMP = 1;
    private static final int NEW_ENTRIES = 2;
    private static final int DRAW_WINNERS = 3;
    private static final int SUMMARY = 4;
    private static final int EXIT = 5;
    private ArrayList<Competition> competitions; // list of all competitions. Only 1 can be active
    private Competition activeComp = null; // the 1 active competition.// for quick access, store here too.
    // We can have multiple competitions. But only 1 "active" competitions.




    /**
    * Main program that uses the main SimpleCompetitions class
    * @param args main program arguments
    */
    public static void main(String[] args) {
    //Create an object of the SimpleCompetitions class
    SimpleCompetitions sc = new SimpleCompetitions();
    //Add your code to complete the task
    sc.startApp();
}

    public Competition addNewCompetition() { // call if 1 entered.
        //add a new competition using the given competition type and competition name. (these into constructors)
        // only gets called if their isnt already an active competition.

    }

    
    private void startApp(){
        
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
                    if (!activeComp){
                        addNewCompetition();
                    }
                    else {
                        System.out.println("There is an active competition. SimpleCompetitions does not support concurrent competitions!";)
                    }
                    break;
                case NEW_ENTRIES:
                    break;
                case DRAW_WINNERS:
                    break;
                case SUMMARY:
                    break;
                case EXIT:
                    break; // maybe print exit messages?
                default:
                    System.out.println("--this should be handled in getSelectedOption--"+"Unkown command: "+selectedOption); // this is meant to be handled in getSelectedOption. SO if this prints then something is wrong.
            }



        } while(selectedOption!=EXIT); // come back and change this
    }

    /**
     * Returns only once valid integer command has been entered.
     * @return the selected integer input command
     */
    private int getSelectedOption(){
        int option=-1;
        while (option<1 && option>NUM_OPTIONS){
            while (!sc.hasNextInt()) {
                System.out.println("Please enter an integer number between 1 and "+ NUM_OPTIONS);
                sc.next();
            }
            option=sc.nextInt(); // whats this do with the new line?
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
            System.out.print("valid responses: T,t,N,n"); 
        }
        return cmd.equals("N") || cmd.equals("n"); // return true if running normal mode. 
    }

    private boolean obtainFilePrefernce(){
        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----\n"+
        "Load competitions from file? (Y/N)?");
        String cmd = sc.nextLine();
        
        // check y,Y,n,N were entered
        while (!validYesNoResponse(cmd)){
            System.out.println("valid responses: Y,y,N,n. Try again.");
            cmd = sc.nextLine(); // mebbe change to next.
        }
        return cmd.equals("Y") || cmd.equals("y");
        // else n or N was entered as we already check for garbage values.
        
    }

    private boolean validYesNoResponse(String cmd){
        return cmd.equals("Y") || cmd.equals("y") || cmd.equals("n") || cmd.equals("N");
    }

    private boolean validModeResponse(String cmd){
        return cmd.equals("N") || cmd.equals("n") || cmd.equals("T") || cmd.equals("t");
    } 

    public void report() {
    	
    }
}
