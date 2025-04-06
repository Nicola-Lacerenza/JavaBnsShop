package exceptions;

public class ParserException extends Exception{

    public ParserException(String message){
        super(message);
    }

    public ParserException(String message, Throwable exception){
        super(message,exception);
    }

}
