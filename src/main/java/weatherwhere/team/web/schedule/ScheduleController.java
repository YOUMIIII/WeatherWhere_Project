package weatherwhere.team.web.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import weatherwhere.team.login.domain.member.Member;
import weatherwhere.team.login.domain.member.MemberRepository;
import weatherwhere.team.web.SessionConst;

@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final MemberRepository memberRepository;

    @GetMapping("/schedule")
    public String schedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        //세션에 회원 데이터가 없으면 login
        if (loginMember == null) {
            return "redirect:/login";
        }

        //세션이 유지되면 스케줄으로 이동
        model.addAttribute("member", loginMember);
        return "main/schedule";
    }
}
