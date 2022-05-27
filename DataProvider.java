import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class DataProvider {

    private String memberFile;
    private String billFile;
    private ArrayList<Member> members;
    private ArrayList<Bill> bills; 
    private static final int NUM_MEMBER_DATA = 3; 
    /**
     * 
     * @param memberFile A path to the member file (e.g., members.csv)
     * @param billFile A path to the bill file (e.g., bills.csv)
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
     public DataProvider(String memberFile, String billFile) 
                        throws DataAccessException, DataFormatException {
         //Add your code here
        this.memberFile = memberFile;
        this.billFile = billFile;
        try {
            getMembers();   
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (DataFormatException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        
        try {
            getBills(); 
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (DataFormatException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

     }

     private void getMembers() throws DataAccessException, DataFormatException{
         Scanner memberStream;
        try {
             memberStream = new Scanner(new FileInputStream(memberFile));
        }
        catch (FileNotFoundException e) {
            throw new DataAccessException(memberFile); // use default constructor 
        }
            
        //read and parse members
        members = new ArrayList<Member>();
        String memberLine;
        String[] memberData;

        while (memberStream.hasNextLine()){ // while still members to add.
            memberLine = memberStream.nextLine();
            memberData = memberLine.split(",");//= new String[NUM_MEMBER_DATA];
            if (memberData.length!=NUM_MEMBER_DATA){
                throw new DataFormatException("Expecting exactly " + NUM_MEMBER_DATA + 
                "piececs of information per member. Got " + memberData.length);
            }
            // else, got 3 strings.
            members.add(new Member(
                memberData[0], // memberID
                memberData[1], // member name
                memberData[2]  // member address
                )
            );

        }

        memberStream.close();
     }

     private void getBills() throws DataAccessException, DataFormatException{
        
        Scanner billStream;
        try {
            billStream = new Scanner(new FileInputStream(billFile));
        }
        catch (FileNotFoundException e){
            throw new DataAccessException(billFile);
        }
        
        //read and parse bills
        bills = new ArrayList<Bill>();

        
        billStream.close();
    }
}
