import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * This class allows an instation of the bills and member data into a datastore
 * object.
 * Many methods in this class are used extensively by other classes and so are
 * public - but not at the expense of data privacy.
 * This class is essentially a collection of helper functions for accesdsing and
 * maintaining game data by other game classes.
 * This class includes methods for reading and updating data in the store and
 * ultimately in the files upon end of program if desired.
 * 
 * @author Ross Petridis
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
     * @param billFile   A path to the bill file (e.g., bills.csv)
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public DataProvider(String memberFile, String billFile) throws DataAccessException, DataFormatException {
        // Add your code here
        this.memberFile = memberFile;
        this.billFile = billFile;
        getMembersFromFile();
        getBillsFromFile();
    }

    /**
     * Copy constructor that returns new copies of objects rather than the original.
     * These copy object would not be used for changing data, since they wont be
     * changing the real database, but a copy.
     * Instead, any copies would be purely for reading.
     * 
     * @param data The data class to return a copy of.
     */
    public DataProvider(DataProvider data) {
        this.billFile = data.billFile;
        this.memberFile = data.memberFile;
        this.members = data.getMembers();
        this.bills = data.getBills();
    }

    /**
     * Given a member Id, retrieve this member
     * 
     * @param memberID ID of the member to retrieve.
     * @return The member
     * @throws MemberDoesNotExist if caanot find a member with ID memberID
     */
    public Member getMember(String memberID) throws MemberDoesNotExist {
        Iterator<Member> iter = members.iterator();
        Member thisMember;
        while (iter.hasNext()) {
            thisMember = iter.next();
            if (thisMember.hasID(memberID)) {
                return thisMember;
            }
        }
        // scanned through all members and still here - member doesn't exist.
        throw new MemberDoesNotExist(memberID);
    }

    /**
     * Recieves a potential billID and retrns true if the bill exists
     * 
     * @param billID
     * @return
     */
    public boolean billExists(String billID) {
        Iterator<Bill> iter = bills.iterator();
        while (iter.hasNext()) {
            if (iter.next().hasId(billID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function that returns a bill with given BillID that is previously
     * known to exist - hence no need for try catch of custom BillDoesNotExist
     * 
     * @param billId
     * @return The bill with ID billId
     */
    private Bill getActualBillObject(String billId) {
        Iterator<Bill> iter = bills.iterator();
        Bill thisBill = null;

        while (iter.hasNext()) {
            thisBill = iter.next();
            if (thisBill.hasId(billId)) {
                return thisBill; // not returning copy as this is a private helper for this class only
            }
        }
        System.out.println("failed to find bill that was assumed to exist. existing 1.");
        System.exit(1);
        return thisBill;
    }

    /**
     * sets a bill to used
     * 
     * @param billID the bill id to set to used
     */
    public void setBillToUsed(String billID) {
        getActualBillObject(billID).useBill();
    }

    /**
     * Checks if a bill has been used or not
     * 
     * @param billID
     * @return
     */
    public boolean billHasBeenUsed(String billID) {
        Bill bill = getBillThatExists(billID);
        return bill.hasBeenUsed();
    }

    /**
     * Reads members from file into an arrray list of members
     * 
     * @throws DataAccessException if function cannot access the file
     * @throws DataFormatException if format of the file does not match the expected
     *                             format
     */
    private void getMembersFromFile() throws DataAccessException, DataFormatException {

        // Set up connection
        Scanner memberStream;
        try {
            memberStream = new Scanner(new FileInputStream(memberFile));
        } catch (FileNotFoundException e) {
            throw new DataAccessException(memberFile); // use default constructor
        }

        // read and parse members
        members = new ArrayList<Member>();
        String memberLine;
        String[] memberData;
        int lineNum =0;
        while (memberStream.hasNextLine()) { // while still members to add.
            lineNum++;
            memberLine = memberStream.nextLine();
            memberData = memberLine.split(",");
            if (memberData.length != NUM_MEMBER_DATA) {
                throw new DataFormatException("Expecting exactly " + NUM_MEMBER_DATA +
                        " piececs of information per member. Got " + memberData.length + " in line " + lineNum + 
                        ". DataProvider requires a Member ID, name, and adress");
            }

            if (emptyFieldFound(memberData)){
                throw new DataFormatException("Empty field found in line " + lineNum + ". DataProvider" +
                " requires a Member ID, name, and adress");
            }

            // else, got 3 strings.
            members.add(new Member(
                    memberData[0], // memberID
                    memberData[1], // member name
                    memberData[2] // member address
            ));
        }
        memberStream.close();
    }

    /**
     * Reads bills from file and stores in arraylist of bills
     * 
     * @throws DataAccessException if cannot access the bills file
     * @throws DataFormatException if supplied format does match exapected format.
     */
    private void getBillsFromFile() throws DataAccessException, DataFormatException {

        // Set up connection
        Scanner billStream;
        try {
            billStream = new Scanner(new FileInputStream(billFile));
        } catch (FileNotFoundException e) {
            throw new DataAccessException(billFile);
        }

        // read and parse bills
        bills = new ArrayList<Bill>();
        String billLine;
        String[] billData;
        while (billStream.hasNextLine()) {

            // read this bill
            billLine = billStream.nextLine();
            billData = billLine.split(",");

            if (billData.length != NUM_BILL_DATA) {
                throw new DataFormatException("Expecting exactly " + NUM_BILL_DATA +
                        "piececs of information per bill. Got " + billData.length);
            }

            try { // try parse entry to make a bill.
                bills.add(
                        new Bill(
                                billData[0],
                                billData[1], // by keeping string, can avoid errors of parsing into Int '""' and also
                                             // might want t use chars in id
                                Float.parseFloat(billData[2]),
                                Boolean.parseBoolean(billData[3])));
            } catch (Exception e) { // weird format recieved
                throw new DataFormatException(
                        "Error parsing Bill data. BillLine = " + billLine + "\nError:" + e.getMessage());
            }
        }
        billStream.close();
    }

    /**
     * retrive bill object that is previously known to exist.
     * 
     * @param billID
     * @return
     */
    public Bill getBillThatExists(String billID) {
        Bill bill = getActualBillObject(billID);
        return new Bill(bill);
    }

    /**
     * Given a bill ID, retrieve the assocaited bill object, or throw a
     * Bill does not exist error
     * 
     * @param billID The inputed ID to find the associated bill for
     * @return The associated bill object
     * @throws BillDoesNotExist if the bill being searched for does not exist
     */
    public Bill getBill(String billID) throws BillDoesNotExist {
        Iterator<Bill> iter = bills.iterator();
        Bill thisBill;

        while (iter.hasNext()) {
            thisBill = iter.next();
            if (thisBill.hasId(billID)) {
                return new Bill(thisBill); // return a copy for security reasons.
            }
        }
        // member doesnt exist if still here
        throw new BillDoesNotExist(billID);
    }

    /**
     * Returns
     * 
     * @param billID
     * @return true iff the inputted bill ID has a member associated with it
     */
    public boolean billHasMember(String billID) {
        Bill bill = null;

        try {
            bill = getBill(billID);
        } catch (BillDoesNotExist e) {
            System.out.println("Bill: " + billID + " does not exist");
        }

        return bill.hasMember();
    }

    /**
     * 
     * @return a copy of the Bills
     */
    public ArrayList<Bill> getBills() {
        ArrayList<Bill> bills_copy = new ArrayList<Bill>();
        Iterator<Bill> iter = bills.iterator();

        while (iter.hasNext()) {
            bills_copy.add(new Bill(iter.next()));
        }
        return bills_copy;
    }

    /**
     * 
     * @return a copy of the members
     */
    public ArrayList<Member> getMembers() {
        ArrayList<Member> members_copy = new ArrayList<Member>();
        Iterator<Member> iter = members.iterator();

        while (iter.hasNext()) {
            members_copy.add(new Member(iter.next()));
        }
        return members_copy;
    }

    /**
     * Writes updates to file by re writing the entire file. The same file used for
     * reading. This is to help
     * kepe track of bills that has been used.
     */
    public void updateBillsFile() {

        PrintWriter billOut = null;

        try {
            billOut = new PrintWriter(new FileOutputStream(billFile));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        for (Bill bill : bills) { // re write each bill
            try {
                billOut.println(bill.getId() + "," + bill.getMemberId() + "," + bill.getTotalAmount() +
                        "," + (bill.hasBeenUsed() ? "true" : "false"));
            } catch (Exception e) {
                System.out.println("Failed to write bill of billID :" + bill.getId() + " Error: " + e.getMessage());
            }
        }
        billOut.close();
    }

    /**
     * Recieves an array of string and returns true if there is an empty field
     * @param data array of string to check for empty fields in
     * @return true iff theres an empty string in data.
     */
    private boolean emptyFieldFound(String[] data){
        for (String str : data){
            if (str==""){
                return true;
            }
        }
        return false;

    }
}