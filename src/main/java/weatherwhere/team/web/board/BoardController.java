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
import weatherwhere.team.repository.board.CommentDTO;
import weatherwhere.team.service.BoardService;
import weatherwhere.team.service.CommentService;
import weatherwhere.team.web.SessionConst;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        model.addAttribute("member", loginMember);
//        System.out.println("loginMember.getUserId() = " + loginMember.getUserId()); // UserId 확인용
        return "main/infoboard/write";
    }

    @PostMapping("/save")
    public String save(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model, @ModelAttribute BoardDTO boardDTO) throws IOException {
        model.addAttribute("member", loginMember);
//        System.out.println("loginMember.getUserId() = " + loginMember.getUserId()); //UserId 확인용
        boardDTO.setUserId(loginMember.getUserId());
        System.out.println("boardDTO = " + boardDTO);
        model.addAttribute("board", boardDTO);

        boardDTO.setId(boardService.count());
        boardService.save(boardDTO);
        System.out.println("<-- ------------------------------------------------ -->");
        System.out.println("count() = " + boardService.count());
        System.out.println("2 boardDTO = " + boardDTO);
        System.out.println("<-- ------------------------------------------------ -->");

        return "main/infoboard/detail";
    }

    @GetMapping("/board")
    public String findAll(Model model) {
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "paging";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {
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
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        boardDTO.setUserId(loginMember.getUserId());
        model.addAttribute("boardUpdate", boardDTO);
        return "main/infoboard/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "main/infoboard/detail";
//        return "redirect:/board/" + boardDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        System.out.println("id = " + id);
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