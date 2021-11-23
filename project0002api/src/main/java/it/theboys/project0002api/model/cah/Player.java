package it.theboys.project0002api.model.cah;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mongodb.client.model.Collation;

import org.bson.codecs.CollectibleCodec;

import it.theboys.project0002api.model.card.WhiteCard;

public class Player{

    private String name;
    private boolean kaizer;
    private int changedCards = 0;
    private int point = 0;
    private final ArrayList<WhiteCard> whiteCards = new ArrayList<WhiteCard>();
    public Object getCards;

    public Player(final string name) {
        this.name = name;
        this.setKaiser(false);
    }

    public void addChanged(){
        this.changedCards++;
    }

    public void addPoint(){
        this.point++;
    }

    public boolean playCard(final whiteCard card){
        if(!this.whiteCards.contains(card)){
            return false;
        }
        this.whiteCards.remove(card);
        this.playedCards = new whiteCard[]{ card };
        this.whiteCards.add(whiteCards);
        return true;
    }

    public void drawCardsForStart(){
        Collections.shuffle(this.whiteCards);
        int i = 0;
        final List<WhiteCard> newCards = new ArrayList<WhiteCard>(this.whiteCards);
        for(final WhiteCard card : this.whiteCards){
            if(i < 10){
                this.whiteCards.add(card);
                newCards.remove(card);
                i++;
            }
        }
        this.whiteCards = newCards;
    }

    public void setKaizer(final boolean currentKaiser){
        this.kaizer = currentKaiser;
    }

    public void setName(final String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "Player{" + "name = " + this.getName() + '}';
    }

    public ArrayList<WhiteCard> getCards(){
        return this.whiteCards;
    }
}