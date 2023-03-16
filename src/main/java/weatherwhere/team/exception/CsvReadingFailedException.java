package weatherwhere.team.exception;

public class CsvReadingFailedException extends RuntimeException{

    public CsvReadingFailedException(){}
    public CsvReadingFailedException(String message){
        super(message);
    }
    public CsvReadingFailedException(String message, Throwable cause){
        super(message, cause);
    }
    public CsvReadingFailedException(Throwable cause){
        super(cause);
    }
}
