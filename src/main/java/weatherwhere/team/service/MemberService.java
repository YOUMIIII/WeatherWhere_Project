package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberJpaRepository;
import weatherwhere.team.repository.region.RegionRepository;
import weatherwhere.team.web.member.MemberJoinForm;

import java.io.File;
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

    public String memberfile(MultipartFile file, MemberJoinForm form) throws Exception{
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

}

