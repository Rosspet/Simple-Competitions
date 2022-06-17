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
 * This class implements the Game Engine for facilitating Simple Competitions.
 * The game engine includes methodologies containing high level game logic which
 * is responsible for
 * initiating lower level functions of the program.
 * 
 * @author Ross Petridis
 */
public class SimpleCompetitions {

    private static Scanner sc = new Scanner(System.in);
    private boolean testingMode;
    private DataProvider data; // Contains ArrayLists of members and bills
    private ArrayList<Competition> competitions;
    private Competition activeComp = null; // Quick access to the current activeComp.

    // Constants
    private static final int NUM_OPTIONS = 5;
    private static final int NEW_COMP = 1;
    private static final int NEW_ENTRIES = 2;
    private static final int DRAW_WINNERS = 3;
    private static final int SUMMARY = 4;
    private static final int EXIT = 5;
    public static final String DIGITS_ONLY_REGEX = "[0-9]+";

    /**
     * Main program that uses the main SimpleCompetitions class to start
     * application.
     * 
     * @param args command line arguments used to run program. (None needed)
     */
    public static void main(String[] args) {
        // Create instance of game engine and start the application.
        SimpleCompetitions simpleComp = new SimpleCompetitions();
        simpleComp.startApp();
    }

    /**
     * The main Applicaiton start up process which calls main menu process.
     * Specifically, initiates reading in of data and initiates main menu.
     */
    private void startApp() {

        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");

        competitions = new ArrayList<Competition>();

        if (askUserForLoadFromFile()) {
            System.out.println("File name:");

            loadCompetitionsFromFile(sc.nextLine());
            setActiveComp();
            testingMode = obtainTestModeFromExistingCompetitions();

        } else { // Need to get operating mode from User.
            testingMode = obtainTestModePreference();
        }

        // read data from mandatory files then start game.
        readInData();
        
        runMenuAndGame();
    }

