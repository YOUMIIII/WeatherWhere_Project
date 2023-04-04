package weatherwhere.team.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.member.Member;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper memberMapper;

    //회원가입
    public Member save(Member member){
        memberMapper.save(member);
        return member;
    }

    //로그인. 값이 없으면 null 반환
    public Member login(String loginId, String loginPw){
        return memberMapper.login(loginId, loginPw);
    }

    public Integer idCheck(String userId){
        return memberMapper.idCheck(userId);
    }

    public String findId(String userMail){
        return memberMapper.findId(userMail);
    }
}
