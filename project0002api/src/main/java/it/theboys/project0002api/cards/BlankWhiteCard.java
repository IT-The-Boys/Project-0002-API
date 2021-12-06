package it.theboys.project0002api.cards;

import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;

public class BlankWhiteCard extends WhiteCard {
    private static final String BLANK_TEXT = "____";
    private final int id; // Always negative
    private String text = null;

    public BlankWhiteCard(int id) {
        this.id = id;
        clear();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    @Nullable
    public String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    public void clear() {
        setText(BLANK_TEXT);
    }

    @Override
    public String getWatermark() {
        return "____";
    }

    @Override
    public boolean isWriteIn() {
        return true;
    }
}