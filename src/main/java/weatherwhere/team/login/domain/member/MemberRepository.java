package weatherwhere.team.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Optional<Member> findByLoginId(String loginId){
        /*List<Member> all = findAll();
        for(Member m : all){
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m); //회원객체가 있을수도 있고 없을수도 있는데 있으면 값을 반환해줌
            }
        }
        return Optional.empty();*/

        //아래 return은 위 식을 람다식으로 바꿈
        return findAll().stream()
                .filter(m -> m.getUserId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values()); //map에서 키는 빼고 값만 가져오기
    }


    //테스트용 초기화
    public void clearStore(){
        store.clear();
    }
}
