package weatherwhere.team.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import weatherwhere.team.domain.Region;
import weatherwhere.team.service.RegionService;

@Controller
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping(value="/region/{regionId}")
    public String createPage(@PathVariable("regionId")Long regionId, Model model){
        Region region=regionService.updateRegionWeather(regionId);
        model.addAttribute("region", region);
        return "main/home";
    }

}
