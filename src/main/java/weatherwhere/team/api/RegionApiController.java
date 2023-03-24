package weatherwhere.team.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weatherwhere.team.service.RegionService;

@RestController//@Controller + @ResponseBody
@RequestMapping("/region")
@RequiredArgsConstructor
public class RegionApiController {
    private final RegionService regionService;

    @PostMapping("/insert")
    public String regionInsert(){
        regionService.post();
        return "ok";
    }


}
