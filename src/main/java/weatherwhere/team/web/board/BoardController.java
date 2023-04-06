package weatherwhere.team.web.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.board.BoardDTO;
import weatherwhere.team.repository.board.CommentDTO;
import weatherwhere.team.repository.board.FavoriteDTO;
import weatherwhere.team.repository.board.FavoriteRepository;
import weatherwhere.team.service.BoardService;
import weatherwhere.team.service.CommentService;
import weatherwhere.team.web.SessionConst;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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


    private final FavoriteRepository favoriteRepository;

    @GetMapping("/save") // 새글작성 버튼 클릭 시 동작하는 부분
    public String saveForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           Model model,
                           @ModelAttribute("board") BoardDTO boardDTO,
                           @RequestParam String parentRegion,
                           @RequestParam String childRegion) {
        model.addAttribute("member", loginMember); // 사이드바

        boardDTO.setUserId(loginMember.getUserId());
        boardDTO.setParentRegion(parentRegion);
        boardDTO.setChildRegion(childRegion);
        System.out.println("새 글작성 버튼 클릭 시 회원 ID 확인 : loginMember.getUserId() = " + loginMember.getUserId()); // UserId 확인용
        return "main/infoboard/write";
    }

    //글 등록 완료
    @PostMapping("/save") // 글 작성 후 DB에 저장하는 버튼 클릭 시 동작하는 부분
    public String save(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model,
                       @ModelAttribute("board") BoardDTO boardDTO)
                                                                    throws IOException {
        model.addAttribute("member", loginMember); // 사이드바
//        System.out.println("boardDTO 에 저장된 userId : " + boardDTO.getUserId()); //UserId 확인용
        Long savedId = boardService.save(boardDTO);

        if (boardDTO.getBoardFile().isEmpty()) { //첨부파일 유무 확인
            boardDTO.setFileAttached(0);
        } else {
            boardDTO.setFileAttached(1);

            boardDTO.setOriginalFileName(boardDTO.getBoardFile().getOriginalFilename());
            boardDTO.setStoredFileName("image_" + boardDTO.getOriginalFileName());
        }
        System.out.println("DB에 저장되는 boardDTO = " + boardDTO);


        return "redirect:/board/"+savedId;
    }

    //즐겨찾기
    @PostMapping("/addfavorite") //즐겨찾기 추가 버튼 클릭 시 동작
    public String addfavorite(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model,
                            @ModelAttribute("board")
                            BoardDTO boardDTO)
            throws IOException {
        model.addAttribute("member", loginMember); // 사이드바


        Long findId = boardDTO.getId(); // 게시글 id 찾기
        System.out.println("findId = " + findId);

        BoardDTO findBoardDTO = boardService.findById(findId); //게시글번호로 글 정보 찾기

        System.out.println("favoriteRepository = " + favoriteRepository.findAll());


        System.out.println("\uD83E\uDDE1게시글 정보 boardDTO = " + findBoardDTO); // 게시글 정보
        System.out.println("\uD83E\uDDE1boardDTO 에 저장된 writer : " + findBoardDTO.getUserId()); //작성자 확인용
        System.out.println("로그인 ID loginMember = " + loginMember.getUserId()); // 로그인 ID 확인용
        String loginId = loginMember.getUserId();
        boardService.favoriateSave(findBoardDTO, loginId); //게시글 정보와, 로그인 ID 넘기기

        System.out.println("\uD83E\uDDE1글번호 = " + findBoardDTO.getId());
//
//        if (boardDTO.getBoardFile().isEmpty()) { //첨부파일 유무 확인
//            boardDTO.setFileAttached(0);
//        } else {
//            boardDTO.setFileAttached(1);
//
//            boardDTO.setOriginalFileName(boardDTO.getBoardFile().getOriginalFilename());
//            boardDTO.setStoredFileName("image_" + boardDTO.getOriginalFileName());
//        }
//        System.out.println("DB에 저장되는 boardDTO = " + boardDTO);

        return "main/infoboard/detail";
    }






    @GetMapping("/board")
    public String findAll(Model model) {
        // DB 에서 전체 게시글 데이터를 가져와서 list.html 에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
//        return "";
        return "paging";
    }

    @GetMapping("/{id}") //detail.html 화면에 넘겨줄 정보들
    public String findById(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           @PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {
        model.addAttribute("member", loginMember); // 사이드바 정보 입력부분
        System.out.println("\uD83E\uDDE1 로그인 ID 확인 글 작성자인지 로그인한 사람껀지 확인 = " + loginMember.getUserId());
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html 에 출력
         */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        System.out.println("\uD83D\uDC9A 상세보기 페이지로 반환된 boardDTO = " + boardDTO);

        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        System.out.println("\uD83E\uDDE1 commentDTOList = " + commentDTOList);
        model.addAttribute("commentList", commentDTOList); //댓글 목록
        model.addAttribute("board", boardDTO); //게시글 정보
        model.addAttribute("page", pageable.getPageNumber());

//        FavoriteDTO findFavoriteDTO = boardService.favoriteFindById(id);
//        Long boardId = findFavoriteDTO.getBoardId();
//
//        BoardDTO boardDTO = boardService.findById(boardId);
//        System.out.println("\uD83D\uDC9A 상세보기 페이지로 반환된 boardDTO = " + boardDTO);



        return "main/infoboard/detail";
    }

    @GetMapping("/update/{id}") // 상세보기페이지에서 수정 버튼 클릭 시 동작하는 부분
    public String updateForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             @PathVariable Long id,
                             Model model) {
        model.addAttribute("member", loginMember); // 사이드바 정보 입력부분
        BoardDTO editDTO = boardService.findById(id);// 수정할 글 불러오기
        System.out.println("불러온 작성글 boardDTO 정보 = " + editDTO);
        model.addAttribute("board", editDTO);
        System.out.println("수정 페이지에 보여질 boardDTO = " + editDTO);
        return "main/infoboard/update";
    }

    @PostMapping("/update/") // 수정페이지에서 글 수정 후 수정버튼 클릭 시 동작하는 부분
    public String update(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         @ModelAttribute BoardDTO boardDTO,
                         Model model) {

        model.addAttribute("member", loginMember); // 사이드바
//        model.addAttribute("boardUpdate", boardDTO);//
        System.out.println("수정페이지 들어오는 버튼 클릭 후 model에 들어간 boardUpdate = " + boardDTO);

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
                         @PathVariable("id") Long id,
                         RedirectAttributes redirectAttributes) { // 삭제 버튼 클릭 시 동작하는 부분
        System.out.println("삭제 버튼 클릭");
//        boardService. // userId 가져오기
        BoardDTO boardDTO = boardService.findById(id);
        redirectAttributes.addAttribute("parentRegion",boardDTO.getParentRegion());
        redirectAttributes.addAttribute("childRegion", boardDTO.getChildRegion());
        String userId= boardDTO.getUserId();
//        System.out.println("로그인 ID = " + loginMember.getUserId()); //로그인 userId 확인용
//        System.out.println("글작성 ID = " + userId); // 글 작성자
//        System.out.println("게시글 번호 : id = " + id); //글 번호

        if (loginMember.getUserId().equals(userId)) {

//            System.out.println("ID 일치");
//            System.out.println(loginMember.getUserId() + userId);
            boardService.delete(id); // boardRepository에서 id 가져옴.
        }
        return "redirect:/board/paging/region";

    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                         @PageableDefault(page = 1) Pageable pageable, Model model) {
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

    @GetMapping("/paging/region")
    public String pagingByRegion(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                 @PageableDefault(page = 1) Pageable pageable,
                                 @RequestParam String parentRegion,
                                 @RequestParam String childRegion,
                                 Model model){
        // @PageableDefault : 컨트롤러에 Pageable 의 기본값을 설정할 때 사용
        // value 0 , page 0 , size 10 , sort (정렬 기준이 되는 column) 없음 , direction (정렬 방식) ASC
        Page<BoardDTO> boardList = boardService.searchRegionAndPaging(parentRegion,childRegion,pageable);
        int blockLimit = 5;
        int startPage = (pageable.getPageNumber()/blockLimit) * blockLimit + 1;
        int endPage = Math.max(startPage + blockLimit - 1,boardList.getTotalPages());

        //boardList.getNumber() -> 현재 슬라이스의 번호를 구함
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boardList", boardList);
        model.addAttribute("member", loginMember);
        return "main/infoboard";
    }

}