package it.theboys.project0002api.exception.database;

public class CardCollectionException extends Exception {

    public CardCollectionException(String message){
        super(message);
    }

    public static String NotFoundException(String id){
        return String.format("Card with %s is not found", id);
    }

    public static String AlreadyExistException(String cardText,String setName){
        return String.format("Card with <%s> text is already exists in %s", cardText, setName);
    }
}
