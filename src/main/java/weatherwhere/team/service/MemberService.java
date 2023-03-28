package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.region.RegionRepository;
import weatherwhere.team.repository.member.MemberJpaRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final RegionRepository regionRepository;


    public String join(Member member){
        validatedDuplicatedMember(member);
        memberJpaRepository.save(member);
        member.getId();
        return member.getUserId();
    }

    public Member findOne(Long memberId){
        return memberJpaRepository.findOne(memberId);
    }

    private void validatedDuplicatedMember(Member member) {
        List<Member> findMembers=memberJpaRepository.findByUserId(member.getUserId());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public void update(Long id, String userId) {
        //member 를 찾아옴
        Member member = memberJpaRepository.findOne(id);
        //변경감지 시작
        member.setUserId(userId);
    }


    public Member login(String userId,String userPw){
        Member loginMember=memberJpaRepository.findLoginMember(userId,userPw);
        return loginMember;
    }

}

