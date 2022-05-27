/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Competition {
    private String name; //competition name
    private int id; //competition identifier
    private String type;
    private static final int BILL_ID_LENGTH = 6;

    public Competition(String compName, int compID, String type){
        id = compID;
        name = compName;
        this.type = type;
    }

    public abstract void addEntries();

    public abstract void drawWinners();

    public void report() {
    }

    public String toString(){
        return "Competition ID: " + id + ", Competition Name: " + name + ", Type: " + type ;    
    }
    public void setType(String type){
        this.type = type;
    }

    public String getBillID(){
        System.out.println("Bill ID:");
        Scanner scanner = SimpleCompetitions.getScanner();
        boolean validResponse = false;
        String response;
        while (!validResponse){
            response = scanner.nextLine();
            validResponse = (validBillID(response) && billHasValidMemberID(response);
        }
        return response;
        
    }

    private boolean billHasValidMemberID(String resposne){
        
    }

    private boolean validBillID(String billID){
        if (billID.length()!=BILL_ID_LENGTH || !billID.matches("[0-9]+")){
            System.out.println("Invalid bill id! It must be a 6-digit number. Please try again.");
            return false;
        }
        return true;

    }

}
