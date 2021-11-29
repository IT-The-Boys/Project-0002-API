package it.theboys.project0002api.model.card;

public class BlackCard {

    private final String full;
    private int answers;

    public BlackCard(final String full){
        this.full = full;
        for(final char character : full.toCharArray()){
            if(character == '_'){
                this.answers++;
            }
        }
    }

    public int getAnswers(){
        return this.answers;
    }    
    public String getFull(){
        return this.full;
    }
}
