package weatherwhere.team.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import weatherwhere.team.domain.Region;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberRepository;
import weatherwhere.team.repository.region.RegionDto;
import weatherwhere.team.service.MemberService;
import weatherwhere.team.service.RegionService;
import weatherwhere.team.service.WeatherService;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberService memberService;
    private final WeatherService weatherService;
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
        String baseDateTime=weatherService.createWeatherList(loginMember.getParentRegion(),loginMember.getChildRegion());
        Region region = regionService.findSpecificRegion(loginMember.getParentRegion(), loginMember.getChildRegion());
        RegionDto regionDto = new RegionDto(region, baseDateTime);


        model.addAttribute("region", region);
        log.info("regionDto 체크 부모지역 : {} ",regionDto.getParentRegion());
        log.info("regionDto 의 첫번째 weather 값 체크 : {} ",regionDto.getWeathers().get(0).getTime());
        model.addAttribute("region",regionDto);
        return "main/home"; //홈으로 리턴
    }
}
