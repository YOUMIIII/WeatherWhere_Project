package weatherwhere.team;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberJpaRepository;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberJpaRepository memberJpaRepository;

    //테스트용 데이터 추가
    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        Member member = new Member();
        member.setUserId("test");
        member.setUserPw("test!");
        member.setUserMail("test@naver.com");
        member.setParentRegion("경기도");
        member.setChildRegion("성남시분당구");
        member.setUserPhoto("/img/home/profile/profile.png");

        memberJpaRepository.save(member);
    }
    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
    public void init2(){
        Member member = new Member();
        member.setUserId("aa");
        member.setUserPw("aa");
        member.setUserMail("test@naver.com");
        member.setParentRegion("경기도");
        member.setChildRegion("용인시수지구");
        member.setUserPhoto("/img/home/profile/profile.png");
        memberJpaRepository.save(member);
    }

    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
    public void init3(){
        Member member = new Member();
        member.setUserId("qwer");
        member.setUserPw("qwer!");
        member.setUserMail("qwer@naver.com");
        member.setParentRegion("서울특별시");
        member.setChildRegion("강동구");
        member.setUserPhoto("/img/home/profile/profile.png");
        memberJpaRepository.save(member);
    }
}
