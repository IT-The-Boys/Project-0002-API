package it.theboys.project0002api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameName {
    CAH(0, "Cards Against Humanity");
    private final Integer code;
    private final String desc;
}
