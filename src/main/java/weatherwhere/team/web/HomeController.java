package weatherwhere.team.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import weatherwhere.team.domain.Region;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberJpaRepository;
import weatherwhere.team.repository.member.MemberRepository;
import weatherwhere.team.service.MemberService;
import weatherwhere.team.service.RegionService;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final RegionService regionService;


    @GetMapping("/")
    public String homeLoginSpring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        //로그인
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "redirect:/login";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        Region region=regionService.updateRegionWeather(loginMember.getUserLocationNum());
        model.addAttribute("region", region);
        return "main/home"; //홈으로 리턴
    }
}
