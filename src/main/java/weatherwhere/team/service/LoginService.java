package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weatherwhere.team.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
     * @return null 이면 로그인 실패
     */

    //로그인 기능을 mapper때문에 일단 MemberRepository에 넣음
    /*public Member login(String loginId, String password){

        return memberRepository.findByLoginId(loginId).filter(m -> m.getUserPw().equals(password)) //값이 같으면 password반환
                .orElse(null); //아니면 null반환

    }*/
}
