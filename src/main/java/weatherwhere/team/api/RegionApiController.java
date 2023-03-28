package weatherwhere.team.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import weatherwhere.team.domain.Region;
import weatherwhere.team.domain.Weather;
import weatherwhere.team.service.RegionService;
import weatherwhere.team.service.WeatherService;

import java.util.List;

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
        Region region = weatherService.createWeatherList(parentRegion,childRegion);
        return "ok";
    }


}
