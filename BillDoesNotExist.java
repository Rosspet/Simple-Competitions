/**
 * Custom Exception for when trying to retrieve a bill that does not exist.
 */
public class BillDoesNotExist extends Exception {
    public BillDoesNotExist() {
        super("Bill doesn't exist\n");
    }

    public BillDoesNotExist(String BillID) {
        super("Bill of id '" + BillID + "' doesn't exist");
    }
}
