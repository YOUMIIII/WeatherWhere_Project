package weatherwhere.team.exception;

public class WeatherApiConnFailedException extends RuntimeException{

    public WeatherApiConnFailedException(){}
    public WeatherApiConnFailedException(String message){
        super(message);
    }
    public WeatherApiConnFailedException(String message, Throwable cause){
        super(message, cause);
    }
    public WeatherApiConnFailedException(Throwable cause){
        super(cause);
    }
}
