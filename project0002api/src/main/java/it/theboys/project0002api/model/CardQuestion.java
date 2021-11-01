package it.theboys.project0002api.model;

public class CardQuestion {
    
    private int card_pick;
    private int card_draw;

    public void setCard_Pick(int card_pick){
        this.card_pick = card_pick;
    }
    public int getCard_Pick(){
        return card_pick;
    }

    public void setCard_draw(int card_draw){
        this.card_draw = card_draw;
    }
    public int getCard_draw(){
        return card_draw;
    }
}
