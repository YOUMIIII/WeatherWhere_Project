package weatherwhere.team.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberJpaRepository;
import weatherwhere.team.repository.member.MemberRepository;
import weatherwhere.team.service.MemberService;
import weatherwhere.team.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login") //로그인 폼을 보여주는 매핑
    public String loginForm(@ModelAttribute("loginForm") weatherwhere.team.web.login.LoginForm form){
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute weatherwhere.team.web.login.LoginForm form, BindingResult bindingResult, @RequestParam(defaultValue = "/")String redirectURL, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/login";
        }

        //userId , userPw 로
        Member loginMember = memberService.login(form.getLoginId(), form.getLoginPw());

        if(loginMember == null){
            bindingResult.reject("loginFail", "입력하신 아이디나 비밀번호를 확인해주세요.");
            return "login/login";
        }

        //로그인 성공 처리 TODO
        //세션이 있으면 세션반환, 없으면 신규 세션을 생성
        HttpSession session =  request.getSession(); //()안에 false-기존세션반환, 없으면 null반환 / true(default)-기존세선반환, 없으면 새로운 세션 생성해서 반환
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션 삭제
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/login";
    }
}
