package it.theboys.project0002api.enums;

import it.theboys.project0002api.model.database.cah.CahCard;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameName {
    CAH(0, "Cards Against Humanity", CahCard.class);
    private final Integer code;
    private final String desc;
    private final Class<?> cardClass;
}
