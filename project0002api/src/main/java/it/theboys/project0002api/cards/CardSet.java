package it.theboys.project0002api.cards;

import java.util.Set;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;


public abstract class CardSet {

    public abstract int getId();

    public abstract String getName();

    public abstract String getDescription();

    public abstract boolean isActive();

    public abstract boolean isBaseDeck();

    public abstract int getWeight();

    public abstract Set<? extends BlackCard> getBlackCards();

    public abstract Set<? extends WhiteCard> getWhiteCards();

    public final JsonWrapper getClientMetadataJson() {
        JsonWrapper obj = new JsonWrapper();
        obj.add(Consts.CardSetData.ID, getId());
        obj.add(Consts.CardSetData.CARD_SET_NAME, getName());
        obj.add(Consts.CardSetData.CARD_SET_DESCRIPTION, getDescription());
        obj.add(Consts.CardSetData.WEIGHT, getWeight());
        obj.add(Consts.CardSetData.BASE_DECK, isBaseDeck());
        obj.add(Consts.CardSetData.BLACK_CARDS_IN_DECK, getBlackCards().size());
        obj.add(Consts.CardSetData.WHITE_CARDS_IN_DECK, getWhiteCards().size());
        return obj;
    }

    @Override
    public String toString() {
        return String.format("%s[name=%s, base=%b, id=%d, active=%b, weight=%d, black=%d, white=%d]",
                getClass().getName(), getName(), isBaseDeck(), getId(), isActive(), getWeight(),
                getBlackCards().size(), getWhiteCards().size());
    }
}