    /**
     * Prompts user for file names, then initiates reading in of data (members and
     * bills) from files
     */
    private void readInData() {
        boolean success = false;
        while (!success) {
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

    /**
     * This method contains crucial logic for running the main menu. The method
     * recieves user input and actions the users command by calling the appropriate
     * functions.
     * For example, To create competitions, add entrys, and draw winners.
     */
    private void runMenuAndGame() {

        int selectedOption;

        do { // MAIN MENU
            displayMenu();
            selectedOption = getSelectedOption();

            switch (selectedOption) {

                case NEW_COMP:
                    if (activeComp == null) {
                        activeComp = makeNewCompetition();
                        competitions.add(activeComp);
                        System.out.println("A new competition has been created!");
                        System.out.println(activeComp);
                    } else {
                        System.out.println("There is an active competition. " +
                                "SimpleCompetitions does not support concurrent competitions!");
                    }
                    break;

                case NEW_ENTRIES:
                    if (activeComp == null) {
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    // Add entries to the active comp and return an updated version of the members
                    // and bills used.
                    data = activeComp.addEntries(new DataProvider(data));
                    break;

                case DRAW_WINNERS:
                    if (activeComp == null) {
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }

                    // Attempt to draw winners (if failed because no entrie exist, dont deactivate
                    // the active comp)
                    boolean success = activeComp.drawWinners(new DataProvider(data));
                    if (success) {
                        activeComp.deactivate();
                        activeComp = null;
                    }
                    break;

                case SUMMARY:
                    // only print summary if there is stuff to print.
                    if (competitions.isEmpty()) {
                        System.out.println("No competition has been created yet!");
                        break;
                    }
                    report();
                    break;

                case EXIT:
                    // Check if its desired to save
                    if (getSaveToFile()) {
                        saveCompetitions();
                        System.out.println("Competitions have been saved to file.\n" +
                                "The bill file has also been automatically updated.");
                    }
                    System.out.println("Goodbye!");
                    break;

                default:
                    break;
            }
        } while (selectedOption != EXIT); // come back and change this
        sc.close();
    }

    /**
     * Ask for user prefences on a new competion, then make the appropriate new
     * competition.
     * 
     * @return The created competition.
     */
    private Competition makeNewCompetition() {

        String compType = getCompType(); // ask user for competition type
        String compName = getCompName(); // ask user for competition name

        if (compType.equalsIgnoreCase("L")) {
            return new LuckyNumbersCompetition(compName, competitions.size() + 1, testingMode);
        } else {
            return new RandomPickCompetition(compName, competitions.size() + 1, testingMode);
        }
    }

    /**
     * Get competition name from user
     * 
     * @return The entered competition name.
     */
    private String getCompName() {
        System.out.println("Competition name: ");
        return sc.nextLine();
    }

    /**
     * Get a valid competition type form the user
     * 
     * @return The valid competition type. L or R for LuckyNumbers or RandomPick
     *         game.
     */
    private String getCompType() {
        String cmd = null;
        boolean validCompType = false;

        while (!validCompType) {
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
     * 
     * @param cmd The inputted command
     * @return True iff valid
     */
    private boolean validCompTypeResponse(String cmd) {
        return cmd.equalsIgnoreCase("R") || cmd.equalsIgnoreCase("L");
    }

    /**
     * Load competitions from binary File or handle exceptions appropriately.
     * 
     * @param fileName The file to load competitions from.
     */
    private void loadCompetitionsFromFile(String fileName) {
        ObjectInputStream compInStream = null;

        // Establish Stream connection File.
        try {
            compInStream = new ObjectInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Read Competition objects from file.
        try {
            while (true) { // Keep reading until EOFException.
                competitions.add(
                        (Competition) compInStream.readObject());
            }
        } catch (EOFException e) {
            // Done reading
        } catch (ClassNotFoundException e) {
            System.out.println("Class not Found. Error: " + e.getMessage());
            System.exit(1);
        } catch (InvalidClassException e) {
            System.out.println("Invalid class error: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            System.exit(1);
        }

        try {
            compInStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Logic for saving competitions to binary file.
     * First gets the filename from user then establishes connection to output,
     * then actually writes the object or handles exceptions appropriately.
     */
    private void saveCompetitions() {

        System.out.println("File name:");
        data.updateBillsFile();
        String saveFileName = sc.nextLine();
        ObjectOutputStream compOut = null;

        // Connect to output
        try {
            compOut = new ObjectOutputStream(new FileOutputStream(saveFileName));

        } catch (IOException e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }

        // write to output
        try {
            for (Competition comp : competitions) {
                compOut.writeObject(comp);
                // System.out.println("Wrote: "+comp.getName());
            }
        } catch (InvalidClassException e) {
            System.out.println("Attempting to write invalid class. Error: " + e.getMessage());
            System.exit(1);
        } catch (NotSerializableException e) {
            System.out.println("Cannot serialise object. Error: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try { // close output stream.
            compOut.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Print the Summary Report on competition data.
     */
    private void report() {

        System.out.println("----SUMMARY REPORT----");
        System.out.println("+Number of completed competitions: " + getNumCompleted());
        System.out.println("+Number of active competitions: " + (activeComp == null ? "0" : "1"));
        for (Competition comp : competitions) {
            System.out.println();
            comp.report();

        }
    }

    /**
     * Find the active comp in the competition list, as set the variable activeComp
     * to it
     * for ease of access.
     */
    private void setActiveComp() {
        for (Competition comp : competitions) {
            if (comp.isActive()) {
                activeComp = comp;
                return;
            }
        }
        return;
    }

    /**
     * 
     * @return the number of competitions that have been completed in the list of
     *         competitions
     */
    private int getNumCompleted() {
        int totNumCompleted = 0;
        for (Competition comp : competitions) {
            totNumCompleted += comp.isActive() ? 0 : 1;
        }
        return totNumCompleted;
    }

    /**
     * Returns only once valid integer command has been entered.
     * 
     * @return the selected integer input command
     */
    private int getSelectedOption() {
        int option = -1;
        String input;
        boolean validInput = false;

        while (!validInput) {

            input = sc.nextLine().trim();

            // check if integer type
            if (!input.matches(DIGITS_ONLY_REGEX)) {
                System.out.println("A number is expected. Please try again.");
                displayMenu();
                continue;
            }

            // is integer type, check if in range
            option = Integer.parseInt(input);
            if (option >= 1 && option <= NUM_OPTIONS) {
                validInput = true;
                break;
            } else { // out of range.
                System.out.println("Unsupported option. Please try again!");
                displayMenu();
                continue;
            }
        }
        return option;
    }

    /**
     * Display menu options
     */
    private void displayMenu() {
        System.out.println(
                "Please select an option. Type " + EXIT + " to exit.\n" +
                        NEW_COMP + ". Create a new competition\n" +
                        NEW_ENTRIES + ". Add new entries\n" +
                        DRAW_WINNERS + ". Draw winners\n" +
                        SUMMARY + ". Get a summary report\n" +
                        EXIT + ". Exit");
    }

    /**
     * Prompt user for memberFile name
     * 
     * @return The inputted name for member file
     */
    private String getMemberFileName() {
        System.out.println("Member file: ");
        return sc.nextLine();
    }

    /**
     * Prompt user for memberFile name
     * 
     * @return The inputted name for member file
     */
    private String getBillFileName() {
        System.out.println("Bill file: ");
        return sc.nextLine();
    }

    /**
     * Obtains operating mode from the user.
     * Either Test or Normal are the possible modes.
     * Test uses deterministic seeds for generating "randomness"
     * 
     * @return True iff testing mode is being used.
     */
    private boolean obtainTestModePreference() {
        System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
        String cmd = sc.nextLine();
        while (!validModeResponse(cmd)) {
            // System.out.println("valid responses: T,t,N,n");
            System.out.println("Invalid mode! Please choose again.");
            System.out.println("Which mode would you like to run? (Type T for Testing, and N for Normal mode):");
            cmd = sc.nextLine();
        }
        return cmd.equalsIgnoreCase("T"); // return true if running normal mode.
    }

    /**
     * Uses existing competitions to decipher the approprite mode of operation.
     * Called after reading competitions form file.
     * 
     * @return true iff testing mode.
     */
    private boolean obtainTestModeFromExistingCompetitions() {

        try {
            Competition comp = competitions.get(0);
            return comp.getTestingMode();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No existing competitions to determine operating mode.");
            System.out.println("Asking for operating mode...");
            return obtainTestModePreference(); // returns true if normal mode.
        }
    }

    /**
     * Get preference for loading game form file or
     * starting game from scratch
     * 
     * @return true iff desired to load gamefrom file.
     */
    private boolean askUserForLoadFromFile() {
        boolean notValidResp = true;
        String cmd = null;
        while (notValidResp) {
            System.out.println("Load competitions from file? (Y/N)?");
            cmd = sc.nextLine();
            if (!validYesNoResponse(cmd)) {
                System.out.println("Unsupported option. Please try again!");
                continue;
            }
            // else it is valid.
            notValidResp = false;
        }
        return cmd.equalsIgnoreCase("Y");
    }

    /**
     * Asks and processes if user want to save game data to a file.
     * 
     * @return true iff desired to save to file.
     */
    private boolean getSaveToFile() {
        System.out.println("Save competitions to file? (Y/N)?");
        return getYesNoResponse();
    }

    /**
     * Asks user for a yes/no anser then maps this to a bool
     * 
     * @return true iff yes.
     */
    private boolean getYesNoResponse() {
        String cmd = sc.nextLine();
        while (!validYesNoResponse(cmd)) {
            System.out.println("Unsupported option. Please try again!");
            cmd = sc.nextLine(); // mebbe change to next.
        }
        return cmd.equalsIgnoreCase("Y");
    }

    /**
     * Checks if inputted string is valid yes or no anser
     * Static to make this useful (general) method accessible to oter classes that
     * call upon uer input.
     * 
     * @param cmd the input
     * @return true if cmd is valid yes or no answer by comparing to
     *         "y" or "n". Comparison is case insensitive.
     */
    public static boolean validYesNoResponse(String cmd) {
        return cmd.equalsIgnoreCase("Y") || cmd.equalsIgnoreCase("n");
    }

    /**
     * Checks if inputted string is valid test or normal anser
     * 
     * @param cmd the input
     * @return true if cmd is valid test or normal answer by comparing to
     *         "N" or "T". Comparison is case insensitive.
     */
    private boolean validModeResponse(String cmd) {
        return cmd.equalsIgnoreCase("N") || cmd.equalsIgnoreCase("T");
    }

    /**
     * Provides access to standard input.
     * 
     * @return Scanner object for reading form stdin.
     */
    public static Scanner getScanner() {
        return sc;
    }
}
