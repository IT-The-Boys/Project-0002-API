package it.theboys.project0002api.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterOperationEnum {
    EQUAL( 0,"eq"),
    NOT_EQUAL(1,"neq"),
    GREATER_THAN(2,"gt"),
    GREATER_THAN_OR_EQUAL_TO(3,"gte"),
    LESS_THAN(4,"lt"),
    LESS_THAN_OR_EQUAL_TO(5,"lte"),
    IN(6,"in"),
    NOT_IN(7,"nin"),
    BETWEEN(8,"btn"),
    CONTAINS(9,"like"),
    NOT_CONTAINS(10,"notLike"),
    IS_NULL(11,"isnull"),
    IS_NOT_NULL(12,"isnotnull"),
    START_WITH(13,"startwith"),
    END_WITH(14,"endwith"),
    IS_EMPTY(15,"isempty"),
    IS_NOT_EMPTY(16,"isnotempty"),
    JOIN(17,"jn"),
    IS(18,"is");

    private final int code;
    private final String value;


    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }



    public static FilterOperationEnum fromValue(String value) {
        for (FilterOperationEnum operation : FilterOperationEnum.values()) {

            //Case insensitive operation name
            if (String.valueOf(operation.value).equalsIgnoreCase(value)) {
                return operation;
            }
        }
        return null;
    }
}
