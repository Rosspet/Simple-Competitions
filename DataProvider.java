import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
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
    private static final int NUM_BILL_DATA = 4;
    /**
     * 
     * @param memberFile A path to the member file (e.g., members.csv)
     * @param billFile A path to the bill file (e.g., bills.csv)
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
     public DataProvider(String memberFile, String billFile) {
         //Add your code here
        this.memberFile = memberFile;
        this.billFile = billFile;
        try {
            getMembersFromFile();   
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
            getBillsFromFile(); 
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

    public Member getMember(String memberID) throws MemberDoesNotExist{
        Iterator<Member> iter = members.iterator();
        Member thisMember;
        while(iter.hasNext()){
            thisMember = iter.next();
            if (thisMember.hasID(memberID)){
                return thisMember;
            }
        }
        // member doesnt exist if still here
        throw new MemberDoesNotExist(memberID);
    }

    

    public boolean billExists(int billID){

        Iterator<Bill> iter = bills.iterator();
        while(iter.hasNext()){
            if(iter.next().hasID(billID)){
                return true;
            }
        }
        return false;
    }


    private void getMembersFromFile() throws DataAccessException, DataFormatException{
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

    private void getBillsFromFile() throws DataAccessException, DataFormatException{
        
        Scanner billStream;
        try {
            billStream = new Scanner(new FileInputStream(billFile));
        }
        catch (FileNotFoundException e){
            throw new DataAccessException(billFile);
        }
        
        //read and parse bills
        bills = new ArrayList<Bill>();
        String billLine;
        String[] billData;
        while (billStream.hasNextLine()){
            billLine = billStream.nextLine();
            billData = billLine.split(","); 
            
            if (billData.length!=NUM_BILL_DATA){
                throw new DataFormatException("Expecting exactly " + NUM_BILL_DATA + 
                "piececs of information per bill. Got " + billData.length);
            }

            try { // try parse entry to make a bill.
                bills.add(
                    new Bill(
                        Integer.parseInt(billData[0]),
                        billData[1], // by keeping string, can avoid errors of parsing into Int '""' and also might want t use chars in id
                        Float.parseFloat(billData[2]),
                        Boolean.parseBoolean(billData[3])
                    )
                );
            }
            catch (Exception e){
                throw new DataFormatException("Error parsing Bill data. BillLine = "+billLine+"\nError:" + e.getMessage());
            }
        }


        billStream.close();
    }


    // getBill
    //alterBill
    //get member
    // altermember

    public Bill getBill(int BillID) throws BillDoesNotExist{
        Iterator<Bill> iter = bills.iterator();
        Bill thisBill;
        while(iter.hasNext()){
            thisBill = iter.next();
            if (thisBill.hasID(BillID)){
                return new Bill(thisBill); // return a copy for security reasons.
            }
        }
        // member doesnt exist if still here
        throw new BillDoesNotExist(BillID);
    }
    
    public boolean billHasMember(int billID){
        Bill bill=null;
        try {
            bill = getBill(billID);
        }
        catch (BillDoesNotExist e) {
            System.out.println("Bill: "+billID+" does not exist");
        }

        return bill.hasMember();
    }
    
    public ArrayList<Bill> getBills(){
        ArrayList<Bill> bills_copy = new ArrayList<Bill>();
        Iterator<Bill> iter = bills.iterator();
        while(iter.hasNext()){
            bills_copy.add(new Bill(iter.next()));
        }
        return bills_copy;
    }

    public ArrayList<Member> getMembers(){
        ArrayList<Member> members_copy = new ArrayList<Member>();
        Iterator<Member> iter = members.iterator();
        while(iter.hasNext()){
            members_copy.add(new Member(iter.next()));
        }
        return members_copy;
    }
    
}

// old unused code. 

/**
 * for (String str : billData){

                // if any empty fields
                if (str.equals("")){
                    throw new DataFormatException("Empty field recieved in bill entry." + 
                    "full entry is:" + billLine);
                }
 */