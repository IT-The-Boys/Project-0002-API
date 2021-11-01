package it.theboys.project0002api.exception.database;

public class CardSetCollectionException extends Exception {

    public CardSetCollectionException(String message){
        super(message);
    }

    public static String NotFoundException(String id){
        return String.format("Set with %s id is not found", id);
    }

    public static String AlreadyExistException(String gameName,String setName){
        return String.format("Set with %s name is already exists in &s", setName, gameName);
    }
}
