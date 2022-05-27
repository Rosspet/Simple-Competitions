import java.util.ArrayList;

public class LuckyNumbersEntry extends Entry {
    
    private ArrayList<Integer> numbers;

    public LuckyNumbersEntry(int entryId, String billId, String memberId, ArrayList<Integer> numbers){
        super(entryId, billId, memberId);
        this.numbers = numbers;
        
    }
}
