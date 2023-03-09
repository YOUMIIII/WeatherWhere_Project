package weatherwhere.team.login.web.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import weatherwhere.team.login.domain.login.LoginService;
import weatherwhere.team.login.domain.member.Location;
import weatherwhere.team.login.domain.member.Member;
import weatherwhere.team.login.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login") //로그인 폼을 보여주는 매핑
    public String loginForm(){
        return "login/login";
    }

    @PostMapping("/login")
    public String login( @ModelAttribute LoginForm form, HttpServletRequest request){

        Member loginMember = loginService.login(form.getLoginId(), form.getLoginPw());

        /*if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }*/

        //로그인 성공 처리 TODO
        //세션이 있으면 세션반환, 없으면 신규 세션을 생성
        HttpSession session =  request.getSession(); //()안에 false-기존세션반환, 없으면 null반환 / true(default)-기존세선반환, 없으면 새로운 세션 생성해서 반환
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "main/home";
    }
}
