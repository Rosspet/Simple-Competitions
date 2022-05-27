public class BillDoesNotExist extends Exception{
    public BillDoesNotExist(){
        super("Bill doesn't exist\n");
    }
    public BillDoesNotExist(int BillID){
        super("Bill of id '"+BillID+"' doesn't exist");
    }
}
