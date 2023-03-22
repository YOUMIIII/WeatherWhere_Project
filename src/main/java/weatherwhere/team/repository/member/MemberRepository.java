package weatherwhere.team.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.member.Member;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {
//    private static Map<Long, Member> store = new HashMap<>();

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

    //수정중

   /* public Optional<Member> findByLoginId(String loginId){
        Optional<Member> byUserId = memberMapper.findByUserId(loginId);
        *//*List<Member> all = findAll();
        for(Member m : all){
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m); //회원객체가 있을수도 있고 없을수도 있는데 있으면 값을 반환해줌
            }
        }
        return Optional.empty();*//*

        //아래 return은 위 식을 람다식으로 바꿈
        return findAll().stream()
                .filter(m -> m.getUserId().equals(loginId))
                .findFirst();
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values()); //map에서 키는 빼고 값만 가져오기
    }


    //테스트용 초기화
    public void clearStore(){
        store.clear();
    }*/
}
