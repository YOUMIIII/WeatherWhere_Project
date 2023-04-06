package weatherwhere.team.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import weatherwhere.team.domain.Region;
import weatherwhere.team.domain.Weather;
import weatherwhere.team.repository.region.RegionDto;
import weatherwhere.team.repository.region.RegionNameDto;
import weatherwhere.team.service.RegionService;
import weatherwhere.team.service.WeatherService;

import java.util.List;
import java.util.stream.Collectors;

@RestController//@Controller + @ResponseBody
@RequestMapping("/region")
@RequiredArgsConstructor
@Slf4j
public class RegionApiController {
    private final RegionService regionService;
    private final WeatherService weatherService;

    @PostMapping("/insert")
    public String regionInsert(){
        regionService.post();
        return "ok";
    }

    @GetMapping("/weather")
    public String regionWeatherRequest(@RequestParam("parentRegion") String parentRegion, @RequestParam("childRegion") String childRegion){
//        Region region = weatherService.createWeatherList(parentRegion,childRegion);
        return "ok";
    }

    @GetMapping("/api")
    public List<RegionNameDto> sendRegionData(){
        List<RegionNameDto> regionList = regionService.getRegionList().stream()
                .map(region -> new RegionNameDto(region))
                .collect(Collectors.toList());
        return regionList;
    }


    @GetMapping("/api/currentWeather")
    public RegionDto sendCurrentWeather(@RequestParam String parentRegion, @RequestParam String childRegion){

        String baseDateTime = weatherService.createWeatherList(parentRegion, childRegion);
        Region region = regionService.findSpecificRegion(parentRegion, childRegion);
        RegionDto regionDto = new RegionDto(region, baseDateTime);

        return regionDto;
    }

    @GetMapping("/api/pastWeather")
    public RegionDto sendPastWeather(@RequestParam String parentRegion,@RequestParam String childRegion){
        String baseDateTime = weatherService.createPastWeatherList(parentRegion,childRegion,"2023-04-05");
        Region region = regionService.findSpecificRegion(parentRegion, childRegion);
        RegionDto regionDto = new RegionDto(region, baseDateTime);
        return regionDto;
    }


}
