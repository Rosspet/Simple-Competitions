public class Winner extends Member {

    private NumbersEntry winningEntry;
    
    public Winner(Member member, NumbersEntry winningEntry){
        super(member);
        this.winningEntry = winningEntry;
    }


    public void setWinningEntry(NumbersEntry winningEntry){
        this.winningEntry = winningEntry;
    }

    public void upDateWinner(NumbersEntry winningEntry){
        // recieves a winning entry. checks if this entry should replace the current winning enmtry
        // atm. awarding highest prize per winner. (if multiuple of these, award the one with smallest ID)
    }

}
