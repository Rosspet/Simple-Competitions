import java.io.Serializable;

/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Mebmer class for keeping track of members and the links to other data
 * sourcecs,
 * such as bills and entry
 */
public class Member implements Serializable {

    private String ID;
    private String name;
    private String address;

    /**
     * Standard cnstructor for when reading members from file
     * 
     * @param ID      the member ID
     * @param name    the name of the member
     * @param address their address
     */
    public Member(String ID, String name, String address) {
        this.ID = ID;
        this.name = name;
        this.address = address;
    }

    // copy constructor
    public Member(Member member) {
        this.ID = member.ID;
        this.name = member.name;
        this.address = member.address;
    }

    /**
     * 
     * @param ID ID to comapre with
     * @return true iff this member has this ID..
     */
    public boolean hasID(String ID) {
        return (this.ID.equals(ID));
    }

    public void setMemberID(String id) {
        this.ID = id;
    }

    public String getAdress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String address) {
        this.address = address;
    }

    public String toString() {
        return ("Member ID: " + ID + ", Member Name: " + name);
    }

    public String getMemberID() {
        return ID;
    }

    public String getMemberName() {
        return name;
    }

}
