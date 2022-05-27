import java.util.ArrayList;
import java.util.Iterator;

public class LuckyNumbersEntry extends Entry {
    
    private ArrayList<Integer> numbers;
    private boolean auto;
    public LuckyNumbersEntry(int entryId, String billId, String memberId, ArrayList<Integer> numbers, boolean auto){
        super(entryId, billId, memberId);
        this.numbers = numbers;

    }

    public String toString(){
        String returnString = String.format("Entry ID: %6d Numbers: ", getEntryiD()) + getEntriesString();
        if (auto){
            returnString+="[Auto]";
        }
        return returnString;
    }

    private String getEntriesString(){
        String numbs="";
        Iterator<Integer> iter = getNumbers().iterator(); 
        while (iter.hasNext()){
            numbs += String.format("%2d ", iter.next());
        }
        return numbs;
    }

    public ArrayList<Integer> getNumbers(){
        return new ArrayList<Integer>(numbers);
    }
}
