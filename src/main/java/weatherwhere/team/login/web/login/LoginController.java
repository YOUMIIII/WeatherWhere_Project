package weatherwhere.team.login.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import weatherwhere.team.login.domain.member.Location;
import weatherwhere.team.login.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @GetMapping("/login") //로그인 폼을 보여주는 매핑
    public String loginForm(){
        return "front/front";
    }


}
