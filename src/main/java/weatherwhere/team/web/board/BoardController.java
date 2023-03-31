package weatherwhere.team.web.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weatherwhere.team.domain.board.BoardEntity;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.board.BoardDTO;
import weatherwhere.team.repository.board.BoardRepository;
import weatherwhere.team.repository.board.CommentDTO;
import weatherwhere.team.service.BoardService;
import weatherwhere.team.service.CommentService;
import weatherwhere.team.web.SessionConst;

import java.io.IOException;
import java.util.List;

/*
 * 사이드 바에 정보 넣기
 * 메서드에 추가 하는
 * @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember 부분과
 * 메서드 안에 추가 하는
 * model.addAttribute("member", loginMember); 부분이 필요
 * */

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save") // 새글작성 버튼 클릭 시 동작하는 부분
    public String saveForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        model.addAttribute("member", loginMember); // 사이드바
        System.out.println("새글작성 버튼 클릭 시 회원ID 확인 : loginMember.getUserId() = " + loginMember.getUserId()); // UserId 확인용
        return "main/infoboard/write";
    }

    //글 등록 완료
    @PostMapping("/save") // 글 작성 후 게시글 작성 버튼 클릭 시 동작하는 부분
    public String save(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model,
                       @ModelAttribute BoardDTO boardDTO) throws IOException {
        model.addAttribute("member", loginMember); // 사이드바
        //todo 여기서 넣어주는 userId를 saveForm 메서드로 옮겨줘야함.
//        System.out.println("loginMember.getUserId() = " + loginMember.getUserId()); //UserId 확인용
        boardDTO.setUserId(loginMember.getUserId()); //로그인 아이디 boardDTO에 넣기
        model.addAttribute("board", boardDTO);
        System.out.println("DB에 저장될 boardDTO 정보 = " + boardDTO);

        boardService.save(boardDTO);
//        System.out.println("새 글 작성 후 게시글 작성 버튼 클릭 후");
//        System.out.println("boardDTO = " + boardDTO);
//        System.out.println("<-- DB에 들어가기 직전----------------------------------------------- -->");
        return "main/infoboard/detail";
    }

    @GetMapping("/board")
    public String findAll(Model model) {
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "paging";
//        return "main/infoboard";
    }

    @GetMapping("/{id}")
    public String findById(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           @PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {
        model.addAttribute("member", loginMember); // 사이드바 정보 입력부분
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
         */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "main/infoboard/detail";
    }

    @GetMapping("/update/{id}") // 상세보기페이지에서 수정 버튼 클릭 시 동작하는 부분
    public String updateForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             @PathVariable Long id, Model model) {
        model.addAttribute("member", loginMember); // 사이드바 정보 입력부분
        BoardDTO boardDTO = boardService.findById(id); // 수정할 글 불러오기
        System.out.println("불러온 작성글 boardDTO 정보 = " + boardDTO);

        model.addAttribute("boardUpdate", boardDTO);
        System.out.println("수정 페이지에 보여질 boardDTO = " + boardDTO);
        return "main/infoboard/update";
    }

    @PostMapping("/update/") // 수정페이지에서 글 수정 후 수정버튼 클릭 시 동작하는 부분
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {

//        model.addAttribute("boardUpdate", boardDTO);//
        System.out.println("수정페이지 들어오는 버튼 클릭 후 model에 들어간 boardUpdate = " + boardDTO);
//        boardDTO.setBoardFile(null);

        BoardDTO board = boardService.update(boardDTO);
//        System.out.println("return findById(boardDTO.getId()) = " + board);
        System.out.println("수정 후 모델에 들어갈 boardDTO = " + boardDTO);
        model.addAttribute("board", board);// detail의 board로 넘겨주기위해
//        System.out.println("글 수정하고 수정버튼 클릭 후------------------------------------------------------------------");
//        System.out.println("boardDTO = " + boardDTO);
//        System.out.println("board = " + board);
        return "main/infoboard/detail";
//        return "redirect:/board/" + boardDTO.getId();
    }

    //삭제 코드 작성 완료
    @GetMapping("/delete/{id}")
    public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         @PathVariable("id") Long id) { // 삭제 버튼 클릭 시 동작하는 부분
        System.out.println("삭제 버튼 클릭");
//        boardService. // userId 가져오기
        String userId = boardService.findById(id).getUserId();

//        System.out.println("로그인ID = " + loginMember.getUserId()); //로그인 userId 확인용
//        System.out.println("글작성ID = " + userId); // 글 작성자
//        System.out.println("게시글 번호 : id = " + id); //글 번호

        if (loginMember.getUserId().equals(userId)) {

//            System.out.println("ID 일치");
//            System.out.println(loginMember.getUserId() + userId);
            boardService.delete(id); // boardRepository에서 id 가져옴.
        }
        return "redirect:/board/paging";

    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @PageableDefault(page = 1) Pageable pageable, Model model) {
//        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 5; // 글 목록 아래의 페이지 넘기는 숫자 개수 ex) 1 2 3 원래 설정은 3개 설정
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        //* blockLimit + 1;은 시작 숫자를 말하는듯???  // 1 4 7 10 ~~
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
        return "main/infoboard";
    }

}