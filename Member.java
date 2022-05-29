/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Member {
    // currently assuming all members in the csv are valid entries.
    private String ID; // no mention over whether member ID is only numerical
    private String name;
    private String address;
    

    public Member(String ID, String name, String address){
        this.ID=ID;
        this.name=name;
        this.address=address;
    }

        //copy constructor
    public Member(Member member){
        this.ID = member.ID;
        this.name = member.name;
        this.address = member.address;
    }

    public String toString(){
        return ("Member ID: " +ID+ ", Member Name: "+ name);
    }

    public String getMemberID(){
        return ID;
    }

    public String getMemberName(){
        return name;
    }

    public boolean hasID(String ID){
        return (this.ID.equals(ID));
    }
}
