package it.theboys.project0002api.enums.cah;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CahCardType {
    QUESTION(0, "Black card"),
    ANSWER(1, "White card");
    private final Integer code;
    private final String desc;
}
