package weatherwhere.team;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.domain.member.MemberRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;

    //테스트용 데이터 추가
    @PostConstruct
    public void init(){
        Member member = new Member();
        member.setUserId("test");
        member.setUserPw("test!");
        member.setUserMail("test@naver.com");
        member.setUserLocation("경기도");
        member.setUserLocation2("용인시");
        member.setUserLocationNum(1645L);
        member.setUserPhoto("photo");

        memberRepository.save(member);
    }
}
