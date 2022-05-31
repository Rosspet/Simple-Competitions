/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Game Engine for facilitating Competitions. 
 * Keeps track of important data and provides access to copys of data.
 * 
 */
public class SimpleCompetitions {

    private static Scanner sc = new Scanner(System.in);
    private boolean testingMode; // true if running in testingMode.
    private static DataProvider data; // Contains ArrayLists of members and bills
    private ArrayList<Competition> competitions; // ArrayList of all past and present competitions
    private Competition activeComp = null; // Quick access to the current activeComp.
    
    // Constants
    private static final int NUM_OPTIONS = 5;
    private static final int NEW_COMP = 1;
    private static final int NEW_ENTRIES = 2;
    private static final int DRAW_WINNERS = 3;
    private static final int SUMMARY = 4;
    private static final int EXIT = 5;

    /**
    * Main program that uses the main SimpleCompetitions class
    * @param args main program arguments
    */
    public static void main(String[] args) {
    
    // Create instance of game engine and start the application.
    SimpleCompetitions simpleComp = new SimpleCompetitions();
    simpleComp.startApp();
    }

    /**
     * The main Applicaiton start up process which calls main menu process.
     */
    private void startApp(){
        
        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");

        competitions = new ArrayList<Competition>();
        
        if (loadFromFilePreference()){ 
            System.out.println("File name:");
            
            loadCompetitionsFromFile(sc.nextLine());
            setActiveComp();
            testingMode = obtainModeFromExistingCompetitions();

        } 
        else { // Need to get operating mode from User.
            testingMode = obtainModePreference();
        }
         
        // read data from mandatory files then start game.
        readInData();
        runMenuAndGame();
    }

