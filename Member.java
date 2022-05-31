import java.io.Serializable;

/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class Member implements Serializable{
    // currently assuming all members in the csv are valid entries.
    private String ID; // no mention over whether member ID is only numerical
    private String name;
    private String address;
    

    public Member(String ID, String name, String address){
        this.ID=ID;
        this.name=name;
        this.address=address;
    }

    public void setMemberID(String id){
        this.ID = id;
    }

    public String getAdress(){
        return address;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAdress(String address){
        this.address = address;
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
