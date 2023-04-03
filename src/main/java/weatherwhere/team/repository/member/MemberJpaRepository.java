package weatherwhere.team.repository.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.member.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberJpaRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public Member findByUserId(String userId){
        List<Member> resultList = em.createQuery("select m from Member m where m.userId=:user_id", Member.class)
                .setParameter("user_id", userId)
                .getResultList();
        return resultList.isEmpty()? null:resultList.get(0);
    }

    public Member findLoginMember(String userId, String userPw){
        List<Member> resultList = em.createQuery("select m from Member m where m.userId=:userId and m.userPw=:userPw", Member.class)
                .setParameter("userId", userId)
                .setParameter("userPw", userPw)
                .getResultList();
        return resultList.isEmpty()? null:resultList.get(0);
    }

}
