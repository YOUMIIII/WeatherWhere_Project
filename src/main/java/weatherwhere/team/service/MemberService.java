package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberJpaRepository;
import weatherwhere.team.repository.member.MemberRepository;
import weatherwhere.team.repository.region.RegionRepository;
import weatherwhere.team.web.member.MemberForm;
import weatherwhere.team.web.mypage.MemberEditForm;
import weatherwhere.team.web.mypage.MemberEditPwForm;

import java.io.File;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;


    public String join(Member member){
        validatedDuplicatedMember(member);
        memberJpaRepository.save(member);
        member.getId();
        return member.getUserId();
    }

    public Member updateMember(Long id, MemberEditForm memberEditForm){
        //트랜잭션이 있는 계층에서 영속 상태의 엔티티를 조회하고 엔티티의 데이터를 변경
        Member member = memberJpaRepository.findOne(id);
        return Member.setEditInfo(member, memberEditForm);//엔티티에서 값을 전부 set 해준 뒤 멤버 객체를 반환
    }

    public Member updateMemberPw(Long id, MemberEditPwForm memberEditPwForm){
        //트랜잭션이 있는 계층에서 영속 상태의 엔티티를 조회하고 엔티티의 데이터를 변경
        Member member = memberJpaRepository.findOne(id);
        return Member.setEditPw(member, memberEditPwForm);//엔티티에서 값을 전부 set 해준 뒤 멤버 객체를 반환
    }

    public Member findMember(String userId){
        return memberJpaRepository.findByUserId(userId);
    }

    private void validatedDuplicatedMember(Member member) {
        Member findMember=memberJpaRepository.findByUserId(member.getUserId());

        if(findMember==null){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    public Member login(String userId,String userPw){
        Member loginMember=memberJpaRepository.findLoginMember(userId,userPw);
        return loginMember;
    }

    public String memberfile(MultipartFile file, MemberForm form) throws Exception{
        /*우리의 프로젝트경로를 담아주게 된다 - 저장할 경로를 지정*/
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/img/home/profile";
        log.info("projectpath = {}", projectPath);

        String fileName = form.getUserId() + "_" + "profile";
        log.info("filename = {}", fileName);

        /*빈 껍데기 생성*/
        /*File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻*/
        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        /*디비에 파일 넣기*/
        form.setUserPhoto("/img/home/profile/" + fileName);

        return form.getUserPhoto();
    }

    public int idCheck(String userId){
        return memberRepository.idCheck(userId);
    }

    public String findId(String userMail){
        return memberRepository.findId(userMail);
    }

}

