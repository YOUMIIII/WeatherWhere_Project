package weatherwhere.team.web.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardHomeController {
    @GetMapping("/board")
    public String index() {
        return "index";
    }
}