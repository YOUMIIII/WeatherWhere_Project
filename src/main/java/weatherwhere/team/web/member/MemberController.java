package weatherwhere.team.web.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.service.MemberService;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/join") //회원등록 폼으로 보내기
    public String addForm(Model model,@ModelAttribute("memberJoinForm") MemberJoinForm form){ //"member"생략 가능하지만 나중에 타임리프로 인식이 안될경우가 있어서 그냥 적었다함

        return "members/signUp";
    }

    @PostMapping("/join")
    public String save(@Validated @ModelAttribute("memberJoinForm") weatherwhere.team.web.member.MemberJoinForm form, BindingResult bindingResult, Model model, MultipartFile file) throws Exception{

        if(!(form.getUserPw().equals(form.getUserPwCheck()))){
            bindingResult.reject("pwError","작성하신 비밀번호가 일치하지 않습니다.");
        }
        if(bindingResult.hasErrors()){
            //폼에 유효성 문제가 있을 경우 다시 회원가입 페이지로.
            return "/members/signUp";
        }

        if(file.isEmpty()){
            form.setUserPhoto("/img/home/profile/profile.png"); // 사진 등록 안하면 기본사진
        } else {
            form.setUserPhoto(memberService.memberfile(file, form));
        }

        //멤버 객체 생성 (회원가입 X)
        Member member=Member.createMember(
                form.getUserId(),
                form.getUserPw(),
                form.getUserMail(),
                form.getUserLocation(),
                form.getUserLocation2(),
//                memberService.getRegionId(form.getUserLocation(),form.getUserLocation2()),
                form.getUserPhoto()
        );
        //회원을 가입시키고, 그 userId를 반환
        String userId=memberService.join(member);

        model.addAttribute("joinId", form.getUserId());
        model.addAttribute("status", true);
        log.info("회원 {} 가입 완료",userId);
        return "members/signupsuccess";
    }

    @ResponseBody
    @RequestMapping(value = "/memberIdChk", method = RequestMethod.POST)
    public int memberIdChk(String userId) throws Exception{
        log.info("memberIdChk() 진입");
        int result = memberService.idCheck(userId);
        log.info("결과값 = {}" + result);
        return result;
    }

}
