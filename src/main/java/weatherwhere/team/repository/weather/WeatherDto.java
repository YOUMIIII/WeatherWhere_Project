package weatherwhere.team.repository.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import weatherwhere.team.domain.Weather;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class WeatherDto {

    private Integer time;

    private Integer temp;

    private String sky;

    private Integer pop;


    public WeatherDto(Weather weather){
        time=Integer.parseInt(weather.getWeatherDateTime().substring(8,10));
        temp=weather.getTemp();
        pop=weather.getPop();
        if(weather.getPty()==0){
            switch (weather.getSky()){
                case 1:
                    sky = "맑음";
                    break;
                case 3:
                    sky="구름 많음";
                    break;
                case 4:
                    sky="흐림";
                    break;
            }
        }else {
            switch (weather.getPty()){
                case 1:
                    sky = "비";
                    break;
                case 2:
                    sky="비/눈";
                    break;
                case 3:
                    sky="눈";
                    break;
                case 4:
                    sky="소나기";
                    break;
            }
        }
        //- 하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4)
        //- 강수형태(PTY) 코드 : (단기) 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    }
}
