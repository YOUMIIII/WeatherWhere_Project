package weatherwhere.team.web.mypage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import weatherwhere.team.domain.closet.Cloth;
import weatherwhere.team.domain.closet.Diary;
import weatherwhere.team.domain.closet.DiaryContents;
import weatherwhere.team.domain.closet.DiaryInfo;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.board.BoardDTO;
import weatherwhere.team.service.BoardService;
import weatherwhere.team.service.ClothService;
import weatherwhere.team.service.MemberService;
import weatherwhere.team.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final ClothService clothService;

    private String pw = "";


    @GetMapping("")
    public String mypage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        //세션이 유지되면 스케줄으로 이동
        model.addAttribute("member", loginMember);
        return "main/mypage";
    }


    @GetMapping("/update-information-checkpw")
    public String checkPw(HttpServletRequest request, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        model.addAttribute("member", loginMember);
        model.addAttribute("memberCheckPwForm", new MemberCheckPwForm());
        return "main/mypage/updatemyinfo-checkpw";
    }

    @PostMapping("/update-information-checkpw")
    public String checkPwSuccess(@Validated @ModelAttribute("memberCheckPwForm") MemberCheckPwForm form, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        if(!(form.getUserPw().equals(loginMember.getUserPw()))){
            bindingResult.addError(
                    new FieldError("memberCheckPwForm", "userPw", form.getUserPw(), false, null, null, "기존 비밀번호와 일치하지 않습니다.")
            );
            model.addAttribute("member", loginMember);
            return "main/mypage/updatemyinfo-checkpw";
        }

//        model.addAttribute("memberEditForm", MemberEditForm.createMemberEditForm(loginMember));
        model.addAttribute("member", loginMember);
        setBringPw(form.getUserPw());
        return "redirect:/mypage/update-information-select";
    }

    public void setBringPw(String pw){
        this.pw = pw;
    }

    public String bringPw(){
        return pw;
    }

    @GetMapping("/update-information-select")
    public String updateInfoSelect(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        if(bringPw().equals("")){
            model.addAttribute("member", loginMember);
            model.addAttribute("memberCheckPwForm", new MemberCheckPwForm());
            return "redirect:/mypage/update-information-checkpw";
        }
        model.addAttribute("member", loginMember);
        return "main/mypage/updatemyinfo-select";
    }

    @GetMapping("/update-information-pw")
    public String updateInfoPw(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        if(bringPw().equals("")){
            model.addAttribute("member", loginMember);
            model.addAttribute("memberCheckPwForm", new MemberCheckPwForm());
            return "redirect:/mypage/update-information-checkpw";
        }
        model.addAttribute("member", loginMember);
        model.addAttribute("memberEditPwForm", new MemberEditPwForm());
        return "main/mypage/updatemypw";
    }

    @PostMapping("/update-information-pw")
    public String updateInfoPwSuccess(@Validated @ModelAttribute("memberEditPwForm") MemberEditPwForm form, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(!(form.getUserPw().equals(form.getUserPwCheck()))){
            model.addAttribute("member", loginMember);
            bindingResult.reject("pwError","작성하신 비밀번호가 일치하지 않습니다.");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("member", loginMember);
            //에러있으면 다시 수정페이지로.
            return "main/mypage/updatemypw";
        }

        Member updateMember = memberService.updateMemberPw(loginMember.getId(), form);
        //SessionAttribute 도 함께 수정
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, updateMember);

        log.info("회원 {} 정보 변경 : 지역 {} , {}",updateMember.getUserId(),updateMember.getParentRegion(),updateMember.getChildRegion());

        //정상적으로 수정되면 모달창 띄우기 위해 status값 전달
        redirectAttributes.addAttribute("status", true);
        model.addAttribute("member", updateMember);
        return "redirect:/mypage/update-information-pw";
    }

    //비밀번호 외의 정보수정

    @GetMapping("/update-information")
    public String updateInfo(HttpServletRequest request, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        if(bringPw().equals("")){
            model.addAttribute("member", loginMember);
            model.addAttribute("memberCheckPwForm", new MemberCheckPwForm());
            return "redirect:/mypage/update-information-checkpw";
        }
        model.addAttribute("memberEditForm", MemberEditForm.createMemberEditForm(loginMember));
        model.addAttribute("member", loginMember);
        return "main/mypage/updatemyinfo";
    }

    //비밀번호 외의 정보수정
    @PostMapping("/update-information")
    public String successUpdateInfo(@Validated @ModelAttribute("memberEditForm") MemberEditForm form, BindingResult bindingResult, MultipartFile file, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {

        if(bindingResult.hasErrors()){
            model.addAttribute("member", loginMember);
            //에러있으면 다시 수정페이지로.
            return "main/mypage/updatemyinfo";
        }

        //수정 폼이 정상일 경우
        String essentialPhotoPath = "/img/home/profile/profile.png";
        if(file.isEmpty()){
            if(loginMember.getUserPhoto().equals(essentialPhotoPath)){
                form.setUserPhoto(essentialPhotoPath); // 사진 등록 안하면 기본사진
            }else {
                form.setUserPhoto(loginMember.getUserPhoto());
            }
        } else {
            form.setUserPhoto(memberService.memberfile(file, form));
        }

        //멤버 정보 업데이트
        Member updateMember = memberService.updateMember(loginMember.getId(), form);
        //SessionAttribute 도 함께 수정
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,updateMember);

        log.info("회원 {} 정보 변경 : 지역 {} , {}",updateMember.getUserId(),updateMember.getParentRegion(),updateMember.getChildRegion());

        //정상적으로 수정되면 모달창 띄우기 위해 status값 전달
        redirectAttributes.addAttribute("status", true);
        model.addAttribute("member", updateMember);
        return "redirect:/mypage/update-information";
    }

    @GetMapping("/mycloset")
    public String mycloset(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        List<Cloth> clothes = clothService.findAll(loginMember.getUserId());
        List<Cloth> favorites = clothService.findFavorites(loginMember.getUserId());
        List<Cloth> tops = clothService.findTops(loginMember.getUserId());
        List<Cloth> bottoms = clothService.findBottoms(loginMember.getUserId());
        model.addAttribute("member", loginMember);
        model.addAttribute("clothes", clothes);
        model.addAttribute("favorites", favorites);
        model.addAttribute("tops", tops);
        model.addAttribute("bottoms", bottoms);
        return "main/mypage/mycloset";
    }

    @GetMapping("/mycloset/addcloth")
    public String addClothForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        model.addAttribute("member", loginMember);
        model.addAttribute("cloth", new Cloth());
        return "main/mypage/addcloth";
    }

    @PostMapping("/mycloset/addcloth")
    public String addCloth(@Validated @ModelAttribute("cloth") ClothAddForm form, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws Exception{
        if(bindingResult.hasErrors()){
            model.addAttribute("member", loginMember);
            return "main/mypage/addcloth";
        }

        Cloth cloth = new Cloth();
        cloth.setUserId(form.getUserId());
        cloth.setCName(form.getCName());
        cloth.setCKind1(form.getCKind1());
        cloth.setCKind2(form.getCKind2());
        cloth.setCKind3(form.getCKind3());
        cloth.setCWhereToBuy(form.getCWhereToBuy());
        cloth.setCColor(form.getCColor());
        cloth.setCPhoto(form.getCPhoto());
        Cloth savedCloth = clothService.save(cloth, file);
        redirectAttributes.addAttribute("cId", savedCloth.getCId());
        redirectAttributes.addAttribute("status", true);
        model.addAttribute("member", loginMember);
        return "redirect:/mypage/mycloset/{cId}";
    }

    @GetMapping("/mycloset/{cId}")
    public String cloth(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @PathVariable Long cId, Model model){
        Cloth cloth = clothService.findById(cId, loginMember.getUserId());
        model.addAttribute("cloth", cloth);
        model.addAttribute("member", loginMember);
        return "main/mypage/cloth";
    }

    @GetMapping("/mycloset/{cId}/edit")
    public String editClothForm(@PathVariable Long cId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        Cloth cloth = clothService.findById(cId, loginMember.getUserId());
        model.addAttribute("cloth", cloth);
        model.addAttribute("member", loginMember);
        return "main/mypage/updateCloth";
    }

    @PostMapping("/mycloset/{cId}/edit")
    public String edit(@PathVariable Long cId, @ModelAttribute ClothUpdateForm updateParam, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, MultipartFile file, Model model) throws Exception{
        clothService.update(cId, updateParam, file);
        model.addAttribute("member", loginMember);
        return "redirect:/mypage/mycloset/{cId}";
    }

    @PostMapping("/mycloset/{cId}")
    public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @PathVariable Long cId, Model model){
        clothService.delete(cId, loginMember.getUserId());
        List<Cloth> clothes = clothService.findAll(loginMember.getUserId());
        model.addAttribute("member", loginMember);
        model.addAttribute("clothes", clothes);
        return "main/mypage/mycloset";
    }


    @GetMapping("/diary")
    public String diary(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        List<Diary> diaries = clothService.findAllDiary(loginMember.getUserId());
        model.addAttribute("diaries", diaries);
        model.addAttribute("member", loginMember);
        return "main/mypage/diary";
    }

    @GetMapping("/diary/add")
    public String addDiary(@ModelAttribute("diaryForm") DiaryAddForm form, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        List<Cloth> clothes = clothService.findAll(loginMember.getUserId());
        model.addAttribute("member", loginMember);
        model.addAttribute("clothes", clothes);
        return "main/mypage/adddiary";
    }

    @PostMapping("/diary/add")
    public String addDiaryForm(@ModelAttribute("diaryForm") DiaryAddForm form, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, BindingResult bindingResult, MultipartFile file, RedirectAttributes redirectAttributes, Model model ) throws Exception{
        if(bindingResult.hasErrors()){
            model.addAttribute("member", loginMember);
            return "main/mypage/adddiary";
        }
        String id = form.getUserId() + form.getDDate();

        DiaryInfo diaryInfo = new DiaryInfo();
        diaryInfo.setDId(id);
        diaryInfo.setDDate(form.getDDate());
        diaryInfo.setDLocation1(form.getDLocation1());
        diaryInfo.setDLocation2(form.getDLocation2());
        List<Long> dCody = form.getDCody();
        for (Long aLong : dCody) {
            log.info("cody = {}", aLong);
        }
        diaryInfo.setDCody1(dCody.get(0));
        diaryInfo.setDCody2(dCody.get(1));
        diaryInfo.setDCody3(dCody.get(2));

        DiaryInfo savedDI = clothService.saveDI(diaryInfo);
        DiaryContents diaryContents = new DiaryContents();
        diaryContents.setDId(id);
        diaryContents.setUserId(form.getUserId());
        diaryContents.setDScore(form.getDScore());
        diaryContents.setDContent(form.getDContent());
        DiaryContents savedDC = clothService.saveDC(diaryContents, file);


        //일기에 추가된 옷에 count 컬럼 1씩 증가
        clothService.updateClothCount(form.getDCody().get(0));
        clothService.updateClothCount(form.getDCody().get(1));
        clothService.updateClothCount(form.getDCody().get(2));

        //일기 리스트 가져오기
        List<Diary> diaries = clothService.findAllDiary(loginMember.getUserId());
        model.addAttribute("diaries", diaries);
        model.addAttribute("member", loginMember);
        return "redirect:/mypage/diary";

    }

    @GetMapping("/favorite")
    public String myFavorite(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @PageableDefault(page = 1) Pageable pageable, Model model){
//        pageable.getPageNumber();
 /*           Page<BoardDTO> boardList = boardService.paging(pageable);
            int blockLimit = 3;
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
            int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();*/

           Page<BoardDTO> favoriteList = boardService.favoritePaging(pageable);
            int blockLimit = 3;
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
            int endPage = ((startPage + blockLimit - 1) < favoriteList.getTotalPages()) ? startPage + blockLimit - 1 : favoriteList.getTotalPages();

            // page 갯수 20개
            // 현재 사용자가 3페이지
            // 1 2 3
            // 현재 사용자가 7페이지
            // 7 8 9
            // 보여지는 페이지 갯수 3개
            // 총 페이지 갯수 8개

            model.addAttribute("boardList", favoriteList);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("member", loginMember);
        return "main/mypage/favorite";
    }





}
