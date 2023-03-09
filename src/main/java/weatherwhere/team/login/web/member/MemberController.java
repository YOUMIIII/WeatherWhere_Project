package weatherwhere.team.login.web.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import weatherwhere.team.login.domain.member.Location;
import weatherwhere.team.login.domain.member.Member;
import weatherwhere.team.login.domain.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/add") //회원등록 폼으로 보내기
    public String addForm(@ModelAttribute("member") Member member){ //"member"생략 가능하지만 나중에 타임리프로 인식이 안될경우가 있어서 그냥 적었다함
        return "members/signUp";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/login";
    }

    @ModelAttribute("userLocations")
    public List<Location> userLocations() {
        List<Location> userLocations = new ArrayList<>();
        userLocations.add(new Location("SEOUL", "서울특별시"));
        userLocations.add(new Location("GYEONGGI", "경기도"));
        userLocations.add(new Location("GANGWON", "강원도"));
        return userLocations;
    }

    @ModelAttribute("userLocation2")
    public List<Location> subLocationCodes() {
        List<Location> userLocation2 = new ArrayList<>();
        userLocation2.add(new Location("YONGIN", "용인시"));
        userLocation2.add(new Location("SUNGNAM", "성남시"));
        userLocation2.add(new Location("GWANGJU", "광주시"));
        return userLocation2;
    }



}
