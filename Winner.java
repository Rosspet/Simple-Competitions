public class Winner extends Member implements Comparable<Winner> {

    private Entry winningEntry;
    private int points;
    private int entryID;
    
    public Winner(Member member, NumbersEntry winningEntry, int points, int entryId){
        super(member);
        this.winningEntry = winningEntry;
        this.points = points;
        this.entryID = entryId;
    }

    public Winner(Member member, Entry winningEntry, int points, int entryId){
        super(member);
        this.winningEntry = winningEntry;
        this.points = points;
        this.entryID = entryId;
    }

    public Winner(Winner winner){
        super(winner.getMemberID(), winner.getMemberName(), winner.getAdress());
        this.points = winner.points;
        this.entryID = winner.entryID;
        this.winningEntry = winner.winningEntry;

    }

    @Override
    public int compareTo(Winner otherWinner){
        // for sorting winners.
        return otherWinner.entryID < this.entryID ? 1 : otherWinner.entryID > this.entryID ? -1 : 0;
    }

    public void setWinningEntry(NumbersEntry winningEntry){
        this.winningEntry = winningEntry;
    }

    public void updatePrize(NumbersEntry winningEntry, int points, int entryId){
        // recieves a winning entry. checks if this entry should replace the current winning enmtry
        // atm. awarding highest prize per winner. (if multiuple of these, award the one with smallest ID)
        if (points > this.points || (points==this.points && entryId<this.entryID)){
            // more poitns or same points but earlier entryId, then update
            this.winningEntry=winningEntry;
            this.points=points;
            this.entryID=entryId;
        }
    }

    public String toString(){
        return "Member ID: " + getMemberID() + ", Member Name: " + getMemberName() + 
            ", Prize: " + points; 
    }

    public void printEntry(){
        System.out.println(
            "--> Entry ID: "+entryID+", Numbers: " +((NumbersEntry)winningEntry).getEntriesString() 
        );
    }

    public int getEntryId(){
        return entryID;
    }

    public int getPoints(){
        return points;
    }

}
