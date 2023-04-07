package weatherwhere.team.repository.region;

import lombok.Data;
import weatherwhere.team.domain.Region;
import weatherwhere.team.repository.weather.WeatherDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RegionSimpleDto {
    private String parentRegion;

    private String childRegion;

    private Integer maxTemp;

    private Integer minTemp;


    public RegionSimpleDto(Region region, String baseDateTime){
        parentRegion=region.getParentRegion();
        childRegion=region.getChildRegion();
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
