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
        /*Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get(); //옵셔널은 값을 꺼낼때 get사용. 값이 없으면 null로 나옴
        if(member.getPassword().equals(password)){
            return member;
        }else {
            return null;
        }
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
        byLoginId.filter(m -> m.getPassword().equals(password)) //값이 같으면 password반환
                .orElse(null); //아니면 null반환*/

        //위 식을 아래 식으로 바꿀 수 있음
        return memberRepository.findByLoginId(loginId).filter(m -> m.getUserPw().equals(password)) //값이 같으면 password반환
                .orElse(null); //아니면 null반환

    }
}
