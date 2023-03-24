package weatherwhere.team;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberJpaRepository;
import weatherwhere.team.repository.member.MemberRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberJpaRepository memberJpaRepository;

    //테스트용 데이터 추가
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        Member member = new Member();
        member.setUserId("test");
        member.setUserPw("test!");
        member.setUserMail("test@naver.com");
//        member.setUserLocation("경기도");
//        member.setUserLocation2("용인시");
        member.setUserLocationNum(1645L);
        member.setUserPhoto("photo");

        memberJpaRepository.save(member);
    }
}
