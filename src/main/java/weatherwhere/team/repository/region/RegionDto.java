package weatherwhere.team.repository.region;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import weatherwhere.team.domain.Region;
import weatherwhere.team.domain.Weather;
import weatherwhere.team.repository.weather.WeatherDto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class RegionDto {

    private String parentRegion;

    private String childRegion;

    private Integer nx;

    private Integer ny;

    private List<WeatherDto> weathers = new ArrayList<>();

    private Integer maxTemp;

    private Integer minTemp;


    public RegionDto(Region region,String baseDateTime){
        parentRegion=region.getParentRegion();
        childRegion=region.getChildRegion();
        nx=region.getNx();
        ny=region.getNy();
        weathers=region.getWeathers().stream()
                .filter(weather -> weather.getBaseDateTime().equals(baseDateTime))
                .map(weather->new WeatherDto(weather))
                .collect(Collectors.toList());
        maxTemp=region.getWeathers().stream()
                .filter(weather -> weather.getBaseDateTime().equals(baseDateTime))
                .mapToInt(weather -> weather.getTemp())
                .max().getAsInt();
        minTemp=region.getWeathers().stream()
                .filter(weather -> weather.getBaseDateTime().equals(baseDateTime))
                .mapToInt(weather->weather.getTemp())
                .min().getAsInt();
    }
}
