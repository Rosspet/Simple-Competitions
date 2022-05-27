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
            validResponse = (Bill.validBillID(response) && billHasValidMemberID(response));
        }
        return response;
        
    }

    
    

}
