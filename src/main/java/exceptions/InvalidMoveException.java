package exceptions;
//defining a custom checked exception  
public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message) {
        super(message); // do not be confused ( directly, it delegates to Exception, and ultimately the message is stored in Throwable.)
    }
}
