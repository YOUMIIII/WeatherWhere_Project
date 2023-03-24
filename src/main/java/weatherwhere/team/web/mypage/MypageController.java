package weatherwhere.team.web.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.board.BoardDTO;
import weatherwhere.team.service.BoardService;
import weatherwhere.team.web.SessionConst;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
    private final BoardService boardService;

    @GetMapping("")
    public String mypage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        //세션이 유지되면 스케줄으로 이동
        model.addAttribute("member", loginMember);
        return "main/mypage";
    }

    @GetMapping("/update-infomation")
    public String updateInfo(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        //세션이 유지되면 스케줄으로 이동
        model.addAttribute("member", loginMember);
        return "main/mypage/updatemyinfo";
    }

    //수정완료페이지 만들고 주석풀기
/*    @PostMapping("/update-infomation")
    public String successUpdateInfo(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        //세션이 유지되면 스케줄으로 이동
        model.addAttribute("member", loginMember);
        return "main/mypage/updatemyinfosuccess";
    }*/

    @GetMapping("/mycloset")
    public String mycloset(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        //세션이 유지되면 스케줄으로 이동
        model.addAttribute("member", loginMember);
        return "main/mypage/mycloset";
    }

    @GetMapping("/favorite")
    public String myFavorite(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @PageableDefault(page = 1) Pageable pageable, Model model){
//        pageable.getPageNumber();
            Page<BoardDTO> boardList = boardService.paging(pageable);
            int blockLimit = 3;
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
            int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

            // page 갯수 20개
            // 현재 사용자가 3페이지
            // 1 2 3
            // 현재 사용자가 7페이지
            // 7 8 9
            // 보여지는 페이지 갯수 3개
            // 총 페이지 갯수 8개

            model.addAttribute("boardList", boardList);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("member", loginMember);
        return "main/mypage/favorite";
    }
}
