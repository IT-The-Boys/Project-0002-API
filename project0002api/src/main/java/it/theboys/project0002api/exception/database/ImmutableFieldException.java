package it.theboys.project0002api.exception.database;

public class ImmutableFieldException extends Exception {
    public ImmutableFieldException(String message) {
        super(message);
    }
    public static String ImmutableSetFieldsException(String fieldName){
        return String.format("Cant change field { %s } , because of restrictions", fieldName);
    }
}
