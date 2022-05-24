/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

import java.util.Scanner;

public class SimpleCompetitions {

    private Scanner sc = new Scanner(System.in);
    private boolean fromFile; // true if reading from file.
    private boolean normalMode; // true if running normal mode. 
    
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

    public Competition addNewCompetition() {
        
    }

    public void report() {
    	
    }

    
    private void startApp(){
        
        fromFile = obtainFilePrefernce();
        normalMode = obtainModePreference();

        if (fromFile){
            System.out.println("@@@loading from file@@@");
        } else {
            
        }
        if (normalMode){
            System.out.println("@@@playing normal@@@");
        } else {

        }
        
        
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
}
