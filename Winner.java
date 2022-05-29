public class Winner extends Member {

    private NumbersEntry winningEntry;
    private int points;
    private int entryID;
    
    public Winner(Member member, NumbersEntry winningEntry, int points, int entryId){
        super(member);
        this.winningEntry = winningEntry;
        this.points = points;
        this.entryID = entryId;
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
            ", Entry ID: " + entryID + ", Prize: " + points; 
    }

}
