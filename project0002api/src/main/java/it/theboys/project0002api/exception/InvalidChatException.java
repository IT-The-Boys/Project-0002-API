package it.theboys.project0002api.exception;

public class InvalidChatException extends Exception{
    
    private String message;

    public InvalidChatException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
