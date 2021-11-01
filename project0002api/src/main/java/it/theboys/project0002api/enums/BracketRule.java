package it.theboys.project0002api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum BracketRule {
    WHITE("white", "No restrictions: Suitable for all ages / Aimed at young audiences / Exempt / Not rated / No applicable rating"),
    YELLOW("yellow", "No restrictions: Parental guidance is suggested for designated age range."),
    PURPLE("purple", "No restrictions: Not recommended for a younger audience but not restricted."),
    RED("red", "Restricted: Parental accompaniment required for younger audiences."),
    BLACK("black", "Prohibitive: Exclusively for older audience / Purchase age-restricted / Banned");

    private final String code;
    private final String description;
    private static final Map<String, String> ruleMap = Collections.unmodifiableMap(initializeMapping());

    public static String getDescriptionByCode(String code) {
        if (ruleMap.containsKey(code)) {
            return ruleMap.get(code);
        }
        return null;
    }

    private static Map<String, String> initializeMapping() {
        Map<String, String> ruleMap = new HashMap<>();
        for (BracketRule s : BracketRule.values()) {
            ruleMap.put(s.code, s.description);
        }
        return ruleMap;
    }
}
