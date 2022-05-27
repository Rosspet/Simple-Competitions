public class MemberDoesNotExist extends Exception{
    public MemberDoesNotExist(){
        super("Member doesnt exist\n");
    }
    public MemberDoesNotExist(String memberID){
        super("Member of id '"+memberID+"' doesn't exist");
    }
}
