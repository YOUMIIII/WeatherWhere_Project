package weatherwhere.team.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="weather")
public class Weather {

    @Id @GeneratedValue
    @Column(name="weather_id")
    private Long id;
    private String baseDateTime;
    private String weatherDateTime;
    private Integer temp; // 온도
    private Integer sky; // 하늘 상태
    
    private Integer pty;//강수형태
    private Integer pop; // 강수확률

    private Integer nx;
    private Integer ny;


    @JoinColumn(name = "region_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    public static Weather createWeather(String baseDateTime,String weatherDateTime,Integer temp,Integer sky,Integer pty,Integer pop,Integer nx,Integer ny,Region region){
        Weather weather=new Weather();
        weather.setBaseDateTime(baseDateTime);
        weather.setWeatherDateTime(weatherDateTime);
        weather.setTemp(temp);
        weather.setSky(sky);
        weather.setPty(pty);
        weather.setPop(pop);
        weather.setNx(nx);
        weather.setNy(ny);
        weather.setRegion(region);
        return weather;
    }

    public void setRegion(Region region){
        this.region=region;
        region.getWeathers().add(this);
    }


}