    /**
     * Logic for running the main menu to implement high level 
     * game play logic.
     */
    private void runMenuAndGame(){

        int selectedOption; 

        do {
            displayMenu();
            selectedOption = getSelectedOption();
            
            switch (selectedOption){

                case NEW_COMP: 
                    // only make new comp if there isn't already an existing one.
                    if (activeComp==null){
                        activeComp = makeNewCompetition();
                        competitions.add(activeComp);
                        System.out.println("A new competition has been created!");
                        System.out.println(activeComp);
                    }
                    else {
                        System.out.println("There is an active competition. "+
                                "SimpleCompetitions does not support concurrent competitions!");
                    }
                    break;

                case NEW_ENTRIES:
                    // Only add entries if there is an existing active Competition.
                    if (activeComp==null){
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    // Add entries to the active comp and return an updated version of the members and bills used.
                    data = activeComp.addEntries(data);
                    break; 

                case DRAW_WINNERS:
                    // Only draw if there is a competition to draw from!
                    if (activeComp==null){
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    
                    // Attempt to draw winners (if failed because no entrie exist, dont deactivate the active comp)
                    boolean success = activeComp.drawWinners();
                    if (success){
                        activeComp.deactivate();
                        activeComp = null;
                    }
                    break;

                case SUMMARY:
                    // only print summary if there is stuff to print.
                    if (competitions.isEmpty()){
                        System.out.println("No competition has been created yet!");
                        break;
                    }
                    report();
                    break;

                case EXIT:
                    // Check if its desired to save
                    if (getSavePreference()){
                        saveCompetitions();
                        System.out.println("Competitions have been saved to file.\n"+
                            "The bill file has also been automatically updated.");
                    }
                    System.out.println("Goodbye!");
                    break; 

                default:
                    break;
            }
        } while(selectedOption!=EXIT); // come back and change this
    }
    /**
     * Create a new competition with user commands
     * @return The created competition.
     */
    public Competition makeNewCompetition() {

        String compType = getCompType();
        String compName = getCompName();

        if (compType.equalsIgnoreCase("L")){
            return new LuckyNumbersCompetition(compName, competitions.size()+1, testingMode);
        } else {
            return new RandomPickCompetition(compName, competitions.size()+1, testingMode);
        }
    }
    
    /**
     * Get competition name from user
     * @return The entered competition name.
     */
    private String getCompName(){
        System.out.println("Competition name: ");
        return sc.nextLine();
    }

    /**
     * Get a valid competition type form the user
     * @return The valid competition type. L or R for LuckyNumbers or RandomPick game.
     */
    private String getCompType(){
        String cmd=null;
        boolean validCompType=false;
        
        while(!validCompType){
            System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
            cmd = sc.nextLine();
            validCompType = validCompTypeResponse(cmd);
            if (!validCompType) {
                System.out.println("Invalid competition type! Please choose again.");
            }
        }
        return cmd; 
    }

    /**
     * Decide whether user input was valid competition type
     * @param cmd The inputted command
     * @return True iff valid
     */
    private boolean validCompTypeResponse(String cmd) {
        return cmd.equalsIgnoreCase("R") || cmd.equalsIgnoreCase("L");
    }

    /**
     * Load competitions from binary File or handle exceptions appropriately.
     * @param fileName The file to load competitions from.
     */
    private void loadCompetitionsFromFile(String fileName) {
        ObjectInputStream compInStream=null;
        
        // Establish Stream connection File. 
        try {
            compInStream = new ObjectInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read Competition objects from file.
        try {
            while(true){ // Keep reading until EOFException.
                competitions.add(
                    (Competition)compInStream.readObject()
                );
            } 
        } catch (EOFException e) {
            // Done reading
        } catch (ClassNotFoundException e) {
            System.out.println("Class not Found. Error: "+e.getMessage());
        } catch (InvalidClassException e){
            System.out.println("Invalid class: "+e.getMessage());
        } catch (IOException e){
            System.out.println("IO ERR: "+e.getMessage());
        } finally {

            // close connection to file.
            try {
                compInStream.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Logic for saving competitions to binary file.
     * First gets the filename from user then establishes connection to output, 
     * then actually writes the object or handles exceptions appropriately.
     */
    private void saveCompetitions(){
        
        System.out.println("File name:");
        data.updateBillsFile();
        String saveFileName = sc.nextLine(); 
        ObjectOutputStream compOut=null;

        // Connect to output
        try {
            compOut = new ObjectOutputStream(new FileOutputStream(saveFileName));
            
        }
        catch (IOException e){
            System.out.print(e.getMessage());
            System.exit(1);
        }

        // write to output
        try {
            for(Competition comp : competitions){
                compOut.writeObject(comp);
                //System.out.println("Wrote: "+comp.getName());
            }
        }
        catch (InvalidClassException e) {
            System.out.println("Attempting to write invalid class. Error: "+e.getMessage());
            System.exit(1);
        }
        catch (NotSerializableException e) {
            System.out.println("Cannot serialise object. Error: "+e.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        try{
            compOut.close();
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }
    }

    /**
     *
     * @return Copy of the object containing relevant game data.
     */
    public static DataProvider getData(){
        return new DataProvider(data);
    }

    private void readInData(){
        boolean success = false;
        while (!success) {
            //getMemberAndBillFileNames();
            String memberFileName = getMemberFileName();
            String billFileName = getBillFileName();
            try {
                data = new DataProvider(memberFileName, billFileName);
                success = true;
            } catch (DataAccessException e) {
                System.out.println("Data access Error: " + e.getMessage());
                System.exit(1);
            } catch (DataFormatException e) {
                System.out.println("Invalid data format in file. Error: " + e.getMessage());
                System.exit(1);
            } catch (Exception e) {
                System.out.println("An unhandled exception occured. Error: " + e.getMessage());
                System.exit(1);
            }
        }
        
        return;
    }

    private void report(){
        
        System.out.println("----SUMMARY REPORT----");
        System.out.println("+Number of completed competitions: "+ getNumCompleted());
        System.out.println("+Number of active competitions: "+ (activeComp==null ? "0" : "1"));
        for (Competition comp : competitions){
            System.out.println();
            comp.report();
            
        }
    }

    private void setActiveComp(){
        for(Competition comp : competitions){
            if (comp.isActive()){
                activeComp = comp;
                return;
            }
        }
        return;
    } 

    private int getNumCompleted(){
        int totNumCompleted=0;
        for(Competition comp : competitions){
            totNumCompleted += comp.isActive() ? 0 : 1;
        }
        return totNumCompleted;
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

    /*
    private void getMemberAndBillFileNames(){
        System.out.println("Member file: ");
        memberFileName = sc.nextLine();
        System.out.println("Bill file: ");
        billFileName = sc.nextLine();
    }*/

    private String getMemberFileName(){
        System.out.println("Member file: ");
        return sc.nextLine();
    }

    private String getBillFileName(){
        System.out.println("Bill file: ");
        return sc.nextLine();
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
        return cmd.equalsIgnoreCase("T"); // return true if running normal mode. 
    }

    private boolean obtainModeFromExistingCompetitions(){

        try {
            Competition comp = competitions.get(0);
            return comp.getTestingMode();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No existing competitions to determine operating mode.");
            System.out.println("Asking for operating mode...");
            return obtainModePreference(); // returns true if normal mode.
        }
    }

    private boolean loadFromFilePreference(){
        boolean notValidResp = true;
        String cmd=null;
        while (notValidResp){
            System.out.println("Load competitions from file? (Y/N)?");
            cmd = sc.nextLine();
            if (!validYesNoResponse(cmd)){
                System.out.println("Unsupported option. Please try again!");
                continue;
            }
            // else it is valid.
            notValidResp = false;
        }
        
        return cmd.equalsIgnoreCase("Y");
        
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

    public static Scanner getScanner(){
        return sc;
    }


}
