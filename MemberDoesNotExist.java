/**
 * Custom error exception for when seaarching for a member in the data base that
 * does not exist.
 * @author Ross Petridis
 */
public class MemberDoesNotExist extends Exception {
    public MemberDoesNotExist() {
        super("Member doesnt exist\n");
    }

    public MemberDoesNotExist(String memberID) {
        super("Member of id '" + memberID + "' doesn't exist");
    }
}
