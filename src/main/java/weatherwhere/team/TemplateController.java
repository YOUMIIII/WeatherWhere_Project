package weatherwhere.team;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {
    @GetMapping("/main/test")
    public String template(){
        return "main/test";
    }

    /*@GetMapping("/home")
    public String mainTest(){
        return "main/home";
    }*/
}
