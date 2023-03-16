package weatherwhere.team.login.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weatherwhere.team.login.domain.member.Member;
import weatherwhere.team.login.domain.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
     * @return null 이면 로그인 실패
     */
    public Member login(String loginId, String password){

        return memberRepository.findByLoginId(loginId).filter(m -> m.getUserPw().equals(password)) //값이 같으면 password반환
                .orElse(null); //아니면 null반환

    }
}
